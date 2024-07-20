package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.DoNothingRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.WrappedRunner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntitySarcophagus extends BossMonster implements MobAttackExt {

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntitySarcophagus.class, EntityDataSerializers.FLOAT);

    public static final AnimatedAction TELEPORT = new AnimatedAction(2.64, "teleport");
    public static final AnimatedAction CHARGE = new AnimatedAction(1.6, 0.44, "charge");
    public static final AnimatedAction BEAM = new AnimatedAction(0.68, 0.48, "cast");
    public static final AnimatedAction BEAM_3X = new AnimatedAction(2.2, 0.48, "cast_3x");
    public static final AnimatedAction FIRE_CIRCLE = new AnimatedAction(2.32, 0.4, "circle_cast");
    public static final AnimatedAction WIND_CIRCLE = AnimatedAction.copyOf(FIRE_CIRCLE, "wind_circle");
    public static final AnimatedAction ICE_CIRCLE = AnimatedAction.copyOf(FIRE_CIRCLE, "ice_circle");
    public static final AnimatedAction EARTH_CIRCLE = AnimatedAction.copyOf(FIRE_CIRCLE, "earth_circle");
    public static final AnimatedAction LIGHT_2X = AnimatedAction.copyOf(BEAM, "light_2x");
    public static final AnimatedAction LIGHT_4X = AnimatedAction.copyOf(BEAM, "light_4x");
    public static final AnimatedAction SHINE = AnimatedAction.copyOf(BEAM, "shine");
    public static final AnimatedAction PRISM = AnimatedAction.copyOf(BEAM, "prism");
    public static final AnimatedAction MISSILE = new AnimatedAction(1.04, 0.72, "missile");
    public static final AnimatedAction STARFALL = new AnimatedAction(8.2, 0.8, "starfall");
    public static final AnimatedAction ANGRY = new AnimatedAction(1.04, "angry");
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(80, "defeat").marker(60).infinite().build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(BEAM, "interact");
    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{TELEPORT, CHARGE, BEAM, BEAM_3X, FIRE_CIRCLE, WIND_CIRCLE, ICE_CIRCLE, EARTH_CIRCLE,
            LIGHT_2X, LIGHT_4X, SHINE, PRISM, MISSILE, STARFALL, DEFEAT, INTERACT, ANGRY};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntitySarcophagus>> ATTACK_HANDLER = createAnimationHandler(b -> {
        b.put(TELEPORT, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAtTick(0.2) || anim.isAtTick(0.44) || anim.isAtTick(1.2) || anim.isAtTick(1.44) || anim.isAtTick(2.2) || anim.isAtTick(2.44)) {
                entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            if (anim.isAtTick(0.28) || anim.isAtTick(1.28) || anim.isAtTick(2.28))
                entity.teleportAround(8, 10);
        });
        b.put(CHARGE, (anim, entity) -> {
            if (anim.isPastTick(0.28) && !anim.isPastTick(1.48)) {
                if (entity.hitEntity == null)
                    entity.hitEntity = new ArrayList<>();
                if (entity.chargeMotion == null) {
                    Vec3 dir = entity.getLookAngle();
                    float yaw = entity.getYRot();
                    if (entity.isVehicle()) {
                        yaw = entity.getControllingPassenger().getYHeadRot();
                        dir = entity.getControllingPassenger().getLookAngle();
                    } else if (entity.getTarget() != null) {
                        dir = entity.getTarget().position().subtract(entity.position()).normalize();
                        entity.lookAt(entity.getTarget(), 360, 10);
                        yaw = entity.getYRot();
                    }
                    entity.chargeMotion = dir.scale(0.28);
                    entity.entityData.set(LOCKED_YAW, yaw);
                }
                entity.setDeltaMovement(entity.chargeMotion.x(), entity.getDeltaMovement().y(), entity.chargeMotion.z());
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.BACK).knockAmount(2))) {
                        entity.hitEntity.add(e);
                    }
                });
                if (entity.tickCount % 5 == 0) {
                    entity.playSound(ModSounds.ENTITY_GENERIC_HEAVY_CHARGE.get(), 1, (entity.random.nextFloat() - entity.random.nextFloat()) * 0.2f + 1.0f);
                }
            } else {
                entity.setDeltaMovement(entity.getDeltaMovement().scale(0.6));
            }
        });
        b.put(BEAM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.LIGHT_BEAM.get().use(entity);
        });
        b.put(BEAM_3X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack() || anim.isAtTick(1.24) || anim.isAtTick(2.0))
                ModSpells.LIGHT_BEAM.get().use(entity);
        });
        b.put(FIRE_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.FIRE_CIRCLE.get().use(entity);
        });
        b.put(WIND_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.WIND_CIRCLE.get().use(entity);
        });
        b.put(ICE_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.ICE_CIRCLE.get().use(entity);
        });
        b.put(EARTH_CIRCLE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.EARTH_CIRCLE.get().use(entity);
        });
        b.put(LIGHT_2X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.EXPANDING_DOUBLE_LIGHT.get().use(entity);
        });
        b.put(LIGHT_4X, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.EXPANDING_QUAD_LIGHT.get().use(entity);
        });
        b.put(SHINE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.SHINE.get().use(entity);
        });
        b.put(PRISM, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.PRISM_LONG.get().use(entity);
        });
        b.put(MISSILE, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.canAttack())
                ModSpells.MISSILE_8X.get().use(entity);
        });
        b.put(STARFALL, (anim, entity) -> {
            entity.getNavigation().stop();
            if (anim.isAtTick(0.24)) {
                entity.starFallPre = entity.position();
                if (entity.hasRestriction()) {
                    Vec3 pos = Vec3.atCenterOf(entity.getRestrictCenter());
                    entity.teleportTo(pos.x, pos.y + 8, pos.z);
                } else if (entity.getTarget() != null) {
                    entity.teleportTo(entity.getTarget().getX(), entity.getTarget().getY() + 8, entity.getTarget().getZ());
                } else {
                    entity.teleportTo(entity.getX(), entity.getY() + 8, entity.getZ());
                }
                entity.starFallPos = entity.position();
                ModSpells.STARFALL_LONG.get().use(entity);
            }
            if (entity.starFallPos != null)
                entity.setPos(entity.starFallPos);
            if (anim.isAtTick(7.96)) {
                entity.teleportTo(entity.starFallPre.x(), entity.starFallPre.y(), entity.starFallPre.z());
                entity.starFallPre = null;
                entity.starFallPos = null;
                entity.teleportAround(6, 12);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntitySarcophagus>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(TELEPORT)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(CHARGE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(BEAM)
                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, BEAM))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(BEAM_3X)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(FIRE_CIRCLE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(WIND_CIRCLE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(ICE_CIRCLE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(EARTH_CIRCLE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 4),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(LIGHT_2X)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(LIGHT_4X)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(SHINE)
                    .withCondition((goal, target, previous) -> !goal.attacker.isEnraged() && goal.attacker.allowAnimation(previous, SHINE))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 7),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(PRISM)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 7),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>nonRepeatableAttack(MISSILE)
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 8),
            WeightedEntry.wrap(MonsterActionUtils.<EntitySarcophagus>enragedBossAttack(STARFALL)
                    .withCondition(((goal, target, previous) -> goal.attacker.isEnraged() && goal.attacker.starfallCooldown <= 0))
                    .prepare(() -> new WrappedRunner<>(new DoNothingRunner<>(true))), 10)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntitySarcophagus>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new DoNothingRunner<>(true)), 1)
    );

    public final AnimatedAttackGoal<EntitySarcophagus> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntitySarcophagus> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS).setAnimationChangeCons(anim -> {
        this.hitEntity = null;
        if (anim != null) {
            this.teleported = TELEPORT.is(anim);
            if (!TELEPORT.is(anim))
                this.previousAttack = anim.getID();
            if (CHARGE.is(anim)) {
                this.chargeMotion = null;
                this.prevStepHeight = this.maxUpStep;
                this.maxUpStep = 1 + this.maxUpStep;
            }
            if (STARFALL.is(anim)) {
                this.gravityPre = this.isNoGravity();
                this.starfallCooldown = 240 + this.getRandom().nextInt(600);
                this.setNoGravity(true);
            }
        } else {
            if (this.prevStepHeight != -1) {
                this.maxUpStep = this.prevStepHeight;
                this.prevStepHeight = -1;
            }
            if (this.getAnimationHandler().isCurrent(STARFALL)) {
                this.setNoGravity(this.gravityPre);
            }
        }
    });

    private Vec3 chargeMotion, starFallPre, starFallPos;
    protected List<LivingEntity> hitEntity;
    private boolean teleported, gravityPre;
    private String previousAttack = "";
    private int starfallCooldown;
    private float prevStepHeight = -1;

    public EntitySarcophagus(EntityType<? extends EntitySarcophagus> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }

    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.SARCOPHAGUS_FIGHT.get());
    }

    @Override
    protected void applyAttributes() {
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.26);
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
        return (!this.getAnimationHandler().hasAnimation() || (!this.getAnimationHandler().isCurrent(DEFEAT, ANGRY) && !this.isTeleporting())) && super.hurt(source, amount);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        if (this.isTeleporting())
            return false;
        return super.shouldRender(x, y, z);
    }

    private boolean isTeleporting() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        if (TELEPORT.is(anim)) {
            if (anim.isPastTick(0.2) && !anim.isPastTick(0.44))
                return true;
            if (anim.isPastTick(1.2) && !anim.isPastTick(1.44))
                return true;
            return anim.isPastTick(2.2) && !anim.isPastTick(2.44);
        } else if (STARFALL.is(anim)) {
            return anim.isPastTick(0.2) && !anim.isPastTick(8.0);
        }
        return false;
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, TELEPORT, STARFALL))
            return;
        super.push(x, y, z);
    }

    @Override
    public int animationCooldown(AnimatedAction anim) {
        if (TELEPORT.is(anim))
            return 7 + this.getRandom().nextInt(6);
        return super.animationCooldown(anim);
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
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(CHARGE)) {
            return this.getBoundingBox().inflate(0.4, 0.1, 0.4);
        }
        return super.calculateAttackAABB(anim, target, grow);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 60.0f, 30.0f);
        }
        BiConsumer<AnimatedAction, EntitySarcophagus> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.FIRE_CIRCLE.get()))
                    this.getAnimationHandler().setAnimation(FIRE_CIRCLE);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.MISSILE_8X.get()))
                    this.getAnimationHandler().setAnimation(MISSILE);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.LIGHT_BEAM.get()))
                    this.getAnimationHandler().setAnimation(BEAM);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(LOCKED_YAW, 0f);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getAnimationHandler().isCurrent(CHARGE) && this.getAnimationHandler().getAnimation().isPastTick(0.28)) {
            this.setXRot(0);
            this.setYRot(this.entityData.get(LOCKED_YAW));
        }
        if (!this.level.isClientSide) {
            --this.starfallCooldown;
        }
    }

    @Override
    public void setEnraged(boolean flag, boolean load) {
        super.setEnraged(flag, load);
        if (flag && !load)
            this.getAnimationHandler().setAnimation(ANGRY);
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 29.5 / 16d, -8 / 16d);
    }

    @Override
    public AnimationHandler<EntitySarcophagus> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean allowAnimation(String prev, AnimatedAction other) {
        if (!this.teleported)
            return TELEPORT.is(other);
        return !TELEPORT.is(other) && (!this.previousAttack.equals(other.getID()));
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPosition;
    }

    private void teleportAround(int range, int yRange) {
        Vec3 pos;
        if (this.hasRestriction())
            pos = Vec3.atCenterOf(this.getRestrictCenter());
        else if (this.getTarget() != null)
            pos = this.getTarget().position();
        else
            pos = this.position();
        for (int i = 0; i < 10; i++) {
            double x = pos.x() + (this.getRandom().nextDouble() * 2 - 1) * range;
            double y = pos.y() + 4;
            double z = pos.z() + (this.getRandom().nextDouble() * 2 - 1) * range;
            if (this.hasRestriction() && this.getRestrictCenter().distToCenterSqr(x, y, z) > this.getRestrictRadius() * this.getRestrictRadius()) {
                continue;
            }
            if (this.teleport(x, y, z, yRange))
                return;
        }
    }

    private boolean teleport(double x, double y, double z, int yRange) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, y - 1, z);
        ChunkAccess chunk = this.level.getChunk(pos);
        while (yRange > 0 && pos.getY() > this.level.getMinBuildHeight() && !chunk.getBlockState(pos).getMaterial().blocksMotion()) {
            pos.move(Direction.DOWN);
            yRange--;
            y--;
        }
        BlockState blockState = chunk.getBlockState(pos);
        if (!blockState.getMaterial().blocksMotion()) {
            return false;
        }
        Vec3 current = this.position();
        this.teleportTo(x, y, z);
        if (!this.level.noCollision(this) || this.level.containsAnyLiquid(this.getBoundingBox())) {
            this.teleportTo(current.x(), current.y(), current.z());
            return false;
        }
        return true;
    }
}