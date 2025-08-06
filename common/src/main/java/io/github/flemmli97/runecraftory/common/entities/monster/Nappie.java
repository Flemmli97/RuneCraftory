package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ChargingMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.SetChargeTarget;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetWalkTargetAwayFromTarget;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.function.Predicate;

public class Nappie extends PommePomme implements HealingPredicateEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder(PommePomme.BUILDER, KICK, CHARGE_ATTACK, INTERACT, SLEEP);
    public static final String HEAL = BUILDER.add("cast", AnimationsBuilder.definition(0.72).marker("attack", 0.32));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Nappie> animationHandler = new AnimationHandler<>(this, ANIMS);

    private final Predicate<LivingEntity> healingPredicate = e -> {
        if (this.getOwnerUUID() == null) {
            if (e instanceof OwnableEntity ownable && ownable.getOwnerUUID() != null)
                return false;
            return e instanceof Enemy && e != this.getTarget();
        }
        if (e instanceof OwnableEntity ownable && this.getOwnerUUID().equals(ownable.getOwnerUUID()))
            return true;
        return this.getOwnerUUID().equals(e.getUUID());
    };

    public Nappie(EntityType<? extends Nappie> type, Level level) {
        super(type, level);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<ChargingMonster>create()
                .start(KICK).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(HEAL).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetWalkTargetAwayFromTarget<ChargingMonster>().minDist(4)).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(CHARGE_ATTACK).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new SetChargeTarget<>())
                .end(2)
                .start(CHARGE_ATTACK).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifFurtherThan(5))
                .prepare(new SetChargeTarget<>())
                .end(3)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(2, new SetWalkTargetAwayFromTarget<>(), MonsterBehaviourUtils.moveTo())
                .add(3, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.CURE_ALL.get().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public AnimationHandler<Nappie> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }
}
