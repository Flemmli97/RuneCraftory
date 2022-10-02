package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.monster.ai.AnimatedMeleeGoal;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityOrc extends BaseMonster {

    private static final AnimatedAction melee1 = new AnimatedAction(22, 14, "attack_1");
    private static final AnimatedAction melee2 = new AnimatedAction(23, 13, "attack_2");
    public static final AnimatedAction interact = AnimatedAction.copyOf(melee1, "interact");
    private static final AnimatedAction[] anims = new AnimatedAction[]{melee1, melee2, interact};
    private final AnimationHandler<EntityOrc> animationHandler = new AnimationHandler<>(this, anims);
    public AnimatedMeleeGoal<EntityOrc> attack = new AnimatedMeleeGoal<>(this);

    public EntityOrc(EntityType<? extends EntityOrc> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 0.85f;
    }

    @Override
    public AnimationHandler<? extends EntityOrc> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE)
            return anim.getID().equals(melee1.getID()) || anim.getID().equals(melee2.getID());
        return false;
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(melee2.getID()))
            return 1.2;
        return 1.1;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (this.random.nextInt(2) == 0)
                this.getAnimationHandler().setAnimation(melee1);
            else
                this.getAnimationHandler().setAnimation(melee2);
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.85D;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(interact);
    }
}
