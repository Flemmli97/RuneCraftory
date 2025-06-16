package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.misc.GroundShakeParticleSpawner;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
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

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class EntityRaccoon extends BossMonster {

    public static final Vec3[] CLONE_POS = new Vec3[]{
            new Vec3(-4, 0, 0),
            new Vec3(0, 0, -4),
            new Vec3(4, 0, 0),
            new Vec3(0, 0, 4)
    };
    private static final EntityDataAccessor<Boolean> BERSERK = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<Vec3>> CLONE_CENTER = SynchedEntityData.defineId(EntityRaccoon.class, CustomDataSerializers.OPTIONAL_VEC);
    private static final EntityDataAccessor<Integer> CLONE_INDEX = SynchedEntityData.defineId(EntityRaccoon.class, EntityDataSerializers.INT);

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DOUBLE_PUNCH = BUILDER.add("double_punch", AnimationsBuilder.definition(0.88).marker("attack", 0.4, 0.68));
    public static final String PUNCH = BUILDER.add("punch", AnimationsBuilder.definition(0.92).marker("attack", 0.56));
    public static final String JUMP = BUILDER.add("jump", AnimationsBuilder.definition(1.08)
            .marker("jump", 0.2).infinite());
    public static final String LAND = BUILDER.add("land", AnimationsBuilder.definition(0.4).marker("attack", 0.24));
    public static final String STOMP = BUILDER.add("stomp", AnimationsBuilder.definition(1.36).marker("attack", 0.56, 1.12));
    public static final String LEAF_SHOOT = BUILDER.add("shoot", AnimationsBuilder.definition(0.88).marker("attack", 0.44));
    public static final String LEAF_BOOMERANG = BUILDER.add("spinning_shoot", LEAF_SHOOT);
    public static final String LEAF_SHOT_CLONE = BUILDER.add("leaf_clone", AnimationsBuilder.definition(0.88)
            .animationId("shoot").marker("attack", 0.44, 0.64));
    public static final String BARRAGE = BUILDER.add("punch_barrage", AnimationsBuilder.definition(3.32)
            .marker("attack", 0.44, 0.84, 1.28).marker("vulnerable_start", 1.52).marker("vulnerable_end", 3.04));
    public static final String ROAR = BUILDER.add("roar", AnimationsBuilder.definition(1.24).marker("roar", 0.12));
    public static final String ANGRY = BUILDER.add("angry", ROAR);
    public static final String CLONE = BUILDER.add("clone", ROAR);
    public static final String DEFEAT = BUILDER.add("defeat", AnimationsBuilder.definition(10).infinite());
    public static final String TRANSFORM = BUILDER.add("transform", AnimationsBuilder.definition(1.5));
    public static final String UNTRANSFORM = BUILDER.add("untransform", AnimationsBuilder.definition(2.2)
            .marker("knockback_start", 1).marker("knockback_end", 1.5));
    public static final String INTERACT = BUILDER.add("interact", DOUBLE_PUNCH);
    public static final String INTERACT_BERSERK = BUILDER.add("interact_berserk", PUNCH);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityRaccoon>> ATTACK_HANDLER = createAnimationHandler(b -> {
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
                    dir = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z()).normalize();
                entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(0.6)));
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(JUMP, (anim, entity) -> {
            entity.getNavigation().stop();
            double length = anim.getLength() - 3;
            if (entity.jumpDir == null) {
                Vec3 dir = entity.getTarget() != null ? entity.getTarget().position().subtract(entity.position()) : entity.getLookAngle();
                dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(8);
                entity.jumpDir = dir.multiply(1 / length, 1, 1 / length);
            }
            if (anim.isAt("jump"))
                entity.setDeltaMovement(entity.jumpDir.x, 2, entity.jumpDir.z);
            if (anim.isPast("jump")) {
                entity.fallDistance = 0;
                entity.setDeltaMovement(entity.getDeltaMovement().add(0, -0.08, 0));
                if (entity.getDeltaMovement().y < -1.1) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x, -1.1, entity.getDeltaMovement().z);
                }
                if (anim.done(0)) {
                    if (entity.isOnGround()) {
                        entity.getAnimationHandler().setAnimation(LAND);
                    }
                }
                // Stuck check. Or e.g. if in water
                if (anim.isPast(6.0) && (!entity.getFeetBlockState().is(Blocks.AIR) || !entity.getBlockStateOn().is(Blocks.AIR))) {
                    entity.getAnimationHandler().setAnimation(LAND);
                }
            }
        });
        b.put(LAND, (anim, entity) -> {
            if (anim.isAt("attack")) {
                CustomDamage.Builder source = new CustomDamage.Builder(entity).noKnockback().element(EnumElement.EARTH).hurtResistant(5)
                        .withChangedAttribute(ModAttributes.STUN.get(), 80);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 8, 3);
                entity.level().addFreshEntity(new GroundShakeParticleSpawner(entity.level(), entity, 360, entity.getBbWidth() * 1.8));
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, entity.getSoundSource(), 1.0f, 0.9f);
            }
        });
        b.put(STOMP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                CustomDamage.Builder source = new CustomDamage.Builder(entity).noKnockback().element(EnumElement.EARTH).hurtResistant(5)
                        .withChangedAttribute(ModAttributes.STUN.get(), 50);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                S2CScreenShake.sendAround(entity, 24, 8, 3);
                entity.level().playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, entity.getSoundSource(), 1.0f, 0.9f);
            }
        });
        b.put(LEAF_SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    ModSpells.SMALL_LEAF_SPELL_X7.get().use(entity);
                else {
                    if (entity.isTamed() || entity.random.nextFloat() < 0.6)
                        ModSpells.SMALL_LEAF_SPELL_X3.get().use(entity);
                    else
                        ModSpells.SMALL_LEAF_SPELL_X5.get().use(entity);
                }
            }
        });
        b.put(LEAF_SHOT_CLONE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    ModSpells.SMALL_LEAF_SPELL_X7.get().use(entity);
                else {
                    if (entity.random.nextFloat() < 0.6)
                        ModSpells.SMALL_LEAF_SPELL_X3.get().use(entity);
                    else
                        ModSpells.SMALL_LEAF_SPELL_X5.get().use(entity);
                }
            }
        });
        b.put(LEAF_BOOMERANG, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("attack")) {
                if (entity.isEnraged())
                    ModSpells.BIG_LEAF_SPELL_DOUBLE.get().use(entity);
                else
                    ModSpells.BIG_LEAF_SPELL.get().use(entity);
            }
        });
        b.put(ROAR, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt("roar"))
                entity.playAngrySound();
        });
        b.put(CLONE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAt(0.05)) {
                entity.playAngrySound();
                Vec3 center = entity.getTarget() == null ? entity.position() : (entity.distanceToSqr(entity.getTarget()) < 144 ? entity.getTarget().position()
                        : entity.getTarget().position().subtract(entity.position()).normalize().scale(12).add(entity.position()));
                entity.entityData.set(CLONE_CENTER, Optional.of(center));
                int id = entity.random.nextInt(CLONE_POS.length);
                Vec3 pos = CLONE_POS[id];
                entity.entityData.set(CLONE_INDEX, id);
                entity.teleportTo(center.x() + pos.x, center.y() + pos.y, center.z() + pos.z);
            }
            entity.entityData.get(CLONE_CENTER).ifPresent(pos -> entity.lookAt(EntityAnchorArgument.Anchor.FEET, pos));
        });
        b.put(UNTRANSFORM, (anim, entity) -> {
            if (entity.isOnGround() && anim.isPast("knockback_start") && !anim.isPast("knockback_end")) {
                entity.push(0, 0.4, 0);
            }
        });
    });
    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityRaccoon>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(DOUBLE_PUNCH)
                    .cooldown(e -> e.animationCooldown(DOUBLE_PUNCH))
                    .withCondition(((goal, target, previous) -> !goal.attacker.isBerserk()))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4, 1.2, 5), e -> 20 + e.getRandom().nextInt(10))), 20),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(DOUBLE_PUNCH)
                    .cooldown(e -> e.animationCooldown(DOUBLE_PUNCH))
                    .withCondition(((goal, target, previous) -> !goal.attacker.isBerserk()))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 17),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(PUNCH)
                    .cooldown(e -> e.animationCooldown(PUNCH))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !PUNCH.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 20),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(JUMP)
                    .cooldown(e -> e.animationCooldown(JUMP))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !JUMP.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 6), e -> 25 + e.getRandom().nextInt(10))), 18),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(STOMP)
                    .cooldown(e -> e.animationCooldown(STOMP))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !STOMP.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 18),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(LEAF_SHOOT)
                    .cooldown(e -> e.animationCooldown(LEAF_SHOOT))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !LEAF_SHOOT.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 5), e -> 25 + e.getRandom().nextInt(10))), 17),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(LEAF_BOOMERANG)
                    .cooldown(e -> e.animationCooldown(LEAF_BOOMERANG))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !LEAF_BOOMERANG.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 5), e -> 25 + e.getRandom().nextInt(10))), 18),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(ROAR)
                    .cooldown(e -> e.animationCooldown(ROAR))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !ROAR.getID().equals(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 5),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(BARRAGE)
                    .cooldown(e -> e.animationCooldown(BARRAGE))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !BARRAGE.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 1),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(CLONE)
                    .cooldown(e -> e.animationCooldown(CLONE))
                    .withCondition(((goal, target, previous) -> goal.attacker.isEnraged() && !CLONE.getID().equals(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 15)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityRaccoon>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntityRaccoon>(() -> new MoveToTargetRunner<>(1, 0.5))
                    .withCondition(((goal, target) -> goal.attacker.isBerserk())), 10),
            WeightedEntry.wrap(new IdleAction<EntityRaccoon>(() -> new MoveAwayRunner<>(1, 1, 5))
                    .withCondition(((goal, target) -> !goal.attacker.isBerserk())), 11)
    );

    public final AnimatedAttackGoal<EntityRaccoon> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private boolean clone;
    private Vec3 jumpDir;

    private int hit;
    private int hitCountdown = -1;

    private final AnimationHandler<EntityRaccoon> animationHandler = new AnimationHandler<>(this, ANIMS)
            .withChangeListener(anim -> {
                if (CLONE.is(anim)) {
                    this.clone = true;
                } else if (anim != null) {
                    this.clone = false;
                    this.entityData.set(CLONE_CENTER, Optional.empty());
                }
                this.jumpDir = null;
                if (anim == null && this.clone) {
                    this.getAnimationHandler().setAnimation(this.getRandom().nextBoolean() ? LEAF_SHOT_CLONE : LEAF_BOOMERANG);
                    return true;
                }
                return false;
            });

    public EntityRaccoon(EntityType<? extends EntityRaccoon> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.GREEN, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.RACCOON_FIGHT.get());
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(BERSERK, false);
        builder.define(CLONE_CENTER, Optional.empty());
        builder.define(CLONE_INDEX, 0);
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

    public Optional<Vec3> cloneCenter() {
        return this.entityData.get(CLONE_CENTER);
    }

    public int cloneIndex() {
        return this.entityData.get(CLONE_INDEX);
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.24);
        super.applyAttributes();
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
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (BERSERK.equals(key)) {
            this.refreshDimensions();
        }
        super.onSyncedDataUpdated(key);
    }

    public void setBerserk(boolean flag, boolean load) {
        this.entityData.set(BERSERK, flag);
        this.refreshDimensions();
        if (!load) {
            if (flag)
                this.getAnimationHandler().setAnimation(TRANSFORM);
            else
                this.getAnimationHandler().setAnimation(UNTRANSFORM);
            this.attack.stop();
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        if (this.isBerserk())
            return pose == Pose.SLEEPING ? SLEEPING_DIMENSIONS : EntityDimensions.fixed(1.65f, 3.35f).scale(this.getScale());
        return super.getDimensions(pose);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(CLONE, TRANSFORM, UNTRANSFORM, ANGRY, ROAR, DEFEAT);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setBerserk(compound.getBoolean("Berserk"), true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Berserk", this.isBerserk());
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, ROAR, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        this.getNavigation().stop();
        BiConsumer<AnimatedAction, EntityRaccoon> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(JUMP, LAND)) {
            return new OrientedBoundingBox(this.attackBB(anim), 0, 0, this.position());
        }
        if (anim.is(STOMP)) {
            double reach = this.getBbWidth() * 0.55;
            Vec3 dir;
            float offset = anim.isAt("attack") ? -90 : 90;
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
    public AABB attackBB(AnimatedAction anim) {
        if (anim.is(JUMP, LAND)) {
            double attackSize = this.getBbWidth() * 1.5;
            return new AABB(-attackSize, -0.5, -attackSize, attackSize, 2, attackSize);
        }
        if (anim.is(STOMP)) {
            return new AABB(-1.8, -0.5, -2.2, 1.8, 2, 2.2);
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
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.SMALL_LEAF_SPELL_X3.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(LEAF_SHOOT);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(JUMP);
            else
                this.getAnimationHandler().setAnimation(DOUBLE_PUNCH);
        }
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
    public AnimationHandler<EntityRaccoon> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        if (this.isBerserk())
            return new Vec3(0, 16.25 / 16d, -10.5 / 16d).scale(1.4);
        return new Vec3(0, 17 / 16d, -5 / 16d);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }
}
