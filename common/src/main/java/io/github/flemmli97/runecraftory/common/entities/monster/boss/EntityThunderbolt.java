package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableDatas;
import io.github.flemmli97.runecraftory.common.entities.data.SyncableEntityData;
import io.github.flemmli97.runecraftory.common.entities.utils.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.network.S2CMobUpdate;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetAttackRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.StrafingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.particle.ColoredParticleData;
import io.github.flemmli97.tenshilib.common.utils.math.OrientedBoundingBox;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

    public static final AnimatedAction BACK_KICK = AnimatedAction.builder(0.64, "back_kick").marker("attack", 0.32).build();
    public static final AnimatedAction LASER_X5 = AnimatedAction.builder(1.44, "laser_x5").marker("attack", 1.2).build();
    public static final AnimatedAction STOMP = AnimatedAction.builder(0.44, "stomp").marker("attack", 0.28).build();
    public static final AnimatedAction HORN_ATTACK = AnimatedAction.builder(0.44, "horn_attack").marker("attack", 0.24).build();
    public static final AnimatedAction BACK_KICK_HORN = AnimatedAction.copyOf(BACK_KICK, "back_kick_horn");
    public static final AnimatedAction CHARGE = AnimatedAction.builder(1.64, "charge")
            .marker("attack_start", 0.44).marker("attack_end", 1.12).build();
    public static final AnimatedAction CHARGE_2 = AnimatedAction.copyOf(CHARGE, "charge_2");
    public static final AnimatedAction CHARGE_3 = AnimatedAction.copyOf(CHARGE, "charge_3");
    public static final AnimatedAction LASER_AOE = AnimatedAction.copyOf(LASER_X5, "laser_aoe");
    public static final AnimatedAction LASER_KICK = AnimatedAction.builder(1.2, "laser_kick").marker("attack", 0.32).build();
    public static final AnimatedAction LASER_KICK_2 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_2");
    public static final AnimatedAction WIND_BLADE = AnimatedAction.builder(0.72, "wind_blade").marker("attack", 0.36).build();
    public static final AnimatedAction LASER_KICK_3 = AnimatedAction.copyOf(LASER_KICK, "laser_kick_3");
    public static final AnimatedAction FEINT = AnimatedAction.builder(2, "feint").marker("neigh", 0.96).build();
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(10, "defeat").infinite().build();
    public static final AnimatedAction NEIGH = AnimatedAction.builder(1.16, "neigh").marker("neigh", 0.48).build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(STOMP, "interact");

    private static final float RANGE_THRESHOLD = 0.7f;
    private static final float FEINT_THRESHOLD = 0.35f;

    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{BACK_KICK, LASER_X5, STOMP, HORN_ATTACK, BACK_KICK_HORN, CHARGE, CHARGE_2, CHARGE_3,
            LASER_AOE, LASER_KICK, LASER_KICK_2, WIND_BLADE, LASER_KICK_3, FEINT, DEFEAT, NEIGH, INTERACT};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityThunderbolt>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityThunderbolt> kick = (anim, entity) -> {
            LivingEntity target = entity.getTarget();
            if (anim.isAt("attack")) {
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
            if (anim.isAt("attack")) {
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
            if (anim.isAt("attack")) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
        b.put(WIND_BLADE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.DOUBLE_SONIC.get().use(entity);
            }
        });
        b.put(LASER_X5, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.LASER5.get().use(entity);
            }
        });
        b.put(LASER_AOE, (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.LASER_AOE.get().use(entity);
            }
        });
        BiConsumer<AnimatedAction, EntityThunderbolt> bigLaser = (anim, entity) -> {
            if (anim.isAt("attack")) {
                ModSpells.BIG_LIGHTNING.get().use(entity);
            }
        };
        b.put(LASER_KICK, bigLaser);
        b.put(LASER_KICK_2, bigLaser);
        b.put(LASER_KICK_3, bigLaser);
        BiConsumer<AnimatedAction, EntityThunderbolt> charge = (anim, entity) -> {
            if (entity.chargeMotion == null) {
                entity.setChargeDirection(EntityUtils.getTargetDirection(entity, EntityAnchorArgument.Anchor.FEET, true)
                        .scale(2));
            }
            if (anim.isAt("attack_start")) {
                entity.setDeltaMovement(entity.chargeMotion.x(), 0.2, entity.chargeMotion.z());
            }
            if (anim.isPast("attack_start") && !anim.isPast("attack_end") && !entity.chargeAttackSuccess) {
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
            if (anim.isAt("neigh"))
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
        b.put(FEINT, (anim, entity) -> {
            if (anim.isAt("neigh"))
                entity.playSound(ModSounds.ENTITY_THUNDERBOLT_NEIGH.get(), 1, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2f + 1.0f);
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityThunderbolt>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(BACK_KICK)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, BACK_KICK) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_X5)
                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, LASER_X5) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(2, 1.2, 4), e -> 35 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(STOMP)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, STOMP) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 11),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(HORN_ATTACK)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, HORN_ATTACK) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 9),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(CHARGE)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, CHARGE) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_AOE)
                    .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, LASER_AOE) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(2, 1.2, 4), e -> 35 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(LASER_KICK)
                    .withCondition((goal, target, previous) -> goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, LASER_KICK) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(WIND_BLADE)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, WIND_BLADE) && !goal.attacker.feintedDeath)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.2, 7), e -> 35 + e.getRandom().nextInt(15))), 7),
            WeightedEntry.wrap(MonsterActionUtils.<EntityThunderbolt>nonRepeatableAttack(WIND_BLADE)
                    .withCondition((goal, target, previous) -> goal.attacker.allowAnimation(previous, WIND_BLADE) && !goal.attacker.feintedDeath
                            && (goal.attacker.getTarget() != null && goal.attacker.getTarget().getY() - goal.attacker.getY() > 4))
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.2, 7), e -> 35 + e.getRandom().nextInt(15))), 6),

            WeightedEntry.wrap(new GoalAttackAction<EntityThunderbolt>(LASER_AOE)
                    .cooldown(e -> e.animationCooldown(LASER_AOE) + 30)
                    .withCondition((goal, target, previous) -> goal.attacker.afterFeint())
                    .prepare(() -> new TimedWrappedRunner<>(new MoveAwayRunner<>(2, 1.2, 4), e -> 35 + e.getRandom().nextInt(15))), 4),
            WeightedEntry.wrap(new GoalAttackAction<EntityThunderbolt>(CHARGE)
                    .cooldown(e -> e.animationCooldown(CHARGE) + 40)
                    .withCondition((goal, target, previous) -> goal.attacker.afterFeint())
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 20),
            WeightedEntry.wrap(new GoalAttackAction<EntityThunderbolt>(LASER_KICK)
                    .cooldown(e -> e.animationCooldown(LASER_KICK) + 50)
                    .withCondition((goal, target, previous) -> goal.attacker.afterFeint())
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.2), e -> 35 + e.getRandom().nextInt(15))), 17)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityThunderbolt>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1.1, 0.5)), 8),
            WeightedEntry.wrap(new IdleAction<>(() -> new StrafingRunner<>(7, 1.1f, 0.2f)), 10),
            WeightedEntry.wrap(new IdleAction<EntityThunderbolt>(DoNothingRunner::new)
                    .withCondition(((goal, target) -> goal.attacker.afterFeint())), 6)
    );

    public final AnimatedAttackGoal<EntityThunderbolt> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityThunderbolt> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .withChangeListener(anim -> {
                if (!this.level.isClientSide) {
                    this.setChargeDirection(null);
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
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.THUNDERBOLT_FIGHT.get());
    }

    private boolean afterFeint() {
        return !this.isTamed() && this.isEnraged() && this.feintedDeath;
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
    public void handleAttack(AnimatedAction anim) {
        BiConsumer<AnimatedAction, EntityThunderbolt> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public OrientedBoundingBox calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(STOMP)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(1.7, -0.4, 1.7), this.getYRot(), 0, this.position());
        } else if (anim.is(CHARGE, CHARGE_2, CHARGE_3)) {
            return new OrientedBoundingBox(OrientedBoundingBox.originAABB(this)
                    .inflate(grow + 1), this.getYRot(), 0, this.position());
        } else
            return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public AABB attackBB(AnimatedAction anim) {
        double width = this.getBbWidth() * 1.6;
        double length = this.getBbWidth() * 1.5;
        if (anim.is(HORN_ATTACK)) {
            width = this.getBbWidth() * 1.3;
            length = this.getBbWidth() * 1.8;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
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
        return 1.5;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.getHealth() > 0 && this.getAnimationHandler().isCurrent(DEFEAT) && !this.feintedDeath && !this.isTamed()) {
            AnimatedAction anim = this.getAnimationHandler().getAnimation();
            if (anim.done(0)) {
                this.feintDeath();
            }
        }
        if (this.getAnimationHandler().isCurrent(FEINT, DEFEAT) && !this.isTamed()) {
            Vec3 delta = this.getDeltaMovement();
            this.setDeltaMovement(0, delta.y, 0);
            if (this.getAnimationHandler().getAnimation().is(DEFEAT)) {
                int tick = (int) this.getAnimationHandler().getAnimation().getTick(1);
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

    protected void feintDeath() {
        this.feintedDeath = true;
        this.getAnimationHandler().setAnimation(FEINT);
        STAT_INCREASE.forEach(att -> {
            AttributeInstance inst = this.getAttribute(att.get());
            inst.removeModifier(STAT_INCREASE_ID);
            inst.addPermanentModifier(new AttributeModifier(STAT_INCREASE_ID, "rf.boss_stat_increase", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL));
        });
    }

    @Override
    protected Vec3 directionToLookAt() {
        if (this.getAnimationHandler().isCurrent(CHARGE, CHARGE_2, CHARGE_3)) {
            return this.chargeMotion;
        }
        return super.directionToLookAt();
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
            this.bossInfo.setProgress((this.getHealth() - (this.getMaxHealth() * FEINT_THRESHOLD)) / (this.getMaxHealth() * RANGE_THRESHOLD));
        else
            this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected boolean checkRage() {
        if (this.getHealth() / this.getMaxHealth() < FEINT_THRESHOLD)
            return !this.feintedDeath;
        if (this.getHealth() / this.getMaxHealth() < RANGE_THRESHOLD)
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
        if (!state.liquid()) {
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
    public boolean allowAnimation(String prev, AnimatedAction other) {
        if (!this.isTamed() && this.isEnraged() && this.feintedDeath) {
            return other.is(CHARGE, LASER_KICK, LASER_AOE);
        }
        if (prev.equals(CHARGE_2.getID()) || prev.equals(CHARGE_3.getID()))
            return !other.getID().equals(CHARGE.getID());
        if (prev.equals(LASER_KICK_2.getID()) || prev.equals(LASER_KICK_3.getID()))
            return !other.getID().equals(LASER_KICK.getID());
        if (prev.equals(BACK_KICK_HORN.getID()))
            return !other.getID().equals(HORN_ATTACK.getID());
        return super.allowAnimation(prev, other);
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

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public void playAngrySound() {
    }

    protected void setChargeDirection(Vec3 moveDirection) {
        this.chargeMotion = moveDirection;
        S2CMobUpdate.send(this, SyncableDatas.MOTION_DIR, this.chargeMotion);
    }

    @Override
    public void onUpdate(SyncableEntityData.SyncedContainer<?> data) {
        super.onUpdate(data);
        data.runIf(SyncableDatas.MOTION_DIR, motion -> this.chargeMotion = motion);
    }
}