package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.AttackBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.SelectableBehaviourBuilder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationDefinitionContainer;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationHandler;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationState;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimationsBuilder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StayWithinDistanceOfAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.function.Predicate;

public class Mage extends BaseMonster implements HealingPredicateEntity {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String SWING = BUILDER.add("swing", AnimationsBuilder.definition(0.64).marker("attack", 0.36));
    public static final String INTERACT = BUILDER.add("interact", SWING);
    public static final String CAST_1 = BUILDER.add("cast_1", AnimationsBuilder.definition(0.84).marker("attack", 0.4));
    public static final String CAST_DOUBLE = BUILDER.add("cast_double", AnimationsBuilder.definition(0.84).animationId(CAST_1).marker("attack", 0.4, 0.6));
    public static final String CAST_2 = BUILDER.add("cast_2", AnimationsBuilder.definition(0.92).marker("attack", 0.36));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Mage> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public Mage(EntityType<? extends Mage> type, Level level) {
        super(type, level);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(SWING).play(MonsterBehaviourUtils.requireInRangePlay())
                .condition(MonsterBehaviourUtils.inAABBRange(SWING))
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(3)
                .start(CAST_1).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(9).runFor(e -> e.getRandom().nextInt(15) + 10))
                .end(5)
                .start(CAST_DOUBLE).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(9).runFor(e -> e.getRandom().nextInt(15) + 10))
                .end(5)
                .start(CAST_2).play(MonsterBehaviourUtils.cooldownedPlay())
                .prepare(new StrafeTarget<BaseMonster>().strafeDistance(9).runFor(e -> e.getRandom().nextInt(15) + 10))
                .end(5)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(4, new StayWithinDistanceOfAttackTarget<BaseMonster>().maxDistance(12))
                .add(4, new StrafeTarget<>()).build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
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
                RuneCraftorySpells.DARK_BALL.get().use(this);
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
    public AnimationHandler<Mage> getAnimationHandler() {
        return this.animationHandler;
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

    public Spell getFirstSpell() {
        return RuneCraftorySpells.PARALYSIS_BALL.get();
    }

    public Spell getSecondSpell() {
        return RuneCraftorySpells.EXPANDING_QUAD_LIGHT.get();
    }

    @Override
    public Predicate<LivingEntity> healeableEntities() {
        return this.healingPredicate;
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