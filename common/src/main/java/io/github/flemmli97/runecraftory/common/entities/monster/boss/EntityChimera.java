package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.MobAttackExt;
import io.github.flemmli97.runecraftory.common.entities.RunecraftoryBossbar;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MoveToTargetAttackRunner;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveAwayRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.TimedWrappedRunner;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EntityChimera extends BossMonster implements MobAttackExt {

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityChimera.class, EntityDataSerializers.FLOAT);

    public static final AnimatedAction LEAP = new AnimatedAction(1.36, "leap");
    public static final AnimatedAction FIRE_TAIL_BUBBLE = new AnimatedAction(1.48, 0.44, "tail_beam");
    public static final AnimatedAction WATER_TAIL_BUBBLE = AnimatedAction.copyOf(FIRE_TAIL_BUBBLE, "water_tail_bubble");
    public static final AnimatedAction WATER_TAIL_BEAM = AnimatedAction.copyOf(FIRE_TAIL_BUBBLE, "water_tail_beam");
    public static final AnimatedAction FIRE_BREATH = new AnimatedAction(1.2, 0.4, "breath_attack");
    public static final AnimatedAction BUBBLE_BEAM = AnimatedAction.copyOf(FIRE_BREATH, "bubble_beam");
    public static final AnimatedAction SLASH = new AnimatedAction(0.64, 0.36, "claw_attack");
    public static final AnimatedAction BITE = new AnimatedAction(1.04, 0.4, "bite_attack");
    public static final AnimatedAction ANGRY = new AnimatedAction(1.04, "angry");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(4, "sleep").infinite().build();
    public static final AnimatedAction DEFEAT = AnimatedAction.builder(80, "defeat").marker(60).infinite().build();
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(SLASH, "interact");
    private static final AnimatedAction[] ANIMATED_ACTIONS = new AnimatedAction[]{LEAP, FIRE_TAIL_BUBBLE, WATER_TAIL_BUBBLE, WATER_TAIL_BEAM, FIRE_BREATH, BUBBLE_BEAM, SLASH, BITE, DEFEAT, INTERACT, ANGRY, SLEEP};

    private static final ImmutableMap<String, BiConsumer<AnimatedAction, EntityChimera>> ATTACK_HANDLER = createAnimationHandler(b -> {
        BiConsumer<AnimatedAction, EntityChimera> summonFire = (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.FIREBALL_BARRAGE.get().use(entity);
            }
        };
        b.put(FIRE_TAIL_BUBBLE, summonFire);
        b.put(FIRE_BREATH, summonFire);
        BiConsumer<AnimatedAction, EntityChimera> summonWater = (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.BUBBLE_BEAM.get().use(entity);
            }
        };
        b.put(BUBBLE_BEAM, summonWater);
        b.put(WATER_TAIL_BUBBLE, summonWater);
        b.put(WATER_TAIL_BEAM, (anim, entity) -> {
            if (anim.canAttack()) {
                ModSpells.WATER_LASER.get().use(entity);
            }
        });
        b.put(LEAP, (anim, entity) -> {
            if ((anim.getTick() < anim.getLength() - 3 && anim.getTick() > anim.getAttackTime())) {
                if (entity.hitEntity == null)
                    entity.hitEntity = new ArrayList<>();
                if (entity.chargeMotion != null) {
                    entity.setDeltaMovement(entity.chargeMotion[0], entity.getDeltaMovement().y, entity.chargeMotion[2]);
                }
                entity.mobAttack(anim, null, e -> {
                    if (!entity.hitEntity.contains(e) && CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knock(CustomDamage.KnockBackType.UP), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE))) {
                        entity.chargeAttackSuccess = true;
                        entity.hitEntity.add(e);
                    }
                });
            }
        });
        b.put(SLASH, (anim, entity) -> {
            if (anim.getTick() == anim.getAttackTime() || anim.getTick() == 17) {
                ModSpells.SLASH.get().use(entity);
            }
        });
        b.put(BITE, (anim, entity) -> {
            if (anim.getTick() == 1 && entity.getTarget() != null) {
                entity.targetPosition = entity.getTarget().position();
            }
            if (anim.getTick() == anim.getAttackTime()) {
                entity.mobAttack(anim, entity.getTarget(), e -> CombatUtils.mobAttack(entity, e,
                        new CustomDamage.Builder(entity).hurtResistant(5).knockAmount(0), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE)));
            } else if (anim.getTick() == 17) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
    });

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityChimera>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(LEAP)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 4), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(FIRE_TAIL_BUBBLE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(WATER_TAIL_BUBBLE)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(WATER_TAIL_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(FIRE_BREATH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(BUBBLE_BEAM)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetRunner<>(1.1, 6), e -> 30 + e.getRandom().nextInt(15))), 1),
            WeightedEntry.wrap(MonsterActionUtils.<EntityChimera>nonRepeatableAttack(SLASH)
                    .prepare(() -> new TimedWrappedRunner<>(new MoveToTargetAttackRunner<>(1.1), e -> 30 + e.getRandom().nextInt(15))), 1)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityChimera>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1.1, 1)), 1),
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveAwayRunner<>(4, 1, 6)), 1)
    );

    public final AnimatedAttackGoal<EntityChimera> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityChimera> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .setAnimationChangeFunc(anim -> {
                if (!this.level.isClientSide) {
                    if (anim == null) {
                        this.chargeMotion = null;
                    } else if (anim.is(LEAP)) {
                        if (this.isVehicle()) {
                            this.lockYaw(this.getControllingPassenger().getYHeadRot());
                            this.setChargeMotion(this.getChargeTo(anim, this.position().add(this.getControllingPassenger().getLookAngle())));
                        } else if (this.getTarget() != null) {
                            LivingEntity target = this.getTarget();
                            this.setChargeMotion(this.getChargeTo(anim, target.position().subtract(this.position())));
                            this.lookAt(target, 360, 10);
                            this.lockYaw(this.getYRot());
                        }
                    } else if (anim.is(FIRE_TAIL_BUBBLE, WATER_TAIL_BUBBLE, WATER_TAIL_BEAM, FIRE_BREATH, BUBBLE_BEAM)) {
                        if (this.getTarget() != null)
                            this.targetPosition = EntityUtil.getStraightProjectileTarget(this.getEyePosition(), this.getTarget());
                    }
                    if (this.getAnimationHandler().isCurrent(LEAP)) {
                        this.hitEntity = null;
                        if (this.chargeAttackSuccess) {
                            this.chargeAttackSuccess = false;
                            this.getAnimationHandler().setAnimation(BITE);
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            });

    protected boolean chargeAttackSuccess;
    private double[] chargeMotion;
    protected List<LivingEntity> hitEntity;

    public EntityChimera(EntityType<? extends EntityChimera> type, Level world) {
        super(type, world);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
    }


    @Override
    public RunecraftoryBossbar createBossBar() {
        return new RunecraftoryBossbar(null, this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS)
                .setMusic(ModSounds.CHIMERA_FIGHT.get());
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
        return (!this.getAnimationHandler().hasAnimation() || !(this.getAnimationHandler().isCurrent(DEFEAT, ANGRY))) && super.hurt(source, amount);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.getAnimationHandler().isCurrent(ANGRY, DEFEAT);
    }

    @Override
    public void push(double x, double y, double z) {
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT, LEAP))
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
    public AABB calculateAttackAABB(AnimatedAction anim, Vec3 target, double grow) {
        if (anim.is(LEAP)) {
            return this.getBoundingBox().inflate(0.3, 0.1, 0.3);
        }
        if (!anim.is(BITE)) {
            return super.calculateAttackAABB(anim, target, grow);
        }
        double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth() * 0.5;
        Vec3 dir;
        float offset = anim.canAttack() ? 10 : -10;
        if (target != null && !this.canBeControlledByRider()) {
            reach = Math.min(reach, this.position().distanceTo(target));
            dir = MathUtils.rotate(MathUtils.normalY, target.subtract(this.position()).normalize(), offset * Mth.DEG_TO_RAD);
        } else {
            if (this.getControllingPassenger() instanceof Player player)
                dir = Vec3.directionFromRotation(player.getXRot(), player.getYRot() + offset);
            else
                dir = Vec3.directionFromRotation(this.getXRot(), this.getYRot() + offset);
        }
        Vec3 attackPos = this.position().add(dir.scale(reach));
        return this.attackAABB(anim).inflate(grow, 0, grow).move(attackPos.x, attackPos.y, attackPos.z);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        LivingEntity target = this.getTarget();
        if (target != null) {
            this.getNavigation().stop();
            this.getLookControl().setLookAt(target, 30.0f, 30.0f);
        }
        BiConsumer<AnimatedAction, EntityChimera> handler = ATTACK_HANDLER.get(anim.getID());
        if (handler != null)
            handler.accept(anim, this);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.BUBBLE_BEAM.get()))
                    this.getAnimationHandler().setAnimation(BUBBLE_BEAM);
            } else if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SLASH.get()))
                    this.getAnimationHandler().setAnimation(SLASH);
            } else {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                    this.getAnimationHandler().setAnimation(LEAP);
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_CHIMERA_AMBIENT.get();
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
        if (this.getAnimationHandler().isCurrent(LEAP, FIRE_BREATH, FIRE_TAIL_BUBBLE, BUBBLE_BEAM, WATER_TAIL_BUBBLE, WATER_TAIL_BUBBLE)) {
            this.setXRot(0);
            this.setYRot(this.entityData.get(LOCKED_YAW));
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
        return new Vec3(0, 22.75 / 16d, -5 / 16d);
    }

    public void setChargeMotion(double[] charge) {
        this.chargeMotion = charge;
    }

    public double[] getChargeTo(AnimatedAction anim, Vec3 dir) {
        int length = anim.getLength() - anim.getAttackTime() - 4;
        Vec3 vec = dir.normalize().scale(4.5);
        return new double[]{vec.x / length, this.getY(), vec.z / length};
    }

    @Override
    public AnimationHandler<EntityChimera> getAnimationHandler() {
        return this.animationHandler;
    }

    public void lockYaw(float yaw) {
        this.entityData.set(LOCKED_YAW, yaw);
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return SLEEP;
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.targetPosition;
    }
}