package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.api.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.api.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.MoveToTargetRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

import java.util.List;

public class EntityTrickyMuck extends EntityBigMuck {

    public static final AnimatedAction SPORE_BALL = AnimatedAction.copyOf(SPORE, "spore_ball");
    private static final AnimatedAction[] ANIMS = new AnimatedAction[]{SLAP, SPORE, SPORE_BALL, INTERACT, SLEEP};

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityTrickyMuck>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(SLAP, e -> 1), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeAction(SPORE, e -> 1), 2),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedEvadingAction(SPORE_BALL, 8, 3, 1, e -> 1), 2)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityTrickyMuck>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 1)), 2),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 4)), 1)
    );

    public final AnimatedAttackGoal<EntityTrickyMuck> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private AnimationHandler<EntityBigMuck> animationHandler;

    public EntityTrickyMuck(EntityType<? extends EntityTrickyMuck> type, Level world) {
        super(type, world);
        this.goalSelector.removeGoal(super.attack);
        this.goalSelector.addGoal(2, this.attack);
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
