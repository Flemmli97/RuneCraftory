package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

public class TrickyMuck extends BigMuck {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder(BigMuck.BUILDER, SLAP, SPORE, INTERACT, SLEEP);
    public static final String SPORE_BALL = BUILDER.add("spore_ball", SPORE);
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<BigMuck> animationHandler = new AnimationHandler<>(this, ANIMS);

    public TrickyMuck(EntityType<? extends TrickyMuck> type, Level world) {
        super(type, world);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(SLAP).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(1)
                .start(SPORE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(2)
                .start(SPORE_BALL).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(4)
                .build();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(SPORE_BALL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.POISON_BALL.get().use(this);
            }
        } else
            super.handleAttack(anim);
    }

    @Override
    public AnimationHandler<BigMuck> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.POISON_BALL.get() : null))
                return;
            if (command == 2) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.POISON_BALL.get()))
                    this.getAnimationHandler().setAnimation(SPORE_BALL);
            }
            if (command == 1) {
                if (this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), RuneCraftorySpells.SPORE_CIRCLE_SPELL.get()))
                    this.getAnimationHandler().setAnimation(SPORE);
            } else
                this.getAnimationHandler().setAnimation(SLAP);
        }
    }
}
