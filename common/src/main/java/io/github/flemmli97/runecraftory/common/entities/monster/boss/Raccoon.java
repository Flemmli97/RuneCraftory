package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.misc.GroundShakeParticleSpawner;
import io.github.flemmli97.runecraftory.common.entities.utils.MoveType;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.DynamicDamage;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.DummyBehaviour;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.entity.data.SyncableDatas;
import io.github.flemmli97.tenshilib.common.entity.data.SyncedDataContainer;
import io.github.flemmli97.tenshilib.common.utils.TypedResource;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityAttachment;
import net.minecraft.world.entity.EntityAttachments;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.SequentialBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.Optional;
import java.util.function.BiConsumer;

public class Raccoon extends BossMonster {

    private static final EntityDataAccessor<Integer> CLONE_INDEX = SynchedEntityData.defineId(Raccoon.class, EntityDataSerializers.INT);
    protected static final EntityDataAccessor<Boolean> BERSERK = SynchedEntityData.defineId(Raccoon.class, EntityDataSerializers.BOOLEAN);

    public static final Vec3[] CLONE_POS = new Vec3[]{
            new Vec3(-6, 0, 0),
            new Vec3(0, 0, -6),
            new Vec3(6, 0, 0),
            new Vec3(0, 0, 6)
    };
    public static final TypedResource<Vec3> CLONE_POSITION = new TypedResource<>(RuneCraftory.modRes("clone_position"));

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DOUBLE_PUNCH = BUILDER.add("double_punch", AnimationsBuilder.definition(0.88).marker("attack", 0.4, 0.68));
    public static final String INTERACT = BUILDER.add("interact", DOUBLE_PUNCH);
    public static final String PUNCH = BUILDER.add("punch", AnimationsBuilder.definition(0.96).marker("attack", 0.6));
    public static final String INTERACT_BERSERK = BUILDER.add("interact_berserk", PUNCH);
    public static final String JUMP = BUILDER.add("jump", AnimationsBuilder.definition(1.12)
            .marker("jump", 0.24).infinite());
    public static final String LAND = BUILDER.add("land", AnimationsBuilder.definition(0.36).marker("attack", 0.12));
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(1.4)
            .marker("attack_1", 0.6).marker("attack_2", 1.16));
    public static final String LEAF_SHOOT = BUILDER.add("shoot", AnimationsBuilder.definition(1).marker("attack", 0.56));
    public static final String LEAF_BOOMERANG = BUILDER.add("spinning_shoot", LEAF_SHOOT);
    public static final String LEAF_SHOT_CLONE = BUILDER.add("leaf_clone", AnimationsBuilder.definition(1)
            .animationId("shoot").marker("attack", 0.56, 0.76));
    public static final String BARRAGE = BUILDER.add("punch_barrage", AnimationsBuilder.definition(3.4)
            .marker("attack", 0.6, 0.92, 1.36).marker("vulnerable_start", 1.64).marker("vulnerable_end", 3.08));
    public static final String ROAR = BUILDER.add("roar", AnimationsBuilder.definition(1.28).marker("roar", 0.16));
    public static final String ANGRY = BUILDER.add("angry", ROAR);
    public static final String CLONE = BUILDER.add("clone", ROAR);
    public static final String TRANSFORM = BUILDER.add("transform", AnimationsBuilder.definition(1.5));
    public static final String UNTRANSFORM = BUILDER.add("untransform", AnimationsBuilder.definition(2.2)
            .marker("knockback_start", 1).marker("knockback_end", 1.5));
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimationState, Raccoon>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(DOUBLE_PUNCH, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
            }
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(PUNCH, (anim, entity) -> {
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(BARRAGE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                LivingEntity target = entity.getTarget();
                Vec3 dir;
                if (target != null) {
                    Vec3 targetPos = target.position();
                    dir = new Vec3(targetPos.x - entity.getX(), 0.0, targetPos.z - entity.getZ()).normalize();
                    entity.setTargetPosition(target);
                } else
                    dir = EntityUtils.horizontalLookAngle(entity);
                entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(0.6)));
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(JUMP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("jump")) {
                Vec3 dir;
                if (entity.getTarget() != null) {
                    dir = entity.getTarget().position().subtract(entity.position());
                    dir = new Vec3(dir.x(), 0, dir.z());
                    if (dir.lengthSqr() > 20 * 20)
                        dir = dir.normalize().scale(20 * 0.14);
                    else
                        dir = dir.scale(0.14);
                } else {
                    dir = EntityUtils.horizontalLookAngle(entity).scale(0.75);
                }
                entity.setDeltaMovement(dir.x(), 2.2, dir.z());
            }
            if (anim.isPast("jump")) {
                entity.fallDistance = 0;
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.08, 0));
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, entity.position().add(entity.getDeltaMovement().x(), 0, entity.getDeltaMovement().z()));
                if (entity.getDeltaMovement().y < -1.1) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, -1.1, entity.getDeltaMovement().z);
                }
                if (anim.done(0)) {
                    if (entity.onGround()) {
                        entity.getAnimationHandler().setAnimation(LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!entity.getBlockStateOn().is(Blocks.AIR) || !entity.getBlockStateOn().is(Blocks.AIR))) {
                    entity.getAnimationHandler().setAnimation(LAND);
                }
            }
        });
        b.put(LAND, (anim, entity) -> {
            if (anim.isAt("attack")) {
                DynamicDamage.Builder source = new DynamicDamage.Builder(entity).noKnockback().element(ItemElement.EARTH).hurtResistant(5)
                        .withChangedAttribute(RuneCraftoryAttributes.STUN.asHolder(), 80);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 8, 3);
                entity.level().addFreshEntity(new GroundShakeParticleSpawner(entity.level(), entity, 360, entity.getBbWidth() * 1.8));
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), entity.getSoundSource(), 1.0f, 0.9f);
            }
        });
        b.put(STOMP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack_1") || anim.isAt("attack_2")) {
                DynamicDamage.Builder source = new DynamicDamage.Builder(entity).noKnockback().element(ItemElement.EARTH).hurtResistant(5)
                        .withChangedAttribute(RuneCraftoryAttributes.STUN.asHolder(), 50);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 8, 3);
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE.value(), entity.getSoundSource(), 1.0f, 0.9f);
            }
        });
        b.put(LEAF_SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    RuneCraftorySpells.SMALL_LEAF_SPELL_X7.get().use(entity);
                else {
                    if (entity.isTamed() || entity.random.nextFloat() < 0.6)
                        RuneCraftorySpells.SMALL_LEAF_SPELL_X3.get().use(entity);
                    else
                        RuneCraftorySpells.SMALL_LEAF_SPELL_X5.get().use(entity);
                }
            }
        });
        b.put(LEAF_SHOT_CLONE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    RuneCraftorySpells.SMALL_LEAF_SPELL_X7.get().use(entity);
                else {
                    if (entity.random.nextFloat() < 0.6)
                        RuneCraftorySpells.SMALL_LEAF_SPELL_X3.get().use(entity);
                    else
                        RuneCraftorySpells.SMALL_LEAF_SPELL_X5.get().use(entity);
                }
            }
        });
        b.put(LEAF_BOOMERANG, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    RuneCraftorySpells.BIG_LEAF_SPELL_DOUBLE.get().use(entity);
                else
                    RuneCraftorySpells.BIG_LEAF_SPELL.get().use(entity);
            }
        });
        b.put(ROAR, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("roar"))
                entity.playAngrySound();
        });
        b.put(CLONE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt(0.1)) {
                entity.playAngrySound();
                Vec3 center = entity.getTarget() == null ? entity.position() : (entity.distanceToSqr(entity.getTarget()) < 144 ? entity.getTarget().position()
                        : entity.getTarget().position().subtract(entity.position()).normalize().scale(12).add(entity.position()));
                entity.setClonePos(center);
                int id = entity.random.nextInt(CLONE_POS.length);
                Vec3 pos = CLONE_POS[id];
                entity.entityData.set(CLONE_INDEX, id);
                entity.teleportTo(center.x() + pos.x, center.y() + pos.y, center.z() + pos.z);
            }
            entity.cloneCenter().ifPresent(pos -> entity.lookAt(EntityAnchorArgument.Anchor.FEET, pos));
        });
        b.put(UNTRANSFORM, (anim, entity) -> {
            if (entity.onGround() && anim.isPast("knockback_start") && !anim.isPast("knockback_end")) {
                entity.push(0, 0.4, 0);
            }
        });
    });

    private final AnimationHandler<Raccoon> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (!this.level().isClientSide) {
                    this.setClonePos(null);
                    if (anim == null && this.getAnimationHandler().isCurrent(CLONE)) {
                        this.getAnimationHandler().setAnimation(this.getRandom().nextBoolean() ? LEAF_SHOT_CLONE : LEAF_BOOMERANG);
                        return true;
                    }
                }
                return false;
            });

    private int hit;
    private int hitCountdown = -1;

    private final EntityDimensions berserkDimensions = EntityDimensions.scalable(1.4f, 2.5f)
            .withAttachments(EntityAttachments.builder().attach(EntityAttachment.PASSENGER, new Vec3(0, 30 / 16d, -7 / 16d)));

    public Raccoon(EntityType<? extends Raccoon> type, Level level) {
        super(type, level);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(RuneCraftoryEntities.RACCOON.getID(), this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(RuneCraftorySounds.RACCOON_FIGHT.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BERSERK, false);
        builder.define(CLONE_INDEX, 0);
    }

    @Override
    protected void definedAdditinoalSyncedData(SyncedDataContainer.Builder<BaseMonster> builder) {
        super.definedAdditinoalSyncedData(builder);
        builder.define(CLONE_POSITION, SyncableDatas.VEC_3, null);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (BERSERK.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.24);
        super.applyAttributes();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<Raccoon>create()
                .start(MonsterBehaviourUtils.checkedAttack(DOUBLE_PUNCH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.isBerserk())
                .prepare(new SetWalkTargetAwayFromTarget<Raccoon>().speedMod((e, t) -> 1.2f))
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(20)
                .start(MonsterBehaviourUtils.checkedAttack(DOUBLE_PUNCH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> !m.isBerserk())
                .prepare(new SetWalkTargetToAttackTarget<Raccoon>().speedMod((e, t) -> 1.2f))
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(17)
                .start(MonsterBehaviourUtils.checkedAttack(PUNCH)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(raccoon -> raccoon.isBerserk() && MonsterBehaviourUtils.ifCloserThan(6).test(raccoon))
                .prepare(new SetWalkTargetToAttackTarget<>())
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(20)
                .start(MonsterBehaviourUtils.checkedAttack(JUMP)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Raccoon::isBerserk)
                .prepare(new SetWalkTargetToAttackTarget<Raccoon>().closeEnoughDist(MonsterBehaviourUtils.closeEnough(16))).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(18)
                .start(MonsterBehaviourUtils.checkedAttack(STOMP)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(raccoon -> raccoon.isBerserk() && MonsterBehaviourUtils.ifCloserThan(8).test(raccoon))
                .prepare(new SetWalkTargetToAttackTarget<>())
                .prepareOptional(MonsterBehaviourUtils.fastMovement())
                .end(18)
                .start(MonsterBehaviourUtils.checkedAttack(LEAF_SHOOT)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Raccoon::isBerserk)
                .prepare(new SetWalkTargetAwayFromTarget<Raccoon>().speedMod(1.1f).minDist(4).radius(9)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(17)
                .start(MonsterBehaviourUtils.checkedAttack(LEAF_BOOMERANG)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Raccoon::isBerserk)
                .prepare(new SetWalkTargetAwayFromTarget<Raccoon>().speedMod(1.1f).minDist(4).radius(6)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(18)
                .start(MonsterBehaviourUtils.checkedAttack(ROAR)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Raccoon::isBerserk)
                .end(3)
                .start(MonsterBehaviourUtils.checkedAttack(BARRAGE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(Raccoon::isBerserk)
                .prepare(new SetWalkTargetToAttackTarget<Raccoon>()
                        .closeEnoughDist(MonsterBehaviourUtils.closeEnough(9))).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(1)
                .start(MonsterBehaviourUtils.checkedAttack(CLONE)).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(m -> m.isEnraged() && m.isBerserk())
                .end(7)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return new OneRandomBehaviour<>(
                DummyBehaviour.opt(new SequentialBehaviour<Raccoon>(new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo()))
                        .startCondition(Raccoon::isBerserk),
                DummyBehaviour.opt(new SequentialBehaviour<Raccoon>(new SetWalkTargetAwayFromTarget<>()
                        .minDist(4).radius(6).speedMod(1.1f), MonsterBehaviourUtils.moveTo())).startCondition(m -> !m.isBerserk())
        );
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (!this.level().isClientSide) {
            --this.hitCountdown;
            if (this.isAlive() && !this.isBerserk() && (this.hitCountdown == 0 || this.hit >= 5)) {
                this.setBerserk(true, false);
                this.hit = 0;
            }
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    public boolean isBerserk() {
        return this.entityData.get(BERSERK);
    }

    public void setBerserk(boolean flag, boolean load) {
        this.entityData.set(BERSERK, flag);
        this.refreshDimensions();
        if (!load) {
            if (flag)
                this.getAnimationHandler().setAnimation(TRANSFORM);
            else
                this.getAnimationHandler().setAnimation(UNTRANSFORM);
        }
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.isBerserk())
            return this.berserkDimensions.scale(this.getAgeScale());
        return super.getDefaultDimensions(pose);
    }

    @Override
    public float getScale() {
        if (this.isBerserk())
            return super.getScale() * 1.4f;
        return super.getScale();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Berserk", this.isBerserk());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBerserk(compound.getBoolean("Berserk"), true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAnimationHandler().isCurrent(JUMP, LAND, DEFEAT, TRANSFORM, UNTRANSFORM, ANGRY))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource source, float damageAmount) {
        super.actuallyHurt(source, damageAmount);
        if (!this.isBerserk()) {
            this.hit++;
            this.hitCountdown = 30;
        } else if (this.getAnimationHandler().isCurrent(BARRAGE)) {
            AnimationState anim = this.getAnimationHandler().getAnimation();
            if (anim.isPast("vulnerable_start") && !anim.isPast("vulnerable_end")) {
                this.setBerserk(false, false);
                this.getAnimationHandler().setAnimation(UNTRANSFORM);
                this.push(0, 0.6, 0);
            }
        }
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance - 5, damageMultiplier);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, ROAR, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public void push(Entity entityIn) {
        if (this.getAnimationHandler().isCurrent(ROAR, CLONE, TRANSFORM))
            return;
        super.push(entityIn);
    }

    @Override
    protected void pushEntities() {
        if (!this.getAnimationHandler().isCurrent(CLONE))
            super.pushEntities();
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(CLONE, TRANSFORM, UNTRANSFORM, ANGRY, ROAR, DEFEAT);
    }

    @Override
    public MoveType getMoveFlag() {
        if (this.getAnimationHandler().isCurrent(TRANSFORM, UNTRANSFORM))
            return MoveType.NONE;
        return super.getMoveFlag();
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimationState anim, Vec3 target, double grow) {
        if (anim.is(JUMP, LAND)) {
            return new OrientedBoundingBox(this.attackBB(anim), 0, 0, this.position());
        }
        if (anim.is(STOMP)) {
            double reach = this.getBbWidth() * 0.55;
            Vec3 dir;
            float offset = anim.isAt("attack_1") ? -90 : 90;
            if (this.getControllingPassenger() instanceof Player player)
                dir = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + offset);
            else
                dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot() + offset);
            Vec3 attackPos = this.position().add(dir.scale(reach));
            return new OrientedBoundingBox(this.attackBB(anim), this.getYRot(), 0, attackPos);
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        if (anim.is(JUMP, LAND)) {
            double attackSize = this.getBbWidth() * 1.6;
            return new AABB(-attackSize, -0.5, -attackSize, attackSize, 2, attackSize);
        }
        if (anim.is(STOMP)) {
            return new AABB(-1.9, -0.5, -2.3, 1.9, 2, 2.3);
        }
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 1.5;
        if (anim.is(DOUBLE_PUNCH)) {
            width = this.getBbWidth() * 1.5;
            length = this.getBbWidth() * 1.7;
        }
        if (anim.is(PUNCH)) {
            width = this.getBbWidth() * 1.5;
            length = this.getBbWidth() * 1.6;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        this.getNavigation().stop();
        BiConsumer<AnimationState, Raccoon> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AnimationHandler<Raccoon> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.SMALL_LEAF_SPELL_X3.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(LEAF_SHOOT);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(JUMP);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_PUNCH);
        }
    }

    public Optional<Vec3> cloneCenter() {
        return Optional.ofNullable(this.getDataContainer().get(CLONE_POSITION));
    }

    public void setClonePos(Vec3 pos) {
        this.getDataContainer().set(CLONE_POSITION, pos);
    }

    public int cloneIndex() {
        return this.entityData.get(CLONE_INDEX);
    }

    @Override
    public String getInteractAnimation() {
        return this.isBerserk() ? INTERACT_BERSERK : INTERACT;
    }

    @Override
    public String getDeathAnimation() {
        return DEFEAT;
    }
}
