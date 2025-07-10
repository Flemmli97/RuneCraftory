package io.github.flemmli97.runecraftory.common.entities.ai.behaviour;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModMemoryTypes;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.PlayAnimation;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.behaviour.SetAnimationToPlay;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.data.AnimationPlayHolder;
import io.github.flemmli97.tenshilib.common.entity.ai.brain.memory.MoreMemoryModules;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.util.BrainUtils;

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
            String previous = BrainUtils.getMemory(entity, ModMemoryTypes.LAST_ANIMATION.get());
            return entity.allowAnimation(previous, animation);
        });
    }

    @SafeVarargs
    public static <E extends BaseMonster> SetAnimationToPlay<E> checkedAttack(AnimationPlayHolder<E>... animations) {
        return new SetAnimationToPlay<>(animations).filter((animation, entity) -> {
            String previous = BrainUtils.getMemory(entity, ModMemoryTypes.LAST_ANIMATION.get());
            return entity.allowAnimation(previous, animation);
        });
    }

    public static <E extends BaseMonster> PlayAnimation<E> cooldownedPlay() {
        return new PlayAnimation<E>().withCallback(cooldownHandler());
    }

    public static <E extends BaseMonster> PlayAnimation.OnStart<E> cooldownHandler() {
        return (animation, chains, entity) -> {
            double calc = entity.animationCooldown(animation);
            calc += entity.getAnimationHandler().get(animation).length();
            if (chains != null) {
                for (AnimationPlayHolder.AnimationHolder chain : chains) {
                    calc += entity.getAnimationHandler().get(chain.animation()).length();
                }
            }
            int cooldown = Mth.ceil(calc);
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, cooldown);
            BrainUtils.setForgettableMemory(entity, ModMemoryTypes.LAST_ANIMATION.get(), animation, cooldown + 80);
        };
    }

    public static <T extends BaseMonster> Predicate<T> inAABBRange(String animation) {
        return entity -> {
            Entity target = BrainUtils.getTargetOfEntity(entity);
            return target != null && entity.isInAttackBox(target, animation);
        };
    }

    public static <E extends BaseMonster> PlayAnimation<E> requireInRangePlay() {
        return (PlayAnimation<E>) new PlayAnimation<E>()
                .withCallback(cooldownHandler())
                .startCondition(entity -> {
                    AnimationPlayHolder<?> anim = BrainUtils.getMemory(entity, MoreMemoryModules.ANIMATION_TO_PLAY.get());
                    Entity target = BrainUtils.getTargetOfEntity(entity);
                    return target == null || entity.isInAttackBox(target, anim.animation());
                });
    }

    public static <E extends LivingEntity> ToIntBiFunction<E, LivingEntity> closeEnough(int dist) {
        return (entity, target) -> {
            int close = dist;
            if (target != null) {
                close += Mth.ceil(target.getBbWidth() * 0.5);
            }
            return close;
        };
    }

    public static <E extends LivingEntity> Predicate<E> ifCloserThan(double dist) {
        return entity -> {
            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
            if (target == null && entity instanceof Mob mob) {
                target = mob.getTarget();
            }
            return entity.distanceToSqr(target) <= dist * dist;
        };
    }

    public static <E extends LivingEntity> Predicate<E> ifFurtherThan(double dist) {
        return entity -> {
            LivingEntity target = BrainUtils.hasMemory(entity, MemoryModuleType.ATTACK_TARGET) ? BrainUtils.getTargetOfEntity(entity) : null;
            if (target == null && entity instanceof Mob mob) {
                target = mob.getTarget();
            }
            return entity.distanceToSqr(target) >= dist * dist;
        };
    }
}
