package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetChargeTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

public class Beetle extends ChargingMonster {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String CHARGE_ATTACK = BUILDER.add("ramm", AnimationsBuilder.definition(1.52).marker("attack_start", 0.16));
    public static final String MELEE = BUILDER.add("attack", AnimationsBuilder.definition(0.72).marker("attack", 0.4));
    public static final String INTERACT = BUILDER.add("interact", MELEE);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Beetle> animationHandler = new AnimationHandler<>(this, ANIMS);

    public Beetle(EntityType<? extends Beetle> type, Level level) {
        super(type, level);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<ChargingMonster>create()
                .start(MELEE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(new MoveToAttackTarget<>())
                .end(1)
                .start(CHARGE_ATTACK).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetChargeTarget<>())
                .end(1)
                .start(CHARGE_ATTACK).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifFurtherThan(4))
                .prepare(new SetChargeTarget<>())
                .end(1)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(4, new SetWalkTargetToAttackTarget<>(), new MoveToWalkTarget<>())
                .add(1, new SetRandomWalkTarget<>(), new MoveToWalkTarget<>())
                .add(3, new Idle<>()).build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 2.6;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public AnimationHandler<Beetle> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    protected boolean isChargingAnim(String anim) {
        return anim.equals(CHARGE_ATTACK);
    }

    @Override
    public double chargingSpeed() {
        return 0.3;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), null))
                return;
            if (command == 1)
                this.getAnimationHandler().setAnimation(CHARGE_ATTACK);
            else
                this.getAnimationHandler().setAnimation(MELEE);
        }
    }

    @Override
    public String getInteractAnimation() {
        return INTERACT;
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }
}
