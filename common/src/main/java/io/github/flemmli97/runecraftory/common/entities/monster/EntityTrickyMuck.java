package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.AnimationType;
import io.github.flemmli97.runecraftory.common.entities.ai.AnimatedRangedGoal;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class EntityTrickyMuck extends EntityBigMuck {

    public static final AnimatedAction SPORE_BALL = AnimatedAction.copyOf(SPORE, "spore_ball");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, SPORE, SPORE_BALL, INTERACT, SLEEP};

    public final AnimatedRangedGoal<EntityBigMuck> ranged = new AnimatedRangedGoal<>(this, 8, false, e -> true);
    private AnimationHandler<EntityBigMuck> animationHandler;

    public EntityTrickyMuck(EntityType<? extends EntityTrickyMuck> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(this.ai);
        this.goalSelector.addGoal(2, this.ranged);
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
        if (type == AnimationType.RANGED && anim.is(SPORE_BALL))
            return true;
        return super.isAnimOfType(anim, type);
    }

    @Override
    public void handleAttack(AnimatedAction anim) {
        if (anim.is(SPORE_BALL)) {
            this.getNavigation().stop();
            if (anim.getTick() == 1 && this.getTarget() != null)
                this.lookAt(this.getTarget(), 360, 90);
            if (anim.canAttack()) {
                ModSpells.POISON_BALL.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? ModSpells.POISON_BALL.get() : null))
                return;
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.POISON_BALL.get()))
                    this.getAnimationHandler().setAnimation(SPORE_BALL);
            }
            if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), ModSpells.SPORE_CIRCLE_SPELL.get()))
                    this.getAnimationHandler().setAnimation(SPORE);
            } else
                this.getAnimationHandler().setAnimation(SLAP);
        }
    }
}
