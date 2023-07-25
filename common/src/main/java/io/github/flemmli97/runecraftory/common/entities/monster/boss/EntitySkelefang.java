package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MultiPartEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.SkelefangAttackGoal;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySlashResidue;
import io.github.flemmli97.runecraftory.common.entities.monster.MultiPartContainer;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.RayTraceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class EntitySkelefang extends BossMonster {

    public static final byte HEAD_DROP = 70;
    public static final byte HEAD_THROW = 71;
    public static final byte NECK_DROP = 72;
    public static final byte NECK_THROW = 73;
    public static final byte FRONT = 74;
    public static final byte FRONT_RIBS = 75;
    public static final byte LEFT_LEG = 76;
    public static final byte RIGHT_LEG = 77;
    public static final byte BACK = 78;
    public static final byte BACK_RIBS = 79;
    public static final byte TAIL = 80;
    public static final byte TAIL_BASE = 81;
    public static final byte HIT = 82;
    public static final byte SHATTER = 83;
    public static final byte CHARGE_BEAM = 84;

    public static final AnimatedAction TAIL_SLAM = new AnimatedAction(2, 0.72, "tail_slam");
    public static final AnimatedAction NEEDLE_THROW = new AnimatedAction(1.16, 0.8, "needle_throw");
    public static final AnimatedAction TAIL_SLAP = new AnimatedAction(0.84, 0.52, "tail_slap");
    public static final AnimatedAction SLASH = new AnimatedAction(0.96, 0.6, "slash");
    public static final AnimatedAction CHARGE = new AnimatedAction(1.5, 0, "charge");
    // 4.5 till start beam, 2 sec beam charge, 4 sec beam duration, 2 sec till restore, 1 sec restoring time
    public static final AnimatedAction BEAM = AnimatedAction.builder(1, "beam").infinite().build();

    public static final AnimatedAction DEATH = AnimatedAction.builder(120, "death").infinite().build();
    public static final AnimatedAction ROAR = new AnimatedAction(1.64, 1, "roar");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(TAIL_SLAM, "interact");

    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{TAIL_SLAP, NEEDLE_THROW, TAIL_SLAM, SLASH, CHARGE, BEAM, DEATH, ROAR, INTERACT};
    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntitySkelefang>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(TAIL_SLAM, (anim, entity) -> {
            if (entity.remainingTailBones() > 10 || entity.isEnraged()) {
                if (anim.canAttack() || anim.getTick() == 24 || anim.getTick() == 32) {
                    entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                    entity.level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 2, 0.7f);
                }
            }
        });
        b.put(NEEDLE_THROW, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.BONE_NEEDLES.get().use(entity);
                if (entity.remainingHeadBones() > 10) {
                    entity.level.broadcastEntityEvent(entity, HEAD_THROW);
                    entity.setHeadBones(10, false);
                } else if (entity.remainingHeadBones() > 0) {
                    entity.level.broadcastEntityEvent(entity, NECK_THROW);
                    entity.setHeadBones(0, false);
                }
            }
        });
        b.put(TAIL_SLAP, (anim, entity) -> {
            if (entity.remainingTailBones() > 10 || entity.isEnraged()) {
                if (anim.canAttack()) {
                    entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                }
            }
        });
        b.put(SLASH, (anim, entity) -> {
            if (anim.canAttack()) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                Vec3 dir;
                Vec3 side;
                if (entity.getControllingPassenger() instanceof Player player) {
                    dir = Vec3.directionFromRotation(0, player.yBodyRot);
                    side = Vec3.directionFromRotation(0, player.yBodyRot + 90);
                } else {
                    dir = Vec3.directionFromRotation(0, entity.yBodyRot);
                    side = Vec3.directionFromRotation(0, entity.yBodyRot + 90);
                }
                dir = dir.scale(entity.getBbWidth() * 0.5 + 1);
                if (entity.remainingLeftLegBones() > 0) {
                    Vec3 leftPos = entity.position().add(dir).add(side.scale(-1.3));
                    EntitySlashResidue slash = new EntitySlashResidue(entity.level, entity);
                    slash.setType(EntitySlashResidue.Type.SKELEFANG);
                    slash.setPos(leftPos.x, leftPos.y, leftPos.z);
                    slash.setXRot(0);
                    slash.setYRot(entity.getYRot());
                    entity.level.addFreshEntity(slash);
                }
                if (entity.remainingRightLegBones() > 0) {
                    Vec3 rightPos = entity.position().add(dir).add(side.scale(1.3));
                    EntitySlashResidue slash = new EntitySlashResidue(entity.level, entity);
                    slash.setType(EntitySlashResidue.Type.SKELEFANG);
                    slash.setPos(rightPos.x, rightPos.y, rightPos.z);
                    slash.setXRot(0);
                    slash.setYRot(entity.getYRot());
                    entity.level.addFreshEntity(slash);
                }
            }
        });
        b.put(CHARGE, (anim, entity) -> {
            if (entity.hitEntity == null)
                entity.hitEntity = new ArrayList<>();
            Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : Vec3.directionFromRotation(0, entity.getYRot());
            dir = new Vec3(dir.x(), 0, dir.z());
            if (dir.lengthSqr() < 0.5)
                entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
            else {
                dir = dir.normalize().scale(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.3);
                entity.setDeltaMovement(dir.x(), entity.getDeltaMovement().y, dir.z());
            }
            entity.mobAttack(anim, null, e -> {
                if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                        new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.UP), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE))) {
                    entity.hitEntity.add(e);
                }
            });
        });
        b.put(BEAM, (anim, entity) -> {
            if (anim.getTick() == 90)
                entity.level.broadcastEntityEvent(entity, CHARGE_BEAM);
            if (anim.getTick() == 130)
                ModSpells.ENERGY_ORB_SPELL.get().use(entity);
            if (anim.getTick() == 230)
                entity.restoreDragon();
            if (anim.getTick() >= 250)
                entity.getAnimationHandler().setAnimation(null);
        });
    });

    private static final EntityDataAccessor<Integer> HEAD_BONES = SynchedEntityData.defineId(EntitySkelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL_BONES = SynchedEntityData.defineId(EntitySkelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LEFT_LEG_BONES = SynchedEntityData.defineId(EntitySkelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RIGHT_LEG_BONES = SynchedEntityData.defineId(EntitySkelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BODY_BONES = SynchedEntityData.defineId(EntitySkelefang.class, EntityDataSerializers.INT);

    public final SkelefangAttackGoal<EntitySkelefang> attack = new SkelefangAttackGoal<>(this);
    private final AnimationHandler<EntitySkelefang> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (anim != null) {
                    this.hitEntity = null;
                }
            });

    protected List<LivingEntity> hitEntity;

    private final MultiPartContainer head;
    private final MultiPartContainer back;
    private final MultiPartContainer rightLeg;
    private final MultiPartContainer leftLeg;
    private int hurtResist;

    public EntitySkelefang(EntityType<? extends EntitySkelefang> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.head = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 1.3f));
        this.back = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 1.5f));
        this.rightLeg = new MultiPartContainer(() -> new MultiPartEntity(this, 1.5f, 2.5f));
        this.leftLeg = new MultiPartContainer(() -> new MultiPartEntity(this, 1.5f, 2.5f));
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        super.applyAttributes();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HEAD_BONES, 20);
        this.entityData.define(TAIL_BONES, 20);
        this.entityData.define(LEFT_LEG_BONES, 20);
        this.entityData.define(RIGHT_LEG_BONES, 20);
        this.entityData.define(BODY_BONES, 20);
    }

    public void restoreDragon() {
        this.entityData.set(HEAD_BONES, 20);
        this.entityData.set(TAIL_BONES, 20);
        this.entityData.set(LEFT_LEG_BONES, 20);
        this.entityData.set(RIGHT_LEG_BONES, 20);
        this.entityData.set(BODY_BONES, 20);
    }

    public void setHeadBones(int amount) {
        this.setHeadBones(amount, true);
    }

    private void setHeadBones(int amount, boolean withParticle) {
        int pre = this.entityData.get(HEAD_BONES);
        this.entityData.set(HEAD_BONES, Math.min(amount, 20));
        if (withParticle) {
            if (pre > 10 && amount <= 10)
                this.level.broadcastEntityEvent(this, HEAD_DROP);
            if (amount <= 0)
                this.level.broadcastEntityEvent(this, NECK_DROP);
        }
    }

    public int remainingHeadBones() {
        return this.entityData.get(HEAD_BONES);
    }

    public void setTailBones(int amount) {
        int pre = this.entityData.get(TAIL_BONES);
        this.entityData.set(TAIL_BONES, Math.min(amount, 20));
        if (pre > 10 && amount <= 10)
            this.level.broadcastEntityEvent(this, TAIL);
        if (amount <= 0)
            this.level.broadcastEntityEvent(this, TAIL_BASE);
    }

    public int remainingTailBones() {
        return this.entityData.get(TAIL_BONES);
    }

    public void setLeftLegBones(int amount) {
        this.entityData.set(LEFT_LEG_BONES, Math.min(amount, 20));
        if (amount <= 0)
            this.level.broadcastEntityEvent(this, LEFT_LEG);
    }

    public int remainingLeftLegBones() {
        return this.entityData.get(LEFT_LEG_BONES);
    }

    public void setRightLegBones(int amount) {
        this.entityData.set(RIGHT_LEG_BONES, Math.min(amount, 20));
        if (amount <= 0)
            this.level.broadcastEntityEvent(this, RIGHT_LEG);
    }

    public int remainingRightLegBones() {
        return this.entityData.get(RIGHT_LEG_BONES);
    }

    public void setBodyBones(int amount) {
        int pre = this.entityData.get(BODY_BONES);
        this.entityData.set(BODY_BONES, Math.min(amount, 20));
        if (pre > 15 && amount <= 15)
            this.level.broadcastEntityEvent(this, BACK_RIBS);
        if (pre > 10 && amount <= 10)
            this.level.broadcastEntityEvent(this, BACK);
        if (pre > 5 && amount <= 5)
            this.level.broadcastEntityEvent(this, FRONT_RIBS);
        if (amount <= 0)
            this.level.broadcastEntityEvent(this, FRONT);
    }

    public int remainingBodyBones() {
        return this.entityData.get(BODY_BONES);
    }

    public boolean hasBones() {
        return this.remainingHeadBones() > 0 || this.remainingTailBones() > 0
                || this.remainingLeftLegBones() > 0 || this.remainingRightLegBones() > 0
                || this.remainingBodyBones() > 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HeadBones", this.remainingHeadBones());
        compound.putInt("TailBones", this.remainingTailBones());
        compound.putInt("LeftLegBones", this.remainingLeftLegBones());
        compound.putInt("RightLegBones", this.remainingRightLegBones());
        compound.putInt("BodyBones", this.remainingBodyBones());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.entityData.set(HEAD_BONES, compound.getInt("HeadBones"));
        this.entityData.set(TAIL_BONES, compound.getInt("TailBones"));
        this.entityData.set(LEFT_LEG_BONES, compound.getInt("LeftLegBones"));
        this.entityData.set(RIGHT_LEG_BONES, compound.getInt("RightLegBones"));
        this.entityData.set(BODY_BONES, compound.getInt("BodyBones"));
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(ROAR, DEATH, INTERACT, BEAM))
            return false;
        if (type == AnimationType.GENERICATTACK) {
            if (anim.is(SLASH))
                return this.remainingRightLegBones() > 0 || this.remainingLeftLegBones() > 0;
            if (anim.is(TAIL_SLAM, TAIL_SLAP))
                return this.remainingTailBones() > 10 || this.isEnraged();
            if (anim.is(CHARGE))
                return this.random.nextFloat() < 0.6;
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            --this.hurtResist;
            this.updateParts();
            if (!this.isDeadOrDying() && !this.hasBones() && !this.getAnimationHandler().isCurrent(BEAM))
                this.getAnimationHandler().setAnimation(BEAM);
            if (!this.getAnimationHandler().hasAnimation() && !this.isTamed() && this.getHealth() / this.getMaxHealth() < 0.5 && !this.isEnraged())
                this.setEnraged(true, false);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch (id) {
            case HEAD_DROP -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.HEAD, this.getXRot(), this.yHeadRot, 1, 0),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case HEAD_THROW -> {
                Vec3 look = Vec3.directionFromRotation(0, this.yBodyRot);
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.HEAD, this.getXRot(), this.yHeadRot, -2, 0, 40, false),
                        this.getX(), this.getY(), this.getZ(), look.x, look.y, look.z);
            }
            case NECK_DROP -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.NECK, this.getXRot(), this.yHeadRot, 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case NECK_THROW -> {
                Vec3 look = Vec3.directionFromRotation(0, this.yBodyRot);
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.NECK, this.getXRot(), this.yHeadRot, -2, 0, 40, false),
                        this.getX(), this.getY(), this.getZ(), look.x, look.y, look.z);
            }
            case FRONT -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.FRONT, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case FRONT_RIBS -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.FRONT_RIBS, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case LEFT_LEG -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.LEFT_LEG, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case RIGHT_LEG -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.RIGHT_LEG, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case BACK -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.BACK, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case BACK_RIBS -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.BACK_RIBS, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case TAIL -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.TAIL, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case TAIL_BASE -> {
                this.level.addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.TAIL_BASE, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                        this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            }
            case HIT -> {
                int amount = this.random.nextInt(6) + 12;
                for (int i = 0; i < amount; i++) {
                    SkelefangParticleData.SkelefangBoneType type = this.random.nextFloat() < 0.4 ? SkelefangParticleData.SkelefangBoneType.GENERIC : SkelefangParticleData.SkelefangBoneType.GENERIC2;
                    this.level.addAlwaysVisibleParticle(new SkelefangParticleData(type, this.getXRot() + this.random.nextInt(40) - 20, this.yHeadRot + this.random.nextInt(360), this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getRandomX(1.3), this.getY(0.5) + this.getBbHeight() * 0.5, this.getRandomZ(1.3), this.random.nextGaussian() * 0.11, this.random.nextGaussian() * 0.11, this.random.nextGaussian() * 0.11);
                }
            }
            case SHATTER -> {
                int amount = this.random.nextInt(15) + 35;
                if (this.hasBones()) {
                    for (int i = 0; i < amount; i++) {
                        SkelefangParticleData.SkelefangBoneType type = this.random.nextFloat() < 0.4 ? SkelefangParticleData.SkelefangBoneType.GENERIC : SkelefangParticleData.SkelefangBoneType.GENERIC2;
                        this.level.addAlwaysVisibleParticle(new SkelefangParticleData(type, this.getXRot() + this.random.nextInt(40) - 20, this.yHeadRot + this.random.nextInt(360), this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                                this.getRandomX(1.3), this.getY(0.5) + this.getBbHeight() * 0.5, this.getRandomZ(1.3), this.random.nextGaussian() * 0.13, this.random.nextGaussian() * 0.13, this.random.nextGaussian() * 0.13);
                    }
                }
                if (this.remainingHeadBones() > 10)
                    this.handleEntityEvent(HEAD_DROP);
                if (this.remainingHeadBones() > 0)
                    this.handleEntityEvent(NECK_DROP);
                if (this.remainingBodyBones() > 0)
                    this.handleEntityEvent(FRONT);
                if (this.remainingBodyBones() > 5)
                    this.handleEntityEvent(FRONT_RIBS);
                if (this.remainingBodyBones() > 10)
                    this.handleEntityEvent(BACK);
                if (this.remainingBodyBones() > 15)
                    this.handleEntityEvent(BACK_RIBS);
                if (this.remainingLeftLegBones() > 0)
                    this.handleEntityEvent(LEFT_LEG);
                if (this.remainingRightLegBones() > 0)
                    this.handleEntityEvent(RIGHT_LEG);
                if (this.remainingTailBones() > 0)
                    this.handleEntityEvent(TAIL_BASE);
                if (this.remainingTailBones() > 10)
                    this.handleEntityEvent(TAIL);
            }
            case CHARGE_BEAM -> {
                Vec3 center = this.position().add(0, this.getBbHeight() * 0.5, 0);
                List<Vector3f> locations = new ArrayList<>();
                double speed = (this.getBbWidth() + 2) / 40;
                locations.addAll(RayTraceUtils.rotatedVecs(MathUtils.normalX.scale(this.getBbWidth() + 2), MathUtils.normalZ, -180, 180, 10));
                locations.addAll(RayTraceUtils.rotatedVecs(MathUtils.normalX.scale(this.getBbWidth() + 2), MathUtils.normalY, -180, 180, 10));
                locations.addAll(RayTraceUtils.rotatedVecs(MathUtils.normalY.scale(this.getBbWidth() + 2), MathUtils.normalX, -180, 180, 10));
                for (Vector3f vec : locations) {
                    Vec3 pos = center.add(vec.x(), vec.y(), vec.z());
                    Vec3 dir = new Vec3(vec.x(), vec.y(), vec.z()).normalize().scale(speed);
                    this.level.addAlwaysVisibleParticle(new DurationalParticleData(217 / 255f, 248 / 255f, 252 / 255f, 0.4f, 2.3f, 40),
                            pos.x(), pos.y(), pos.z(), -dir.x(), -dir.y(), -dir.z());
                }
            }
            default -> super.handleEntityEvent(id);
        }
    }

    private void updateParts() {
        if (this.remainingTailBones() > 0) {
            Vec3 view = this.calculateViewVector(0, this.yBodyRot);
            Vec3 backPos = this.position().add(view.scale(-1)).add(0, 1, 0);
            this.back.updatePositionTo(backPos.x(), backPos.y(), backPos.z(), true);
        } else {
            this.back.removeEntity();
        }
        if (this.remainingHeadBones() > 10) {
            Vec3 headPos = this.position().add(this.calculateViewVector(this.getXRot() * 0.6f, this.yBodyRot).scale(2.9).add(0, 2.15, 0));
            this.head.updatePositionTo(headPos.x(), headPos.y(), headPos.z(), true);
        } else {
            this.head.removeEntity();
        }
        Vec3 side = this.calculateViewVector(0, this.yBodyRot + 90);
        if (this.remainingLeftLegBones() > 0) {
            Vec3 leftLegPos = this.position().add(side.scale(-1)).add(0, 0, 0);
            this.leftLeg.updatePositionTo(leftLegPos.x(), leftLegPos.y(), leftLegPos.z(), true);
        } else {
            this.leftLeg.removeEntity();
        }
        if (this.remainingRightLegBones() > 0) {
            Vec3 rightLegPos = this.position().add(side.scale(1)).add(0, 0, 0);
            this.rightLeg.updatePositionTo(rightLegPos.x(), rightLegPos.y(), rightLegPos.z(), true);
        } else {
            this.rightLeg.removeEntity();
        }
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        int diffAdd = this.difficultyCooldown();
        return 25 + this.getRandom().nextInt(15) - (this.isEnraged() ? 13 : 0) + diffAdd;
    }

    @Override
    protected boolean checkRage() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !(this.getAnimationHandler().isCurrent(ROAR)) && super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        if (damageSrc == DamageSource.OUT_OF_WORLD) {
            super.actuallyHurt(damageSrc, damageAmount);
            if (this.isDeadOrDying())
                this.level.broadcastEntityEvent(this, (byte) 83);
            return;
        }
        if (this.hurtResist > 0)
            return;
        this.hurtResist = 2;
        if (this.hasBones()) {
            if (damageAmount > 4) {
                if (this.remainingTailBones() > 0) {
                    this.setTailBones(this.remainingTailBones() - 7);
                } else if (this.remainingLeftLegBones() > 0) {
                    this.setLeftLegBones(this.remainingLeftLegBones() - 7);
                } else if (this.remainingRightLegBones() > 0) {
                    this.setRightLegBones(this.remainingRightLegBones() - 7);
                } else if (this.remainingHeadBones() > 0) {
                    this.setHeadBones(this.remainingHeadBones() - 7);
                } else if (this.remainingBodyBones() > 0) {
                    this.setBodyBones(this.remainingBodyBones() - 6);
                }
            }
            if (this.isDeadOrDying())
                this.level.broadcastEntityEvent(this, (byte) 83);
            else
                this.level.broadcastEntityEvent(this, (byte) 82);
            if (!this.hasBones())
                this.getAnimationHandler().setAnimation(BEAM);
        } else
            super.actuallyHurt(damageSrc, damageAmount);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEATH;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.lookAt(target, 180.0f, 50.0f);
        }
        this.getNavigation().stop();
        BiConsumer<AnimatedAction, EntitySkelefang> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void mobAttack(AnimatedAction anim, LivingEntity target, Consumer<LivingEntity> cons) {
        if (anim.is(CHARGE)) {
            double widthH = this.getBbWidth() * 0.5 + 1.5;
            AABB aabb = new AABB(-widthH, -0.02, -widthH, widthH, 1.8 + 0.02, widthH).move(this.getX(), this.getY(), this.getZ())
                    .move(Vec3.directionFromRotation(0, this.yBodyRot).scale(1.5));
            this.level.getEntitiesOfClass(LivingEntity.class, aabb, this.hitPred).forEach(cons);
            if (this.getServer() != null)
                Platform.INSTANCE.sendToAll(new S2CAttackDebug(aabb), this.getServer());
            return;
        }
        List<AABB> aabbs = new ArrayList<>();
        if (anim.is(TAIL_SLAP)) {
            double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth() * 0.5;
            Vec3 dir;
            if (target != null && !this.canBeControlledByRider()) {
                dir = target.position().subtract(this.position());
                dir = new Vec3(dir.x(), Mth.clamp(dir.y(), -0.1, 0.1), dir.z()).normalize();
            } else {
                if (this.getControllingPassenger() instanceof Player player)
                    dir = player.getLookAngle();
                else
                    dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot());
            }
            Vec3 attackPos = this.position().add(dir.scale(this.getBbWidth() + reach + 3));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
            attackPos = this.position().add(dir.scale(this.getBbWidth() + reach));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
            attackPos = this.position().add(dir.scale(this.getBbWidth()));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
        }
        if (anim.is(TAIL_SLAM)) {
            double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth() * 0.5;
            Vec3 dir;
            if (this.getControllingPassenger() instanceof Player player)
                dir = Vec3.directionFromRotation(0, player.getYRot());
            else
                dir = Vec3.directionFromRotation(0, this.getYRot());
            double angle = 0;
            Vec3 offset = dir;
            if (anim.canAttack())
                angle = 50;
            else if (anim.getTick() == 32)
                angle = -30;
            if (angle != 0)
                offset = MathUtils.rotate(MathUtils.normalY, dir, (float) (Mth.DEG_TO_RAD * angle));
            Vec3 attackPos = this.position().add(dir.scale(this.getBbWidth())).add(offset.scale(reach + 3));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
            attackPos = this.position().add(dir.scale(this.getBbWidth())).add(offset.scale(reach));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
            attackPos = this.position().add(dir.scale(this.getBbWidth()));
            aabbs.add(new AABB(-1.25, -0.02, -1.25, 1.25, 1.8 + 0.02, 1.25).move(attackPos.x, attackPos.y, attackPos.z));
        }
        if (anim.is(SLASH)) {
            double reach = 1;
            Vec3 dir;
            Vec3 side;
            if (this.getControllingPassenger() instanceof Player player) {
                dir = Vec3.directionFromRotation(0, player.yBodyRot);
                side = Vec3.directionFromRotation(0, player.yBodyRot + 90);
            } else {
                dir = Vec3.directionFromRotation(0, this.yBodyRot);
                side = Vec3.directionFromRotation(0, this.yBodyRot + 90);
            }
            dir = dir.scale(this.getBbWidth() * 0.5 + reach);
            if (this.remainingLeftLegBones() > 0) {
                Vec3 rightPos = this.position().add(dir).add(side.scale(1.3));
                aabbs.add(new AABB(-1.6, -0.02, -1.6, 1.6, 1.8 + 0.02, 1.6).move(rightPos.x, rightPos.y, rightPos.z));
            }
            if (this.remainingRightLegBones() > 0) {
                Vec3 leftPos = this.position().add(dir).add(side.scale(-1.3));
                aabbs.add(new AABB(-1.6, -0.02, -1.6, 1.6, 1.8 + 0.02, 1.6).move(leftPos.x, leftPos.y, leftPos.z));
            }
        }
        Set<LivingEntity> targets = new HashSet<>();
        for (AABB aabb : aabbs) {
            targets.addAll(this.level.getEntitiesOfClass(LivingEntity.class, aabb, this.hitPred));
            if (this.getServer() != null)
                Platform.INSTANCE.sendToAll(new S2CAttackDebug(aabb), this.getServer());
        }
        targets.forEach(cons);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE);
            else
                this.getAnimationHandler().setAnimation(TAIL_SLAP);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ROAR);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public AnimationHandler<EntitySkelefang> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return DEATH;
    }
}
