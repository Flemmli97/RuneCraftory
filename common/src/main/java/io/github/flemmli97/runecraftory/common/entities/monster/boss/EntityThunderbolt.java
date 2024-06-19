package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

public class EntityThunderbolt extends BossMonster {

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityThunderbolt.class, EntityDataSerializers.FLOAT);

    public static final AnimatedAction BACK_KICK = new AnimatedAction(0.64, 0.32, "back_kick");
    public static final AnimatedAction LASER_X5 = new AnimatedAction(1.44, 1.2, "laser_x5");
    public static final AnimatedAction STOMP = new AnimatedAction(0.44, 0.28, "stomp");
    public static final AnimatedAction HORN_ATTACK = new AnimatedAction(0.44, 0.24, "horn_attack");
    public static final AnimatedAction BACK_KICK_HORN = AnimatedAction.copyOf(BACK_KICK, "back_kick_horn");
    public static final AnimatedAction CHARGE = new AnimatedAction(1.64, 0.44, "charge");
    public static final AnimatedAction CHARGE_2 = AnimatedAction.copyOf(CHARGE, "charge_2");
    public static final AnimatedAction CHARGE_3 = AnimatedAction.copyOf(CHARGE, "charge_3");
    public static final AnimatedAction LASER_AOE = AnimatedAction.copyOf(LASER_X5, "laser_aoe");
    public static final AnimatedAction LASER_KICK = new AnimatedAction(1.2, 0.32, "laser_kick");
    public static final AnimatedAction LASER_KICK_2 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_2");
    public static final AnimatedAction WIND_BLADE = new AnimatedAction(0.72, 0.36, "wind_blade");
    public static final AnimatedAction LASER_KICK_3 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_3");
    public static final AnimatedAction FEINT = new AnimatedAction(2, 0.9, "feint");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(80, "defeat").marker(60).infinite().build();
    public static final AnimatedAction NEIGH = new AnimatedAction(24, 9, "neigh");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(STOMP, "interact");

    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{BACK_KICK, LASER_X5, STOMP, HORN_ATTACK, BACK_KICK_HORN, CHARGE, CHARGE_2, CHARGE_3,
            LASER_AOE, LASER_KICK, LASER_KICK_2, WIND_BLADE, LASER_KICK_3, FEINT, DEFEAT, NEIGH, INTERACT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityThunderbolt>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityThunderbolt> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack()) {
                entity.mobAttack(anim, target, e -> {
                    if (entity.doHurtTarget(e)) {
                        e.setDeltaMovement(0, 0.05, 0);
                        e.hasImpulse = true;
                        e.knockback(0.8f, entity.getX() - e.getX(), entity.getZ() - e.getZ());
                    }
                });
            }
        };
        b.put(BACK_KICK, kick);
        b.put(BACK_KICK_HORN, kick);
        b.put(HORN_ATTACK, (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.canAttack()) {
                AtomicBoolean bool = new AtomicBoolean(false);
                entity.mobAttack(anim, target, e -> {
                    if (entity.doHurtTarget(e)) {
                        if (!bool.get())
                            bool.set(true);
                        e.setDeltaMovement(0, 0.65, 0);
                        e.hasImpulse = true;
                    }
                });
                if (bool.get() && !entity.isVehicle()) {
                    entity.hornAttackSuccess = true;
                }
            }
        });
        b.put(STOMP, (anim, entity) -> {
            if (anim.canAttack()) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(WIND_BLADE, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.DOUBLE_SONIC.get().use(entity);
            }
        });
        b.put(LASER_X5, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.LASER5.get().use(entity);
            }
        });
        b.put(LASER_AOE, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.LASER_AOE.get().use(entity);
            }
        });
        BiConsumer<AnimatedAction, EntityThunderbolt> bigLaser = (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.BIG_LIGHTNING.get().use(entity);
            }
        };
        b.put(LASER_KICK, bigLaser);
        b.put(LASER_KICK_2, bigLaser);
        b.put(LASER_KICK_3, bigLaser);
        BiConsumer<AnimatedAction, EntityThunderbolt> charge = (anim, entity) -> {
            if (anim.canAttack()) {
                if (entity.chargeMotion != null) {
                    entity.setDeltaMovement(entity.chargeMotion.x(), 0.2, entity.chargeMotion.z());
                }
            }
            if ((anim.getTick() < anim.getLength() - 6 && anim.getTick() > anim.getAttackTime()) && !entity.chargeAttackSuccess) {
                entity.mobAttack(anim, null, e -> {
                    if (entity.doHurtTarget(e)) {
                        entity.chargeAttackSuccess = true;
                        entity.setDeltaMovement(0, entity.getDeltaMovement().y, 0);
                    }
                });
            }
        };
        b.put(CHARGE, charge);
        b.put(CHARGE_2, charge);
        b.put(CHARGE_3, charge);
        b.put(NEIGH, (anim, entity) -> {
            if (anim.canAttack())
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
        b.put(FEINT, (anim, entity) -> {
            if (anim.canAttack())
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityThunderbolt>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(BACK_KICK)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 5),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_X5)
                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && !goal.attacker.isAnimEqual(previous, LASER_X5))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(2, 1.2, 4), e -> 35 + e.getRandom().nextInt(15))), 5),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(STOMP)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 6),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(HORN_ATTACK)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 5),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(CHARGE)
                    .withCondition((goal, target, previous) -> !goal.attacker.isAnimEqual(previous, CHARGE))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 5),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_AOE)
                    .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && !goal.attacker.isAnimEqual(previous, LASER_AOE))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(2, 1.2, 4), e -> 35 + e.getRandom().nextInt(15))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_KICK)
                    .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && !goal.attacker.isAnimEqual(previous, LASER_KICK))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(WIND_BLADE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.2, 7), e -> 35 + e.getRandom().nextInt(15))), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityThunderbolt>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1.1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(7, 1.1f, 0.2f)), 1)
    );

    public final AnimatedAttackGoal<EntityThunderbolt> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityThunderbolt> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .setAnimationChangeFunc(anim -> {
                if (!this.level.isClientSide) {
                    this.chargeMotion = null;
                    if (anim == null) {
                        AnimatedAction chainAnim = this.chainAnim(this.getAnimationHandler().getAnimation());
                        this.chargeAttackSuccess = false;
                        this.hornAttackSuccess = false;
                        boolean chain = !this.commanded;
                        this.commanded = false;
                        if (chain) {
                            if (chainAnim != null) {
                                this.getAnimationHandler().setAnimation(chainAnim);
                                return true;
                            }
                        }
                    } else if (anim.is(CHARGE, CHARGE_2, CHARGE_3)) {
                        if (this.isVehicle()) {
                            this.lockYaw(this.getControllingPassenger().getYHeadRot());
                            Vec3 dir = this.getControllingPassenger().getLookAngle();
                            dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(2);
                            this.chargeMotion = dir;
                        } else if (this.getTarget() != null) {
                            LivingEntity target = this.getTarget();
                            Vec3 dir = target.position().subtract(this.position());
                            dir = new Vec3(dir.x(), 0, dir.z()).normalize().scale(2);
                            this.chargeMotion = dir;
                            this.lookAt(target, 360, 10);
                            this.lockYaw(this.getYRot());
                        }
                    }
                    return false;
                }
                return false;
            });

    protected boolean feintedDeath, hornAttackSuccess, chargeAttackSuccess;
    private Vec3 chargeMotion;
    private boolean commanded;

    public EntityThunderbolt(EntityType<? extends BossMonster> type, Level world) {
        super(type, world);
        this.bossInfo.setColor(BossEvent.BossBarColor.BLUE);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.THUNDERBOLT_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.31);
        super.applyAttributes();
    }

    @Override
    public void addGoal() {
        super.addGoal();
        this.goalSelector.removeGoal(this.wander);
        this.wander = new RestrictedWaterAvoidingStrollGoal(this, 0.6);
        this.goalSelector.addGoal(6, this.wander);
    }

    @Override
    public double sprintSpeedThreshold() {
        return 0.9;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(FEINT, DEFEAT, NEIGH))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(FEINT, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT))
            return;
        super.push(x, y, z);
    }

    @Override
    public AnimatedAction getDeathAnimation() {
        return DEFEAT;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return this.getBbWidth() * 0.8;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 30.0f, 30.0f);
        }
        BiConsumer<AnimatedAction, EntityThunderbolt> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(STOMP)) {
            return this.getBoundingBox().inflate(1.5, -0.4, 1.5);
        } else if (anim.is(CHARGE, CHARGE_2, CHARGE_3)) {
            return this.getBoundingBox().inflate(1);
        } else
            return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.LASER5.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(LASER_X5);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(STOMP);
            else
                this.getAnimationHandler().setAnimation(HORN_ATTACK);
            this.commanded = true;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.HORSE_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.HORSE_AMBIENT;
    }

    @Override
    public float getVoicePitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.8f;
    }

    @Override
    public double ridingSpeedModifier() {
        return 1.1;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getHealth() > 0 && this.getAnimationHandler().isCurrent(DEFEAT) && !this.feintedDeath && !this.isTamed()) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim.getTick() > anim.getLength()) {
                this.feintedDeath = true;
                this.getAnimationHandler().setAnimation(FEINT);
            }
        }
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_2, CHARGE_3)) {
            this.setXRot(0);
            this.setYRot(this.entityData.get(LOCKED_YAW));
        }
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT) && !this.isTamed()) {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(0, delta.y, 0);
            if (this.getAnimationHandler().getAnimation().is(DEFEAT)) {
                int tick = this.getAnimationHandler().getAnimation().getTick();
                if (tick < 40) {
                    if (tick % 10 == 0)
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight()),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth()),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else if (tick < 80) {
                    if (tick % 2 == 0)
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 2),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                } else {
                    int amount = (tick - 80) / 10;
                    for (int i = 0; i < amount; i++) {
                        this.level.addParticle(new ColoredParticleData(ModParticles.BLINK.get(), 71 / 255F, 237 / 255F, 255 / 255F, 1),
                                this.getX() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                                this.getY() + this.random.nextDouble() * (this.getBbHeight() + 1),
                                this.getZ() + (this.random.nextDouble() - 0.5D) * (this.getBbWidth() + 3),
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D,
                                this.random.nextGaussian() * 0.02D);
                    }
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Feint", this.feintedDeath);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.feintedDeath = compound.getBoolean("Feint");
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        if (flag && !load) {
            if (!this.isEnraged()) {
                this.getAnimationHandler().setAnimation(NEIGH);
                this.getNavigation().stop();
            } else {
                this.getAnimationHandler().setAnimation(DEFEAT);
                this.getNavigation().stop();
                this.bossInfo.setProgress(0);
            }
        }
        super.setEnraged(flag, load);
    }

    @Override
    protected void updateBossBar() {
        if (!this.feintedDeath)
            this.bossInfo.setProgress((this.getHealth() - (this.getMaxHealth() * 0.3f)) / (this.getMaxHealth() * 0.7f));
        else
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < 0.3)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < 0.7)
            return !this.isEnraged();
        return false;
    }

    @Override
    protected void fullyHeal() {
        super.fullyHeal();
        this.feintedDeath = false;
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() && (this.getAnimationHandler() == null || !this.getAnimationHandler().isCurrent(FEINT, DEFEAT));
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if (!state.getMaterial().isLiquid()) {
            BlockState blockstate = this.level.getBlockState(pos.above());
            SoundType soundtype = Platform.INSTANCE.getSoundType(state, this.level, pos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = Platform.INSTANCE.getSoundType(blockstate, this.level, pos, this);
            }
            this.playSound(SoundEvents.HORSE_GALLOP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
        }
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 27 / 16d, -4 / 16d);
    }

    @Override
    public AnimationHandler<EntityThunderbolt> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimEqual(String prev, AnimatedAction other) {
        if (other == null)
            return true;
        if (prev.equals(CHARGE_2.getID()) || prev.equals(CHARGE_3.getID()))
            return other.getID().equals(CHARGE.getID());
        if (prev.equals(LASER_KICK_2.getID()) || prev.equals(LASER_KICK_3.getID()))
            return other.getID().equals(LASER_KICK.getID());
        if (prev.equals(BACK_KICK_HORN.getID()))
            return other.getID().equals(HORN_ATTACK.getID());
        return prev.equals(other.getID());
    }

    public AnimatedAction chainAnim(AnimatedAction anim) {
        if (anim == null)
            return null;
        return switch (anim.getID()) {
            case "laser_kick" -> this.isEnraged() && this.feintedDeath ? LASER_KICK_2 : null;
            case "laser_kick_2" -> this.feintedDeath ? LASER_KICK_3 : null;
            case "horn_attack" -> this.hornAttackSuccess ? BACK_KICK_HORN : null;
            case "charge" -> this.chargeAttackSuccess ? null : CHARGE_2;
            case "charge_2" -> this.isEnraged() && !this.chargeAttackSuccess ? CHARGE_3 : null;
            default -> null;
        };
    }

    public void lockYaw(float yaw) {
        this.entityData.set(LOCKED_YAW, yaw);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public void playAngrySound() {
    }
}