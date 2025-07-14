package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.attachment.player.EntityStatsTracker;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class TameMonsterTrigger extends SimpleCriterionTrigger<TameMonsterTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, BaseMonster monster, EntityStatsTracker tracker) {
        this.trigger(player, inst -> inst.matches(player, monster, tracker));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<EntityPredicate> predicate,
                                  int amount) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.CODEC.optionalFieldOf("predicate").forGetter(TriggerInstance::predicate),
                ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(TriggerInstance::amount)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> of(int amount) {
            return RuneCraftoryCriteria.TAME_MONSTER_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), amount));
        }

        public static Criterion<TriggerInstance> of(int amount, EntityPredicate.Builder builder) {
            return RuneCraftoryCriteria.TAME_MONSTER_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(builder.build()), amount));
        }

        public static Advancement.Builder amountOfSteps(Advancement.Builder builder, String key, int amount, boolean boss) {
            for (int i = 0; i < amount; i++) {
                builder.addCriterion(key + "_" + i, boss ? bossOf(i + 1) : of(i + 1));
            }
            return builder;
        }

        public static Criterion<TriggerInstance> bossOf(int amount) {
            return RuneCraftoryCriteria.TAME_MONSTER_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(),
                    Optional.of(EntityPredicate.Builder.entity().of(RunecraftoryTags.EntityTypes.BOSS_MONSTERS).build()), amount));
        }

        public boolean matches(ServerPlayer player, BaseMonster monster, EntityStatsTracker tracker) {
            if (this.predicate.isPresent()) {
                if (!this.predicate.get().matches(player, monster))
                    return false;
                return tracker.getTameCount(monster.getType()) >= this.amount;
            }
            return tracker.getTotalTameCount(false) >= this.amount;
        }
    }
}
