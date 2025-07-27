package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MultiPartEntity;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.SlashResidueEntity;
import io.github.flemmli97.runecraftory.common.entities.monster.MultiPartContainer;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CAttackDebug;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.particles.DurationalParticleData;
import io.github.flemmli97.runecraftory.common.particles.SkelefangParticleData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.MathUtils;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Skelefang extends BossMonster {

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

    private static final EntityDataAccessor<Integer> HEAD_BONES = SynchedEntityData.defineId(Skelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TAIL_BONES = SynchedEntityData.defineId(Skelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LEFT_LEG_BONES = SynchedEntityData.defineId(Skelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> RIGHT_LEG_BONES = SynchedEntityData.defineId(Skelefang.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> BODY_BONES = SynchedEntityData.defineId(Skelefang.class, EntityDataSerializers.INT);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String TAIL_SLAM = BUILDER.add("tail_slam", AnimationsBuilder.definition(2)
            .marker("attack_1", 0.72).marker("attack_2", 1.2).marker("attack_3", 1.64));
    public static final String INTERACT = BUILDER.add("interact", TAIL_SLAM);
    public static final String NEEDLE_THROW = BUILDER.add("needle_throw", AnimationsBuilder.definition(1.16).marker("attack", 0.8));
    public static final String TAIL_SLAP = BUILDER.add("tail_slap", AnimationsBuilder.definition(0.84).marker("attack", 0.52));
    public static final String SLASH = BUILDER.add("slash", AnimationsBuilder.definition(0.96).marker("attack", 0.6));
    public static final String CHARGE = BUILDER.add("charge", AnimationsBuilder.definition(1.5).marker("attack_start", 0).marker("attack_end"));
    // 4.5 till start beam, 2 sec beam charge, 4 sec beam duration, 2 sec till restore, 1 sec restoring time
    public static final String BEAM = BUILDER.add("beam", AnimationsBuilder.definition(13)
            .marker("charge", 4.5).marker("beam", 6.5)
            .marker("restore_start", 11).marker("restore_end", 12)
            .marker("restore", 11.5));
    public static final String DEATH = BUILDER.add("death", AnimationsBuilder.definition(10).infinite());
    public static final String ROAR = BUILDER.add("roar", AnimationsBuilder.definition(2).marker("roar", 0.28));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Skelefang>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(TAIL_SLAM, (anim, entity) -> {
            if (entity.remainingTailBones() > 10 || entity.isEnraged()) {
                if (anim.isAt("attack_1") || anim.isAt("attack_2") || anim.isAt("attack_3")) {
                    entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                    entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, entity.getSoundSource(), 2, 0.7f);
                }
            }
        });
        b.put(NEEDLE_THROW, (anim, entity) -> {
            if (anim.isAt("attack")) {
                RuneCraftorySpells.BONE_NEEDLES.get().use(entity);
                if (entity.remainingHeadBones() > 10) {
                    entity.level().broadcastEntityEvent(entity, HEAD_THROW);
                    entity.setHeadBones(10, false);
                } else if (entity.remainingHeadBones() > 0) {
                    entity.level().broadcastEntityEvent(entity, NECK_THROW);
                    entity.setHeadBones(0, false);
                }
            }
        });
        b.put(TAIL_SLAP, (anim, entity) -> {
            if (entity.remainingTailBones() > 10 || entity.isEnraged()) {
                if (anim.isAt("attack")) {
                    entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                }
            }
        });
        b.put(SLASH, (anim, entity) -> {
            if (anim.isAt("attack")) {
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
                    SlashResidueEntity slash = new SlashResidueEntity(entity.level(), entity);
                    slash.setSize(1.5f);
                    slash.setOneTime();
                    slash.setPos(leftPos.x, leftPos.y, leftPos.z);
                    slash.setXRot(0);
                    slash.setYRot(entity.yBodyRot);
                    entity.level().addFreshEntity(slash);
                }
                if (entity.remainingRightLegBones() > 0) {
                    Vec3 rightPos = entity.position().add(dir).add(side.scale(1.3));
                    SlashResidueEntity slash = new SlashResidueEntity(entity.level(), entity);
                    slash.setSize(1.5f);
                    slash.setOneTime();
                    slash.setPos(rightPos.x, rightPos.y, rightPos.z);
                    slash.setXRot(0);
                    slash.setYRot(entity.yBodyRot);
                    entity.level().addFreshEntity(slash);
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
            if (entity.tickCount % 5 == 0) {
                entity.playSound(RuneCraftorySounds.ENTITY_GENERIC_HEAVY_CHARGE.get(), 1, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2f + 1.0f);
                S2CScreenShake.sendAround(entity, 24, 10, 1);
            }
            entity.mobAttack(anim, null, e -> {
                if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                        new DynamicDamage.Builder(entity).hurtResistant(5).knock(DynamicDamage.KnockBackType.UP, 0.7f).withChangedAttribute(RuneCraftoryAttributes.STUN.asHolder(), 70))) {
                    entity.hitEntity.add(e);
                }
            });
        });
        b.put(BEAM, (anim, entity) -> {
            if (anim.isAt("charge"))
                entity.level().broadcastEntityEvent(entity, CHARGE_BEAM);
            if (anim.isAt("beam"))
                RuneCraftorySpells.ENERGY_ORB_SPELL.get().use(entity);
            if (anim.isAt("restore"))
                entity.restoreDragon();
        });
        b.put(ROAR, (anim, entity) -> {
            if (anim.isAt("roar")) {
                entity.playSound(RuneCraftorySounds.ENTITY_SKELEFANG_ROAR.get(), 1, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2f + 1.0f);
                S2CScreenShake.sendAround(entity, 32, 40, 2);
            }
        });
    });

    private final AnimationHandler<Skelefang> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (anim != null) {
                    this.hitEntity = null;
                }
                if (!this.level().isClientSide && anim == null) {
                    boolean chain = !this.commanded;
                    this.commanded = false;
                    if (chain) {
                        if (this.getAnimationHandler().isCurrent(NEEDLE_THROW)) {
                            if (!this.isEnraged() || this.needleChain >= 2 || (this.needleChain == 1 && this.getRandom().nextFloat() > 0.25)) {
                                this.needleChain = 0;
                                return false;
                            }
                            this.getAnimationHandler().setAnimation(NEEDLE_THROW);
                            this.needleChain++;
                            return true;
                        }
                    }
                }
                return false;
            });
    protected List<LivingEntity> hitEntity;
    private int hurtResist;
    private boolean ignoreHurt;
    private boolean commanded;
    private int needleChain;

    private final MultiPartContainer head;
    private final MultiPartContainer back;
    private final MultiPartContainer rightLeg;
    private final MultiPartContainer leftLeg;

    public Skelefang(EntityType<? extends Skelefang> type, Level world) {
        super(type, world);
        this.head = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 1.3f)
                .updatePosition(new Vec3(0, 2.15, 2.9)));
        this.back = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 1.5f)
                .updatePosition(new Vec3(0, 1, -1.5)));
        this.leftLeg = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 2.5f)
                .updatePosition(new Vec3(1.2, 0, 0)));
        this.rightLeg = new MultiPartContainer(() -> new MultiPartEntity(this, 1.6f, 2.5f)
                .updatePosition(new Vec3(-1.2, 0, 0)));
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.SKELEFANG_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        super.applyAttributes();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HEAD_BONES, 20);
        builder.define(TAIL_BONES, 20);
        builder.define(LEFT_LEG_BONES, 20);
        builder.define(RIGHT_LEG_BONES, 20);
        builder.define(BODY_BONES, 20);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (key.equals(BODY_BONES)) {
            if (!this.hasBones())
                this.ignoreHurt = true;
        }
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Skelefang>create()
                .start(MonsterBehaviourUtils.checkedAttack(TAIL_SLAM)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.remainingTailBones() > 10 && MonsterBehaviourUtils.ifCloserThan(6).test(m))
                .prepare(new SetWalkTargetToAttackTarget<Skelefang>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4))).prepareOptional(new MoveToAttackTarget<>())
                .end(10)
                .start(MonsterBehaviourUtils.checkedAttack(TAIL_SLAP)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.remainingTailBones() > 10 && MonsterBehaviourUtils.ifCloserThan(6).test(m))
                .prepare(new SetWalkTargetToAttackTarget<Skelefang>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(12)
                .start(MonsterBehaviourUtils.checkedAttack(NEEDLE_THROW)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<Skelefang>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(10)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(SLASH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetToAttackTarget<Skelefang>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(4)))
                .prepareOptional(new MoveToAttackTarget<>())
                .end(9)
                .start(MonsterBehaviourUtils.checkedAttack(CHARGE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepareOptional(new MoveToAttackTarget<>())
                .end(11)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(1, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>()).build();
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
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ROAR);
    }

    @Override
    protected boolean checkRage() {
        return false;
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            super.actuallyHurt(source, damageAmount);
            if (this.isDeadOrDying())
                this.level().broadcastEntityEvent(this, (byte) 83);
            return;
        }
        if (this.hurtResist > 0)
            return;
        this.hurtResist = 2;
        if (this.hasBones()) {
            if (damageAmount > 4) {
                int boneDamage = 7;
                if (this.remainingTailBones() > 0) {
                    int amount = Math.min(boneDamage, this.remainingTailBones());
                    boneDamage -= amount;
                    this.setTailBones(this.remainingTailBones() - amount);
                }
                if (this.remainingLeftLegBones() > 0) {
                    int amount = Math.min(boneDamage, this.remainingLeftLegBones());
                    boneDamage -= amount;
                    this.setLeftLegBones(this.remainingLeftLegBones() - amount);
                }
                if (this.remainingRightLegBones() > 0) {
                    int amount = Math.min(boneDamage, this.remainingRightLegBones());
                    boneDamage -= amount;
                    this.setRightLegBones(this.remainingRightLegBones() - amount);
                }
                if (this.remainingHeadBones() > 0) {
                    int amount = Math.min(boneDamage, this.remainingHeadBones());
                    boneDamage -= amount;
                    this.setHeadBones(this.remainingHeadBones() - amount);
                }
                if (this.remainingBodyBones() > 0) {
                    int amount = Math.min(boneDamage, this.remainingBodyBones());
                    this.setBodyBones(this.remainingBodyBones() - amount);
                }
            }
            if (this.isDeadOrDying())
                this.level().broadcastEntityEvent(this, (byte) 83);
            else
                this.level().broadcastEntityEvent(this, (byte) 82);
            if (!this.hasBones())
                this.getAnimationHandler().setAnimation(BEAM);
        } else
            super.actuallyHurt(source, damageAmount);
    }

    public void setTailBones(int amount) {
        int pre = this.entityData.get(TAIL_BONES);
        this.entityData.set(TAIL_BONES, Math.min(amount, 20));
        if (pre > 10 && amount <= 10)
            this.level().broadcastEntityEvent(this, TAIL);
        if (amount <= 0)
            this.level().broadcastEntityEvent(this, TAIL_BASE);
    }

    public void setLeftLegBones(int amount) {
        this.entityData.set(LEFT_LEG_BONES, Math.min(amount, 20));
        if (amount <= 0)
            this.level().broadcastEntityEvent(this, LEFT_LEG);
    }

    public void setRightLegBones(int amount) {
        this.entityData.set(RIGHT_LEG_BONES, Math.min(amount, 20));
        if (amount <= 0)
            this.level().broadcastEntityEvent(this, RIGHT_LEG);
    }

    public void setHeadBones(int amount) {
        this.setHeadBones(amount, true);
    }

    public void setBodyBones(int amount) {
        int pre = this.entityData.get(BODY_BONES);
        this.entityData.set(BODY_BONES, Math.min(amount, 20));
        if (pre > 15 && amount <= 15)
            this.level().broadcastEntityEvent(this, BACK_RIBS);
        if (pre > 10 && amount <= 10)
            this.level().broadcastEntityEvent(this, BACK);
        if (pre > 5 && amount <= 5)
            this.level().broadcastEntityEvent(this, FRONT_RIBS);
        if (amount <= 0)
            this.level().broadcastEntityEvent(this, FRONT);
    }

    private void setHeadBones(int amount, boolean withParticle) {
        int pre = this.entityData.get(HEAD_BONES);
        this.entityData.set(HEAD_BONES, Math.min(amount, 20));
        if (withParticle) {
            if (pre > 10 && amount <= 10)
                this.level().broadcastEntityEvent(this, HEAD_DROP);
            if (amount <= 0)
                this.level().broadcastEntityEvent(this, NECK_DROP);
        }
    }

    public void restoreDragon() {
        this.entityData.set(HEAD_BONES, 20);
        this.entityData.set(TAIL_BONES, 20);
        this.entityData.set(LEFT_LEG_BONES, 20);
        this.entityData.set(RIGHT_LEG_BONES, 20);
        this.entityData.set(BODY_BONES, 20);
    }

    public boolean checkIgnoreHurtOverlay() {
        if (this.ignoreHurt) {
            this.ignoreHurt = false;
            return true;
        }
        return false;
    }

    public boolean hasBones() {
        return this.remainingHeadBones() > 0 || this.remainingTailBones() > 0
                || this.remainingLeftLegBones() > 0 || this.remainingRightLegBones() > 0
                || this.remainingBodyBones() > 0;
    }

    public int remainingHeadBones() {
        return this.entityData.get(HEAD_BONES);
    }

    public int remainingTailBones() {
        return this.entityData.get(TAIL_BONES);
    }

    public int remainingLeftLegBones() {
        return this.entityData.get(LEFT_LEG_BONES);
    }

    public int remainingRightLegBones() {
        return this.entityData.get(RIGHT_LEG_BONES);
    }

    public int remainingBodyBones() {
        return this.entityData.get(BODY_BONES);
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch (id) {
            case HEAD_DROP ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.HEAD, this.getXRot(), this.yHeadRot, 1, 0),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case HEAD_THROW -> {
                Vec3 look = Vec3.directionFromRotation(0, this.yBodyRot);
                this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.HEAD, this.getXRot(), this.yHeadRot, -2, 0, 40, false),
                        this.getX(), this.getY(), this.getZ(), look.x, look.y, look.z);
            }
            case NECK_DROP ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.NECK, this.getXRot(), this.yHeadRot, 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case NECK_THROW -> {
                Vec3 look = Vec3.directionFromRotation(0, this.yBodyRot);
                this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.NECK, this.getXRot(), this.yHeadRot, -2, 0, 40, false),
                        this.getX(), this.getY(), this.getZ(), look.x, look.y, look.z);
            }
            case FRONT ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.FRONT, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case FRONT_RIBS ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.FRONT_RIBS, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case LEFT_LEG ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.LEFT_LEG, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case RIGHT_LEG ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.RIGHT_LEG, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case BACK ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.BACK, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case BACK_RIBS ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.BACK_RIBS, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case TAIL ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.TAIL, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case TAIL_BASE ->
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(SkelefangParticleData.SkelefangBoneType.TAIL_BASE, this.getXRot(), this.yBodyRot, this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getX(), this.getY(), this.getZ(), 0, 0, 0);
            case HIT -> {
                int amount = this.random.nextInt(6) + 12;
                for (int i = 0; i < amount; i++) {
                    SkelefangParticleData.SkelefangBoneType type = this.random.nextFloat() < 0.4 ? SkelefangParticleData.SkelefangBoneType.GENERIC : SkelefangParticleData.SkelefangBoneType.GENERIC2;
                    this.level().addAlwaysVisibleParticle(new SkelefangParticleData(type, this.getXRot() + this.random.nextInt(40) - 20, this.yHeadRot + this.random.nextInt(360), this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
                            this.getRandomX(1.3), this.getY(0.5) + this.getBbHeight() * 0.5, this.getRandomZ(1.3), this.random.nextGaussian() * 0.11, this.random.nextGaussian() * 0.11, this.random.nextGaussian() * 0.11);
                }
            }
            case SHATTER -> {
                int amount = this.random.nextInt(15) + 35;
                if (this.hasBones()) {
                    for (int i = 0; i < amount; i++) {
                        SkelefangParticleData.SkelefangBoneType type = this.random.nextFloat() < 0.4 ? SkelefangParticleData.SkelefangBoneType.GENERIC : SkelefangParticleData.SkelefangBoneType.GENERIC2;
                        this.level().addAlwaysVisibleParticle(new SkelefangParticleData(type, this.getXRot() + this.random.nextInt(40) - 20, this.yHeadRot + this.random.nextInt(360), this.random.nextInt(2) - 1, this.random.nextInt(2) - 1),
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
                locations.addAll(MathUtils.rotatedVecs(MathUtils.NORMAL_X.scale(this.getBbWidth() + 2).toVector3f(), MathUtils.NORMAL_Z.toVector3f(), -180, 180, 10));
                locations.addAll(MathUtils.rotatedVecs(MathUtils.NORMAL_X.scale(this.getBbWidth() + 2).toVector3f(), MathUtils.NORMAL_Y.toVector3f(), -180, 180, 10));
                locations.addAll(MathUtils.rotatedVecs(MathUtils.NORMAL_Y.scale(this.getBbWidth() + 2).toVector3f(), MathUtils.NORMAL_X.toVector3f(), -180, 180, 10));
                for (Vector3f vec : locations) {
                    Vec3 pos = center.add(vec.x(), vec.y(), vec.z());
                    Vec3 dir = new Vec3(vec.x(), vec.y(), vec.z()).normalize().scale(speed);
                    this.level().addAlwaysVisibleParticle(new DurationalParticleData(217 / 255f, 248 / 255f, 252 / 255f, 0.4f, 2.3f, 40, this.getId()),
                            pos.x(), pos.y(), pos.z(), -dir.x(), -dir.y(), -dir.z());
                }
            }
            default -> super.handleEntityEvent(id);
        }
        if (this.hurtDuration == 10 && this.checkIgnoreHurtOverlay()) {
            this.hurtDuration = 0;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            --this.hurtResist;
            this.updateParts();
            if (!this.isDeadOrDying() && !this.hasBones() && !this.getAnimationHandler().isCurrent(BEAM))
                this.getAnimationHandler().setAnimation(BEAM);
            if (this.isAlive() && !this.getAnimationHandler().hasAnimation() && !this.isTamed() && this.getHealth() / this.getMaxHealth() < 0.5 && !this.isEnraged())
                this.setEnraged(true, false);
        }
    }

    private void updateParts() {
        if (this.remainingTailBones() > 0) {
            this.back.tick();
        } else {
            this.back.removeEntity();
        }
        if (this.remainingHeadBones() > 10) {
            this.head.tick();
        } else {
            this.head.removeEntity();
        }
        if (this.remainingLeftLegBones() > 0) {
            this.leftLeg.tick();
        } else {
            this.leftLeg.removeEntity();
        }
        if (this.remainingRightLegBones() > 0) {
            this.rightLeg.tick();
        } else {
            this.rightLeg.removeEntity();
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !(this.getAnimationHandler().isCurrent(ROAR)) && super.hurt(source, amount);
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE)) {
            return null;
        }
        return super.directionToLookAt();
    }

    @Override
    public int animationCooldown(String anim) {
        int diffAdd = this.difficultyCooldown();
        return (this.isEnraged() ? 27 + this.getRandom().nextInt(20) : 35 + this.getRandom().nextInt(25)) + diffAdd;
    }

    @Override
    public void handleAttack(AnimationState anim) {
        this.getNavigation().stop();
        BiConsumer<AnimationState, Skelefang> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void mobAttack(AnimationState anim, LivingEntity target, Consumer<LivingEntity> cons) {
        if (anim.is(CHARGE)) {
            double width = this.getBbWidth();
            double speed = Math.max(width, this.getDeltaMovement().length() - width);
            OrientedBoundingBox obb = new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(0.8, 0, 0.2)
                    .expandTowards(0, 0, speed), this.getYRot(), 0, this.position());
            this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(),
                    entity -> this.hitPred.test(entity) && obb.intersects(entity.getBoundingBox())).forEach(cons);
            if (!this.level().isClientSide)
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
            return;
        }
        List<OrientedBoundingBox> obbs = new ArrayList<>();
        if (anim.is(TAIL_SLAP)) {
            double range = this.getBbWidth() + 5;
            obbs.add(new OrientedBoundingBox(new AABB(-range * 0.75, -0.02, 0, range * 0.75, 1.8 + 0.02, range),
                    this.getYRot(), 0, this.position()));
        }
        if (anim.is(TAIL_SLAM)) {
            float angle = this.yHeadRot - 5;
            if (this.getControllingPassenger() instanceof Player player)
                angle = player.yHeadRot;
            if (anim.isAt("attack_3"))
                angle += 20;
            else if (!anim.isAt("attack_2"))
                angle -= 35;
            obbs.add(new OrientedBoundingBox(new AABB(-1.25, -0.02, -0, 1.25, 2.1 + 0.02, 7),
                    angle, 0, this.position()));
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
            double attackSize = 1.65;
            if (this.remainingLeftLegBones() > 0) {
                Vec3 rightPos = this.position().add(dir).add(side.scale(1.3));
                obbs.add(new OrientedBoundingBox(new AABB(-attackSize, -0.02, -attackSize, attackSize, 1.8 + 0.02, attackSize), this.getYRot(), 0, rightPos));
            }
            if (this.remainingRightLegBones() > 0) {
                Vec3 leftPos = this.position().add(dir).add(side.scale(-1.3));
                obbs.add(new OrientedBoundingBox(new AABB(-attackSize, -0.02, -attackSize, attackSize, 1.8 + 0.02, attackSize), this.getYRot(), 0, leftPos));
            }
        }
        Set<LivingEntity> targets = new HashSet<>();
        for (OrientedBoundingBox obb : obbs) {
            targets.addAll(this.level().getEntitiesOfClass(LivingEntity.class, obb.getEncompassingBox(), entity -> this.hitPred.test(entity) && obb.intersects(entity.getBoundingBox())));
            if (!this.level().isClientSide)
                S2CAttackDebug.sendDebugPacket(obb, S2CAttackDebug.EnumAABBType.ATTACK, this);
        }
        targets.forEach(cons);
    }

    @Override
    public AnimationHandler<Skelefang> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE);
            else
                this.getAnimationHandler().setAnimation(TAIL_SLAP);
            this.commanded = true;
        }
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        if (this.hasBones())
            return new Vec3(0, 43.5 / 16d, 7 / 16d).scale(this.getScale())
                    .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
        return super.getPassengerAttachmentPoint(entity, dimensions, partialTick);
    }

    @Override
    public void playAngrySound() {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockIn) {
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEATH;
    }

    @Override
    public String getSleepAnimation() {
        return DEATH;
    }
}
