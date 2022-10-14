package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.ChargeAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class EntityPanther extends ChargingMonster {

    private static final AnimatedAction melee = new AnimatedAction(16, 9, "attack");
    private static final AnimatedAction leap = new AnimatedAction(23, 6, "leap");
    public static final AnimatedAction interact = AnimatedAction.copyOf(melee, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee, leap, interact};
    public ChargeAttackGoal<EntityPanther> attack = new ChargeAttackGoal<>(this);
    protected List<LivingEntity> hitEntity;
    private final AnimationHandler<EntityPanther> animationHandler = new AnimationHandler<>(this, anims)
            .setAnimationChangeCons(a -> {
                if (!leap.checkID(a))
                    this.hitEntity = null;
            });

    public EntityPanther(EntityType<? extends EntityPanther> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityPanther> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(leap.getID())) {
            if (anim.canAttack()) {
                Vec3 target = this.getTarget() != null ? this.getTarget().position() : this.getLookAngle();
                Vec3 vec32 = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ())
                        .normalize().scale(1.35);
                this.setDeltaMovement(vec32.x, 0.25f, vec32.z);
            }
            if (anim.getTick() >= anim.getAttackTime()) {
                if (this.hitEntity == null)
                    this.hitEntity = new ArrayList<>();
                this.mobAttack(anim, null, e -> {
                    if (!this.hitEntity.contains(e)) {
                        this.hitEntity.add(e);
                        this.doHurtTarget(e);
                    }
                });
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee.getID());
        if (type == AnimationType.CHARGE)
            return anim.getID().equals(leap.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (leap.checkID(anim))
            return 2;
        return 1.4;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(leap);
            else
                this.getAnimationHandler().setAnimation(melee);
        }
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}
