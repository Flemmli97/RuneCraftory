package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class LevelTrigger extends SimpleCriterionTrigger<LevelTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> inst.matches(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  int level) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                Codec.INT.fieldOf("level").forGetter(TriggerInstance::level)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> of(int amount) {
            return RuneCraftoryCriteria.LEVEL_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), amount));
        }

        public boolean matches(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).getPlayerLevel().getLevel() >= this.level;
        }
    }
}
