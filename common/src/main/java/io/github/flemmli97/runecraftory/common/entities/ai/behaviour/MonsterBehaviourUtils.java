package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.tenshilib.common.entity.AOEAttackEntity;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToAttackTarget;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.MoveToWalkTargetWithSight;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetAnimationToPlay;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.animated.AnimatedEntity;
import io.github.flemmli97.tenshilib.common.registry.TenshilibMemoryModules;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntBiFunction;

public class MonsterBehaviourUtils {

    public static <E extends BaseMonster> Predicate<E> chancedStart(Supplier<Float> floats) {
        return entity -> {
            if (entity.getRandom().nextFloat() < floats.get()) {
                return true;
            }
            if (!BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN)) {
                int cooldown = entity.animationCooldown(null);
                BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
            }
            return false;
        };
    }

    public static <E extends BaseMonster> SetAnimationToPlay<E> checkedAttack(String... animations) {
        return new SetAnimationToPlay<E>(animations).filter((animation, entity) -> {
            String previous = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.LAST_ANIMATION.get());
            return entity.allowAnimation(previous, animation);
        });
    }

    @SafeVarargs
    public static <E extends BaseMonster> SetAnimationToPlay<E> checkedAttack(AnimationPlayHolder<E>... animations) {
        return new SetAnimationToPlay<>(animations).filter((animation, entity) -> {
            String previous = BrainUtils.getMemory(entity, RuneCraftoryMemoryTypes.LAST_ANIMATION.get());
            return entity.allowAnimation(previous, animation);
        });
    }

    public static <E extends BaseMonster> PlayAnimation<E> cooldownedPlay() {
        return cooldownedPlay(null);
    }

    public static <E extends BaseMonster> PlayAnimation<E> requireInRangePlay() {
        return cooldownedPlay(entity -> {
            AnimationPlayHolder<?> anim = BrainUtils.getMemory(entity, TenshilibMemoryModules.ANIMATION_TO_PLAY.get());
            Entity target = BrainUtils.getTargetOfEntity(entity);
            return target != null && entity.isInAttackBox(target, anim.animation());
        });
    }

    public static <E extends BaseMonster> PlayAnimation<E> cooldownedPlay(Predicate<E> condition) {
        PlayAnimation<E> behaviour = new PlayAnimation<E>().withCallback(cooldownHandler())
                .withCallback(cooldownHandlerCont());
        if (condition != null) {
            behaviour.startCondition(condition);
        }
        return behaviour;
    }

    public static <E extends BaseMonster> PlayAnimation.OnStart<E> cooldownHandler() {
        return (animation, entity) -> {
            double calc = entity.animationCooldown(animation);
            calc += entity.getAnimationHandler().get(animation).length();
            int cooldown = Mth.ceil(calc);
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
            BrainUtils.setForgettableMemory(entity, RuneCraftoryMemoryTypes.LAST_ANIMATION.get(), animation, cooldown + 80);
        };
    }

    public static <E extends BaseMonster> PlayAnimation.OnContinue<E> cooldownHandlerCont() {
        return (animation, chains, entity) -> {
            // Extend cooldown by chained attacks
            double calc = BrainUtils.getTimeUntilMemoryExpires(entity, MemoryModuleType.ATTACK_COOLING_DOWN);
            if (chains != null) {
                for (AnimationPlayHolder.AnimationHolder chain : chains) {
                    calc += entity.getAnimationHandler().get(chain.animation()).length();
                }
                int cooldown = Mth.ceil(calc);
                BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
                BrainUtils.setForgettableMemory(entity, RuneCraftoryMemoryTypes.LAST_ANIMATION.get(), animation, cooldown);
            }
        };
    }

    public static <T extends BaseMonster> Predicate<T> inAABBRange(String animation) {
        return entity -> {
            Entity target = BrainUtils.getTargetOfEntity(entity);
            return target != null && entity.isInAttackBox(target, animation);
        };
    }

    public static <E extends LivingEntity> ToIntBiFunction<E, LivingEntity> closeEnough(int dist) {
        return (entity, target) -> dist;
    }

    public static <E extends Mob> Predicate<E> ifCloserThan(double dist) {
        return entity -> {
            LivingEntity target = entity.getTarget();
            if (target == null)
                return false;
            double distance = dist + entity.getBbWidth() * 0.5 + target.getBbWidth() * 0.5;
            return entity.distanceToSqr(target) <= distance * distance;
        };
    }

    public static <E extends Mob> Predicate<E> ifFurtherThan(double dist) {
        return entity -> {
            LivingEntity target = entity.getTarget();
            if (target == null)
                return false;
            double distance = dist + entity.getBbWidth() * 0.5 + target.getBbWidth() * 0.5;
            return entity.distanceToSqr(target) >= distance * distance;
        };
    }

    public static <E extends LivingEntity> Consumer<ExtendedBehaviour<E>> withCondition(Predicate<E> test) {
        return behaviour -> {
            behaviour.startCondition(test);
            behaviour.stopIf(e -> !test.test(e));
        };
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> timedMovement() {
        return timedMovement(30, 45);
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> timedMovement(int min, int max) {
        MoveToAttackTarget<E> behaviour = moveAttack();
        behaviour.runFor(e -> min + e.getRandom().nextInt(max - min));
        return behaviour;
    }

    public static <E extends PathfinderMob & AOEAttackEntity & AnimatedEntity> MoveToAttackTarget<E> moveAttack() {
        MoveToAttackTarget<E> behaviour = new MoveToAttackTarget<>();
        behaviour.runFor(e -> 100);
        return behaviour;
    }

    public static <E extends PathfinderMob> MoveToWalkTargetWithSight<E> moveTo() {
        return new MoveToWalkTargetWithSight<>();
    }

    @SafeVarargs
    public static <T> Predicate<T> and(Predicate<T>... and) {
        return t -> {
            for (Predicate<T> pred : and) {
                if (!pred.test(t))
                    return false;
            }
            return true;
        };
    }
}
