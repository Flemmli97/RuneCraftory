package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTrickyMuck extends EntityBigMuck {

    public static final AnimatedAction SPORE_BALL = AnimatedAction.copyOf(SPORE, "spore_ball");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, SPORE, SPORE_BALL, INTERACT, SLEEP};
    private AnimationHandler<EntityBigMuck> animationHandler;

    public EntityTrickyMuck(EntityType<? extends EntityTrickyMuck> type, Level world) {
        super(type, world);
    }

    @Override
    public float attackChance(AnimationType type) {
        return 1;
    }

    @Override
    protected AnimationHandler<EntityBigMuck> getOrCreateAnimationHandler() {
        return this.animationHandler = new AnimationHandler<>(this, ANIMS);
    }

    @Override
    public AnimationHandler<EntityBigMuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public boolean isAnimOfType(AnimatedAction anim, AnimationType type) {
        if (type == AnimationType.MELEE && anim.getID().equals(SPORE_BALL.getID()))
            return true;
        return super.isAnimOfType(anim, type);
    }

    @Override
    public double maxAttackRange(AnimatedAction anim) {
        if (anim.getID().equals(SPORE.getID()))
            return 6;
        return super.maxAttackRange(anim);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.getID().equals(SPORE_BALL.getID())) {
            this.getNavigation().stop();
            if (anim.canAttack()) {
                if (EntityUtils.sealed(this))
                    return;
                ModSpells.POISON_BALL.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (command == 2)
                this.getAnimationHandler().setAnimation(SPORE_BALL);
            if (command == 1)
                this.getAnimationHandler().setAnimation(SPORE);
            else
                this.getAnimationHandler().setAnimation(SLAP);
        }
    }
}
