package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.network.S2CScreenShake;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.CustomDataSerializers;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
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

    public static final AnimatedAction DOUBLE_PUNCH = new AnimatedAction(0.88, 0.4, "double_punch");
    public static final AnimatedAction PUNCH = new AnimatedAction(0.92, 0.56, "punch");
    public static final AnimatedAction JUMP = new AnimatedAction(1.48, 1.32, "jump");
    public static final AnimatedAction STOMP = new AnimatedAction(1.36, 0.56, "stomp");
    public static final AnimatedAction LEAF_SHOOT = new AnimatedAction(0.88, 0.44, "shoot");
    public static final AnimatedAction LEAF_BOOMERANG = AnimatedAction.copyOf(LEAF_SHOOT, "spinning_shoot");
    public static final AnimatedAction LEAF_SHOT_CLONE = AnimatedAction.copyOf(LEAF_SHOOT, "leaf_clone");
    public static final AnimatedAction BARRAGE = new AnimatedAction(3.32, 0.44, "punch_barrage");
    public static final AnimatedAction ROAR = new AnimatedAction(1.24, 1, "roar");
    public static final AnimatedAction ANGRY = AnimatedAction.copyOf(ROAR, "angry");
    public static final AnimatedAction CLONE = AnimatedAction.copyOf(ROAR, "clone");

    public static final AnimatedAction DEFEAT = AnimatedAction.builder(100, "defeat").infinite().build();
    public static final AnimatedAction TRANSFORM = new AnimatedAction(30, 0, "transform");
    public static final AnimatedAction UNTRANSFORM = new AnimatedAction(2.2, 0, "untransform");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(DOUBLE_PUNCH, "interact");
    public static final AnimatedAction INTERACT_BERSERK = AnimatedAction.copyOf(PUNCH, "interact_berserk");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{DOUBLE_PUNCH, PUNCH, JUMP, STOMP, LEAF_SHOOT, LEAF_BOOMERANG, LEAF_SHOT_CLONE, BARRAGE, ROAR, ANGRY, CLONE, DEFEAT, TRANSFORM, UNTRANSFORM, INTERACT, INTERACT_BERSERK};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityRaccoon>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(DOUBLE_PUNCH, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (target != null) {
                entity.getNavigation().moveTo(target, 1.0);
                if (anim.getTick() == 1) {
                    entity.targetPosition = target.position();
                }
            }
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack() || anim.getTick() == 13) {
                entity.mobAttack(anim, target, entity::doHurtTarget);
            }
        });
        b.put(PUNCH, (anim, entity) -> {
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack() || anim.getTick() == 13) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(BARRAGE, (anim, entity) -> {
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack() || anim.isAtTick(0.84) || anim.isAtTick(1.28)) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
                LivingEntity target = entity.getTarget();
                Vec3 dir;
                if (target != null) {
                    Vec3 targetPos = target.position();
                    dir = new Vec3(targetPos.x - entity.getX(), 0.0, targetPos.z - entity.getZ()).normalize();
                } else
                    dir = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z()).normalize();
                entity.setDeltaMovement(entity.getDeltaMovement().add(dir.scale(0.6)));
            }
        });
        b.put(JUMP, (anim, entity) -> {
            entity.getNavigation().stop();
            LivingEntity target = entity.getTarget();
            double length = anim.getAttackTime() - 3;
            if (entity.jumpDir == null) {
                Vec3 jumpDir;
                if (target != null) {
                    Vec3 targetPos = target.position();
                    jumpDir = new Vec3(targetPos.x - entity.getX(), 0.0, targetPos.z - entity.getZ());
                    if (jumpDir.lengthSqr() > 25) // 14
                        jumpDir = jumpDir.normalize().scale(8);
                } else
                    jumpDir = new Vec3(entity.getLookAngle().x(), 0, entity.getLookAngle().z()).normalize().scale(8);
                entity.jumpDir = jumpDir.multiply(1 / length, 1, 1 / length);
            }
            if (anim.getTick() > 3 && anim.getTick() <= anim.getAttackTime()) {
                double d = Math.sin((anim.getTick() - 3) * Math.PI / length * 2) * 0.95;
                entity.setDeltaMovement(entity.jumpDir.x, d < 0 ? d * 1.65 : d, entity.jumpDir.z);
                if (anim.canAttack()) {
                    CustomDamage.Builder source = new CustomDamage.Builder(entity).noKnockback().element(EnumElement.EARTH).hurtResistant(5)
                            .withChangedAttribute(ModAttributes.STUN.get(), 80);
                    entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                    Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(8, 3), entity);
                    entity.level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, entity.getSoundSource(), 1.0f, 0.9f);
                }
                if (anim.getTick() >= anim.getAttackTime())
                    entity.setDeltaMovement(Vec3.ZERO);
            }
        });
        b.put(STOMP, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.lookAt(entity.getTarget(), 180.0f, 50.0f);
            }
            if (anim.canAttack() || anim.getTick() == 24) {
                CustomDamage.Builder source = new CustomDamage.Builder(entity).noKnockback().element(EnumElement.EARTH).hurtResistant(5)
                        .withChangedAttribute(ModAttributes.STUN.get(), 50);
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e, source));
                Platform.INSTANCE.sendToTrackingAndSelf(new S2CScreenShake(8, 3), entity);
                entity.level.playSound(null, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, entity.getSoundSource(), 1.0f, 0.9f);
            }
        });
        b.put(LEAF_SHOOT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack()) {
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
            if (anim.canAttack() || anim.isAtTick(anim.getAttackTime() + 6)) {
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
            if (anim.canAttack()) {
                if (entity.isEnraged())
                    ModSpells.BIG_LEAF_SPELL_DOUBLE.get().use(entity);
                else
                    ModSpells.BIG_LEAF_SPELL.get().use(entity);
            }
        });
        b.put(ROAR, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAtTick(1))
                entity.playAngrySound();
        });
        b.put(CLONE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAtTick(1)) {
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
            if (entity.isOnGround() && anim.isPastTick(1.0) && !anim.isPastTick(1.5)) {
                entity.push(0, 0.4, 0);
            }
        });
    });
    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityRaccoon>>> ATTACKS = List.of(
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(DOUBLE_PUNCH)
                    .cooldown(e -> e.animationCooldown(DOUBLE_PUNCH))
                    .withCondition(((goal, target, previous) -> !goal.attacker.isBerserk()))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(4, 1.2, 5), e -> 20 + e.getRandom().nextInt(10))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(DOUBLE_PUNCH)
                    .cooldown(e -> e.animationCooldown(DOUBLE_PUNCH))
                    .withCondition(((goal, target, previous) -> !goal.attacker.isBerserk()))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 7),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(PUNCH)
                    .cooldown(e -> e.animationCooldown(PUNCH))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !PUNCH.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(JUMP)
                    .cooldown(e -> e.animationCooldown(JUMP))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !JUMP.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1, 6), e -> 25 + e.getRandom().nextInt(10))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(STOMP)
                    .cooldown(e -> e.animationCooldown(STOMP))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !STOMP.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 10),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(LEAF_SHOOT)
                    .cooldown(e -> e.animationCooldown(LEAF_SHOOT))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !LEAF_SHOOT.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 5), e -> 25 + e.getRandom().nextInt(10))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(LEAF_BOOMERANG)
                    .cooldown(e -> e.animationCooldown(LEAF_BOOMERANG))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !LEAF_BOOMERANG.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(3, 1, 5), e -> 25 + e.getRandom().nextInt(10))), 9),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(ROAR)
                    .cooldown(e -> e.animationCooldown(ROAR))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !ROAR.getID().equals(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 6),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(BARRAGE)
                    .cooldown(e -> e.animationCooldown(BARRAGE))
                    .withCondition(((goal, target, previous) -> goal.attacker.isBerserk() && !BARRAGE.getID().equals(previous)))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1), e -> 25 + e.getRandom().nextInt(10))), 2),
            WeightedEntry.wrap(new GoalAttackAction<EntityRaccoon>(CLONE)
                    .cooldown(e -> e.animationCooldown(CLONE))
                    .withCondition(((goal, target, previous) -> goal.attacker.isEnraged() && !CLONE.getID().equals(previous)))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 9)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityRaccoon>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<EntityRaccoon>(() -> new MoveToTargetRunner<>(1, 1))
                    .withCondition(((goal, target) -> goal.attacker.isBerserk())), 10),
            WeightedEntry.wrap(new IdleAction<EntityRaccoon>(() -> new MoveAwayRunner<>(1, 1, 5))
                    .withCondition(((goal, target) -> !goal.attacker.isBerserk())), 10)
    );

    public final AnimatedAttackGoal<EntityRaccoon> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private boolean clone;
    private Vec3 jumpDir;

    private int hit;
    private int hitCountdown = -1;

    private final AnimationHandler<EntityRaccoon> animationHandler = new AnimationHandler<>(this, ANIMS)
            .setAnimationChangeCons(anim -> {
                if (CLONE.is(anim)) {
                    this.clone = true;
                } else if (anim != null) {
                    this.clone = false;
                    this.entityData.set(CLONE_CENTER, Optional.empty());
                }
                this.jumpDir = null;
            }).setAnimationChangeFunc(anim -> {
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BERSERK, false);
        this.entityData.define(CLONE_CENTER, Optional.empty());
        this.entityData.define(CLONE_INDEX, 0);
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
        if (!this.level.isClientSide) {
            --this.hitCountdown;
            if (this.isAlive() && !this.isBerserk() && (this.hitCountdown == 0 || this.hit >= 5)) {
                this.setBerserk(true, false);
                this.hit = 0;
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getAnimationHandler().isCurrent(JUMP, DEFEAT, TRANSFORM, UNTRANSFORM, ANGRY))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    protected void actuallyHurt(DamageSource damageSrc, float damageAmount) {
        super.actuallyHurt(damageSrc, damageAmount);
        if (!this.isBerserk()) {
            this.hit++;
            this.hitCountdown = 30;
        } else if (this.getAnimationHandler().isCurrent(BARRAGE) && this.getAnimationHandler().getAnimation().isPastTick(1.4)) {
            this.setBerserk(false, false);
            this.getAnimationHandler().setAnimation(UNTRANSFORM);
            this.push(0, 0.6, 0);
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
        LivingEntity target = this.getTarget();
        if (target != null && !anim.is(STOMP)) {
            this.lookAt(target, 180.0f, 50.0f);
        }
        this.getNavigation().stop();
        BiConsumer<AnimatedAction, EntityRaccoon> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(JUMP)) {
            return this.attackAABB(anim).move(this.position());
        }
        if (anim.is(STOMP)) {
            double reach = this.getBbWidth() * 0.55;
            Vec3 dir;
            float offset = anim.canAttack() ? -90 : 90;
            if (this.getControllingPassenger() instanceof Player player)
                dir = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + offset);
            else
                dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot() + offset);
            Vec3 attackPos = this.position().add(dir.scale(reach));
            return this.attackAABB(anim).move(attackPos.x, attackPos.y, attackPos.z);
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return this.isBerserk() ? 2.5 : 1.5;
    }

    @Override
    public AABB attackAABB(AnimatedAction anim) {
        if (anim.is(JUMP)) {
            double attackSize = this.getBbWidth() + 2.75;
            return new AABB(-attackSize, -0.5, -attackSize, attackSize, 2, attackSize);
        }
        if (anim.is(STOMP)) {
            return new AABB(-2, -0.5, -2, 2, 2, 2);
        }
        return super.attackAABB(anim);
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
