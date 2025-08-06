package io.github.flemmli97.runecraftory.common.entities.monster;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.ai.behaviour.MonsterBehaviourUtils;
import io.github.flemmli97.runecraftory.common.entities.utils.ElementalAttackMob;
import io.github.flemmli97.runecraftory.common.entities.utils.HealingPredicateEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
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
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;

import java.util.function.Predicate;

public class Demon extends BaseMonster implements HealingPredicateEntity, ElementalAttackMob {

    public static final AnimationsBuilder BUILDER = new AnimationsBuilder();
    public static final String DARK = BUILDER.add("cast", AnimationsBuilder.definition(0.88).marker("attack", 0.52));
    public static final String HEAL = BUILDER.add("heal", DARK);
    public static final String INTERACT = BUILDER.add("interact", DARK);
    public static final String STAB = BUILDER.add("stab", AnimationsBuilder.definition(0.68).marker("attack", 0.4));
    public static final String STAB_LONG = BUILDER.add("stab_long", AnimationsBuilder.definition(0.8).marker("attack", 0.48));
    public static final String SWIPE = BUILDER.add("swipe", AnimationsBuilder.definition(0.88).marker("attack", 0.44));
    public static final String SLEEP = BUILDER.add("sleep", AnimationsBuilder.definition(0).infinite());
    public static final AnimationDefinitionContainer ANIMS = BUILDER.build();

    private final AnimationHandler<Demon> animationHandler = new AnimationHandler<>(this, ANIMS);

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

    public Demon(EntityType<? extends Demon> type, Level level) {
        super(type, level);
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCombatAI() {
        return AttackBehaviourBuilder.<BaseMonster>create()
                .start(STAB).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(4)
                .start(STAB_LONG).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(SWIPE).play(MonsterBehaviourUtils.requireInRangePlay())
                .prepare(new SetWalkTargetToAttackTarget<>()).prepareOptional(MonsterBehaviourUtils.moveAttack())
                .end(5)
                .start(DARK).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(3)
                .start(DARK).play(MonsterBehaviourUtils.cooldownedPlay())
                .condition(MonsterBehaviourUtils.ifFurtherThan(4))
                .end(5)
                .start(HEAL).play(MonsterBehaviourUtils.cooldownedPlay())
                .end(2)
                .build();
    }

    @Override
    public ExtendedBehaviour<? extends BaseMonster> getCooldownAI() {
        return SelectableBehaviourBuilder.<BaseMonster>builder()
                .add(4, new SetWalkTargetToAttackTarget<>(), MonsterBehaviourUtils.moveTo())
                .add(3, new SetRandomWalkTarget<>(), MonsterBehaviourUtils.moveTo()).build();
    }

    @Override
    public AABB attackBB(AnimationState anim) {
        double width = this.getBbWidth() * 1;
        double length = width;
        if (anim.is(STAB)) {
            width = this.getBbWidth() * 1.8;
            length = this.getBbWidth() * 4.5;
        } else if (anim.is(STAB_LONG)) {
            width = this.getBbWidth() * 1.8;
            length = this.getBbWidth() * 5.5;
        } else if (anim.is(SWIPE)) {
            width = this.getBbWidth() * 4.35;
            length = this.getBbWidth() * 3.6;
        }
        return new AABB(-width * 0.5, -0.02, 0, width * 0.5, this.getBbHeight() + 0.02, length);
    }

    @Override
    public void handleAttack(AnimationState anim) {
        if (anim.is(DARK)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.DARK_BALL.get().use(this);
            }
        } else if (anim.is(HEAL)) {
            this.getNavigation().stop();
            if (anim.isAt("attack")) {
                RuneCraftorySpells.CURE_ALL.get().use(this);
            }
        } else {
            super.handleAttack(anim);
        }
    }

    @Override
    public AnimationHandler<Demon> getAnimationHandler() {
        return this.animationHandler;
    }

    @Override
    public void handleRidingCommand(int command) {
        if (!this.getAnimationHandler().hasAnimation()) {
            if (!this.getProp().rideActionCosts.canRun(command, this.getControllingPassenger(), command == 2 ? RuneCraftorySpells.DARK_BALL.get() : null))
                return;
            if (command == 2)
                this.getAnimationHandler().setAnimation(DARK);
            else if (command == 1)
                this.getAnimationHandler().setAnimation(SWIPE);
            else
                this.getAnimationHandler().setAnimation(STAB);
        }
    }

    @Override
    public ItemElement getAttackElement() {
        return ItemElement.DARK;
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