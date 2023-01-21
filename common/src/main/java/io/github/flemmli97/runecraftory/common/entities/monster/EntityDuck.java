package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class EntityDuck extends ChargingMonster {

    private static final AnimatedAction melee = new AnimatedAction(15, 8, "slap");
    private static final AnimatedAction dive = new AnimatedAction(48, 22, "dive");
    public static final AnimatedAction interact = AnimatedAction.copyOf(melee, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, dive, interact};
    public ChargeAttackGoal<EntityDuck> attack = new ChargeAttackGoal<>(this);
    protected List<LivingEntity> hitEntity = new ArrayList<>();
    private final AnimationHandler<EntityDuck> animationHandler = new AnimationHandler<>(this, anims);

    public EntityDuck(EntityType<? extends EntityDuck> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected Consumer<AnimatedAction> animatedActionConsumer() {
        return a -> {
            super.animatedActionConsumer().accept(a);
            if (!dive.checkID(a)) {
                this.hitEntity.clear();
            }
        };
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityDuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(dive.getID())) {
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
        return this.isVehicle() || ((anim != null && anim.getID().equals(dive.getID()) && anim.getTick() >= anim.getAttackTime())) ? this.getYRot() : this.entityData.get(LOCKED_YAW);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(dive.getID());
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
        return 1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(dive);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}
