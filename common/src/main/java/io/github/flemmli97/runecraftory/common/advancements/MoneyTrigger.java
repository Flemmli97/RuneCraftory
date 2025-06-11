package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;

import java.util.Optional;

public class MoneyTrigger extends SimpleCriterionTrigger<MoneyTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> inst.matches(player));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  int amount) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(TriggerInstance::amount)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> of(int amount) {
            return ModCriteria.MONEY_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), amount));
        }

        public boolean matches(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).getMoney() >= this.amount;
        }
    }
}
