package io.github.flemmli97.runecraftory.common.entities.monster.boss;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BossMonster;
import io.github.flemmli97.runecraftory.common.entities.DelayedAttacker;
import io.github.flemmli97.runecraftory.common.entities.ai.RestrictedWaterAvoidingStrollGoal;
import io.github.flemmli97.runecraftory.common.entities.ai.boss.ChimeraAttackGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.CombatUtils;
import io.github.flemmli97.runecraftory.common.utils.CustomDamage;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.utils.MathUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
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
import java.util.function.Function;

public class EntityChimera extends BossMonster implements DelayedAttacker {

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
                        entity.attack.chargeSuccess(0);
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
            if (anim.getTick() == anim.getAttackTime()) {
                entity.mobAttack(anim, entity.getTarget(), e -> {
                    CombatUtils.mobAttack(entity, e,
                            new CustomDamage.Builder(entity).hurtResistant(5).knockAmount(0), CombatUtils.getAttributeValue(entity, Attributes.ATTACK_DAMAGE));
                });
            } else if (anim.getTick() == 17) {
                entity.mobAttack(anim, entity.getTarget(), entity::doHurtTarget);
            }
        });
    });

    private static final EntityDataAccessor<Float> LOCKED_YAW = SynchedEntityData.defineId(EntityChimera.class, EntityDataSerializers.FLOAT);

    public final ChimeraAttackGoal<EntityChimera> attack = new ChimeraAttackGoal<>(this);
    private final AnimationHandler<EntityChimera> animationHandler = new AnimationHandler<>(this, ANIMATED_ACTIONS)
            .setAnimationChangeCons(anim -> {
                if (!this.level.isClientSide) {
                    if (this.chargeAttackSuccess && anim != null)
                        this.chargeAttackSuccess = false;
                    if (this.getAnimationHandler().isCurrent(LEAP))
                        this.hitEntity = null;
                }
            });
    protected boolean chargeAttackSuccess;
    private double[] chargeMotion;
    protected List<LivingEntity> hitEntity;
    private Function<Vec3, Vec3> aiTargetPosition;

    public EntityChimera(EntityType<? extends EntityChimera> type, Level world) {
        super(type, world);
        this.bossInfo.setColor(BossEvent.BossBarColor.BLUE);
        if (!world.isClientSide)
            this.goalSelector.addGoal(1, this.attack);
        this.maxUpStep = 1;
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
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (anim.is(DEFEAT, INTERACT, SLEEP, ANGRY))
            return false;
        if (type == AnimationType.GENERICATTACK) {
            if (this.chargeAttackSuccess)
                return anim.is(BITE);
            return true;
        }
        return false;
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
        if (this.getAnimationHandler().isCurrent(ANGRY, DEFEAT))
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
    public AABB calculateAttackAABB(AnimatedAction anim, LivingEntity target, double grow) {
        if (!anim.is(BITE)) {
            return super.calculateAttackAABB(anim, target, grow);
        }
        double reach = this.maxAttackRange(anim) * 0.5 + this.getBbWidth() * 0.5;
        Vec3 dir;
        float offset = anim.canAttack() ? 10 : -10;
        if (target != null && !this.canBeControlledByRider()) {
            reach = Math.min(reach, this.distanceTo(target));
            dir = MathUtils.rotate(MathUtils.normalY, target.position().subtract(this.position()).normalize(), offset * Mth.DEG_TO_RAD);
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
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.825D;
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
    public float attackChance(AnimationType type) {
        return 1;
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

    public void setAiTarget(Function<Vec3, Vec3> aiVarHelper) {
        this.aiTargetPosition = aiVarHelper;
    }

    @Override
    public Vec3 targetPosition(Vec3 from) {
        return this.aiTargetPosition.apply(from);
    }
}