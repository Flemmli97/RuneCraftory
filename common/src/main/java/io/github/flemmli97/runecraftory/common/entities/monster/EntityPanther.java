package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.LeapingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.LeapingAttackGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EntityPanther extends LeapingMonster {

    private static final AnimatedAction MELEE = new AnimatedAction(16, 9, "attack");
    private static final AnimatedAction LEAP = new AnimatedAction(23, 6, "leap");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction SLEEP = AnimatedAction.builder(1, "sleep").infinite().changeDelay(AnimationHandler.DEFAULT_ADJUST_TIME).build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, LEAP, INTERACT, SLEEP};

    public LeapingAttackGoal<EntityPanther> attack = new LeapingAttackGoal<>(this);
    private final AnimationHandler<EntityPanther> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityPanther(EntityType<? extends EntityPanther> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        if (type == AnimationType.MELEE)
            return 0.8f;
        return 1;
    }

    @Override
    public AnimationHandler<? extends EntityPanther> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(LEAP)) {
            if (anim.canAttack()) {
                Vec3 vec32;
                if (this.getTarget() != null) {
                    Vec3 target = this.getTarget().position();
                    vec32 = new Vec3(target.x - this.getX(), 0.0, target.z - this.getZ())
                            .normalize().scale(1.35);
                } else
                    vec32 = this.getLookAngle();
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
    public Vec3 getLeapVec(@Nullable LivingEntity target) {
        return super.getLeapVec(target).scale(1.15);
    }

    @Override
    public double leapHeightMotion() {
        return 0.25f;
    }

    @Override
    public float maxLeapDistance() {
        return 4;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.is(MELEE);
        if (type == AnimationType.LEAP)
            return anim.is(LEAP);
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (LEAP.is(anim))
            return 2;
        return 1.4;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 1)
                this.getAnimationHandler().setAnimation(LEAP);
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
        return SLEEP;
    }
}
