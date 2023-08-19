package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedMeleeGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class EntityScorpion extends BaseMonster {

    public static final AnimatedAction MELEE = new AnimatedAction(12, 6, "attack");
    public static final AnimatedAction INTERACT = AnimatedAction.copyOf(MELEE, "interact");
    public static final AnimatedAction STILL = AnimatedAction.builder(1, "still").infinite().build();
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{MELEE, INTERACT, STILL};

    public final AnimatedMeleeGoal<EntityScorpion> attack = new AnimatedMeleeGoal<>(this);
    private final AnimationHandler<EntityScorpion> animationHandler = new AnimationHandler<>(this, ANIMS);

    public EntityScorpion(EntityType<? extends EntityScorpion> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    protected void applyAttributes() {
        super.applyAttributes();
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        return type == AnimationType.MELEE && anim.is(MELEE);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        return 1.1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.9f;
    }

    @Override
    public AnimationHandler<EntityScorpion> getAnimationHandler() {
        return this.animationHandler;
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
