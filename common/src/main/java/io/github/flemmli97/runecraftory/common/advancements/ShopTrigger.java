package io.github.flemmli97.runecraftory.common.advancements;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class ShopTrigger extends SimpleCriterionTrigger<ShopTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, EntityNPCBase npc, ItemStack stack) {
        LootContext lootContext = EntityPredicate.createContext(player, npc);
        this.trigger(player, inst -> inst.matches(lootContext, stack));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> target,
                                  Optional<ItemPredicate> item) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player),
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("target").forGetter(TriggerInstance::target),
                ItemPredicate.CODEC.optionalFieldOf("item").forGetter(TriggerInstance::item)
        ).apply(inst, TriggerInstance::new));

        public static Criterion<TriggerInstance> buyAny() {
            return ModCriteria.SHOP_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(),
                    Optional.empty(), Optional.empty()));
        }

        public static Criterion<TriggerInstance> buyFromItem(EntityPredicate.Builder npc, ItemPredicate.Builder item) {
            return ModCriteria.SHOP_TRIGGER.get().createCriterion(new TriggerInstance(Optional.empty(),
                    Optional.of(EntityPredicate.wrap(npc)), Optional.of(item.build())));
        }

        public boolean matches(LootContext context, ItemStack stack) {
            if (!this.target.map(p -> p.matches(context)).orElse(true)) {
                return false;
            }
            return this.item.map(p -> p.test(stack)).orElse(true);
        }
    }
}
