package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EntityDuck extends ChargingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(15, 8, "slap");
    private static final AnimatedAction DIVE = new AnimatedAction(48, 22, "dive");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, DIVE, INTERACT, STILL};

    public ChargeAttackGoal<EntityDuck> attack = new ChargeAttackGoal<>(this);
    private final AnimationHandler<EntityDuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityDuck(EntityType<? extends EntityDuck> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.8f;
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityDuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(DIVE)) {
            if (anim.canAttack()) {
                Vec3 targetDir = this.chargeMotion != null ? new Vec3(this.chargeMotion[0], this.chargeMotion[1], this.chargeMotion[2]) : this.getLookAngle();
                Vec3 vec32 = new Vec3(targetDir.x, 0, targetDir.z)
                        .normalize().scale(0.9);
                this.setDeltaMovement(vec32.x, -0.35f, vec32.z);
                this.getNavigation().stop();
            }
            if (anim.getTick() >= anim.getAttackTime() && !this.onGround) {
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            } else {
                Vec3 delta = this.getDeltaMovement();
                this.setDeltaMovement(delta.x, 0.1f, delta.z);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public float chargingYaw() {
        AnimatedAction anim = this.getAnimationHandler().getAnimation();
        return this.isVehicle() || ((anim != null && anim.is(DIVE) && anim.getTick() >= anim.getAttackTime())) ? this.getYRot() : this.entityData.get(LOCKED_YAW);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.is(MELEE);
        if (type == AnimationType.CHARGE)
            return anim.is(DIVE);
        return false;
    }

    @Override
    public double[] getChargeTo(AnimatedAction anim, Vec3 pos) {
        Vec3 vec = pos.subtract(this.position());
        return new double[]{vec.x, vec.y, vec.z};
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.15;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(DIVE);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public AnimatedAction getSleepAnimation() {
        return STILL;
    }
}
