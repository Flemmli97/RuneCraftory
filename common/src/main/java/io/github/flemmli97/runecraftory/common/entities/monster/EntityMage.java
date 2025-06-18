package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.animated.MonsterActionUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.tenshilib.common.entity.AnimatedAction;
import io.github.flemmli97.tenshilib.common.entity.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.AnimatedAttackGoal;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.GoalAttackAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.IdleAction;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.KeepDistanceRunner;
import io.github.flemmli97.tenshilib.common.entity.ai.animated.impl.RandomMoveAroundRunner;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Predicate;

public class EntityMage extends BaseMonster implements HealingPredicateEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SWING = BUILDER.add("swing", AnimationsBuilder.definition(0.64).marker("attack", 0.36));
    public static final String CAST_1 = BUILDER.add("cast_1", AnimationsBuilder.definition(0.84).marker("attack", 0.4));
    public static final String CAST_DOUBLE = BUILDER.add("cast_double", AnimationsBuilder.definition(0.84).marker("attack", 0.4, 0.6));
    public static final String CAST_2 = BUILDER.add("cast_2", AnimationsBuilder.definition(0.92).marker("attack", 0.36));
    public static final String INTERACT = BUILDER.add("interact", SWING);
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private static final List<WeightedEntry.Wrapper<GoalAttackAction<EntityMage>>> ATTACKS = List.of(
            WeightedEntry.wrap(MonsterActionUtils.simpleMeleeActionInRange(SWING, e -> 0.8f), 1),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedStrafingAction(CAST_1, 6, 1, e -> 1), 3),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedStrafingAction(CAST_DOUBLE, 7, 1, e -> 1), 2),
            WeightedEntry.wrap(MonsterActionUtils.simpleRangedStrafingAction(CAST_2, 8, 1, e -> 1), 4)
    );
    private static final List<WeightedEntry.Wrapper<IdleAction<EntityMage>>> IDLE_ACTIONS = List.of(
            WeightedEntry.wrap(new IdleAction<>(() -> new KeepDistanceRunner<>(4, 10, 1)), 3),
            WeightedEntry.wrap(new IdleAction<>(() -> new RandomMoveAroundRunner<>(16, 5)), 1),
            WeightedEntry.wrap(new IdleAction<>(DoNothingRunner::new), 2)
    );

    public final AnimatedAttackGoal<EntityMage> attack = new AnimatedAttackGoal<>(this, ATTACKS, IDLE_ACTIONS);
    private final AnimationHandler<EntityMage> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public EntityMage(EntityType<? extends EntityMage> type, Level world) {
        super(type, world);
        this.goalSelector.addGoal(2, this.attack);
    }

    @Override
    public AABB attackBB(String anim) {
        double width = this.getBbWidth() * 1.4;
        double length = this.getBbWidth() * 2.1;
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(CAST_1)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.getFirstSpell().use(this);
            }
        } else if (anim.is(CAST_DOUBLE)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                ModSpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(CAST_2)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                this.getSecondSpell().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            Spell spell = switch (command) {
                case 2 -> this.getSecondSpell();
                case 1 -> this.getFirstSpell();
                default -> null;
            };
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), spell))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(CAST_2);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(CAST_1);
            else
                this.getAnimationHandler().setAnimation(SWING);
        }
    }

    @Override
    public AnimationHandler<EntityMage> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
    }

    @Override
    public void playInteractionAnimation() {
        this.getAnimationHandler().setAnimation(INTERACT);
    }

    @Override
    public String getSleepAnimation() {
        return SLEEP;
    }

    public Spell getFirstSpell() {
        return ModSpells.PARALYSIS_BALL.get();
    }

    public Spell getSecondSpell() {
        return ModSpells.EXPANDING_QUAD_LIGHT.get();
    }

    @Override
    public Vec3 passengerOffset(Entity passenger) {
        return new Vec3(0, 13.5 / 16d, -5 / 16d);
    }
}