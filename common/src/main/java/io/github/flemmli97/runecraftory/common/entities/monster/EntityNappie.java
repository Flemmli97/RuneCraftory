package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

public class EntityNappie extends EntityPommePomme implements HealingPredicateEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String HEAL = BUILDER.add("cast", AnimationsBuilder.definition(0.72).marker("attack", 0.32));
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();
    //
//    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityPommePomme>>> ATTACKS = List.of(
//            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(KICK, e -> 1), 5),
//            WeightedEntry.wrap(new GoalAttackAction<EntityPommePomme>(HEAL)
//                    .cooldown(e -> e.animationCooldown(HEAL))
//                    .prepare(() -> new WrappedRunner<>(new MoveToTargetRunner<>(1, 12))), 1),
//            WeightedEntry.wrap(new GoalAttackAction<EntityPommePomme>(CHARGE_ATTACK)
//                    .cooldown(e -> e.animationCooldown(CHARGE_ATTACK))
//                    .withCondition(MonsterActionUtils.chargeCondition())
//                    .prepare(ChargeAction::new), 10)
//    );
//    private static final List<WeightedEntry.Wrapper<IdleAction<EntityPommePomme>>> IDLE_ACTIONS = List.of(
//            WeightedEntry.wrap(new IdleAction<>(() -> new MoveToTargetRunner<>(1, 0.5)), 3),
//            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(12, 5)), 5)
//    );
//
//    public final AnimatedAttackGoal<EntityPommePomme> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityNappie> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public EntityNappie(EntityType<? extends EntityNappie> type, Level world) {
        super(type, world);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.CURE_ALL.get().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public AnimationHandler<EntityNappie> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }
}
