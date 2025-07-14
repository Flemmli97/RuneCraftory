package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ShippingTrigger extends SimpleCriterionTrigger<ShippingTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, PlayerData data, ItemStack stack) {
        this.trigger(player, inst -> inst.matches(data, stack));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ItemPredicate> predicate,
                                  int amount) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                ItemPredicate.CODEC.optionalFieldOf("predicate").forGetter(TriggerInstance::predicate),
                ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(TriggerInstance::amount)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> shipAny(int amount) {
            return RuneCraftoryCriteria.SHIPPING_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.empty(), amount));
        }

        public static Criterion<TriggerInstance> shipSpecific(ItemPredicate.Builder item, int amount) {
            return RuneCraftoryCriteria.SHIPPING_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(), Optional.of(item.build()), amount));
        }

        public boolean matches(PlayerData data, ItemStack stack) {
            if (this.predicate.isPresent()) {
                if (!this.predicate.get().test(stack))
                    return false;
                PlayerData.ShippedItemData shipped = data.shippedItemData(stack);
                return shipped != null && shipped.amount() >= this.amount;
            }
            return data.getShippedTypesAmount() >= this.amount;
        }
    }
}
