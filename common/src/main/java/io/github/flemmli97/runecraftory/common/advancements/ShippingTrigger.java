package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public class ShippingTrigger extends SimpleCriterionTrigger<ShippingTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "shipping");

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(player, itemPredicate, GsonHelper.getAsInt(json, "amount", 1));
    }

    public void trigger(ServerPlayer player, PlayerData data, ItemStack stack) {
        this.trigger(player, inst -> inst.matches(data, stack));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate itemPredicate;
        private final int amount;

        public TriggerInstance(EntityPredicate.Composite composite, ItemPredicate itemPredicate, int amount) {
            super(ID, composite);
            this.itemPredicate = itemPredicate;
            this.amount = amount;
        }

        public static ShippingTrigger.TriggerInstance shipAny(int amount) {
            return new ShippingTrigger.TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY, amount);
        }

        public static ShippingTrigger.TriggerInstance shipSpecific(ItemPredicate.Builder item, int amount) {
            return new ShippingTrigger.TriggerInstance(EntityPredicate.Composite.ANY, item.build(), 1);
        }

        /**
         * If the itemPredicate is defined get shipping amount for that item.
         * Else it will use the amount of shipped item types.
         */
        public boolean matches(PlayerData data, ItemStack stack) {
            if (this.itemPredicate != null && this.itemPredicate != ItemPredicate.ANY) {
                if (!this.itemPredicate.matches(stack))
                    return false;
                PlayerData.ShippedItemData shipped = data.shippedItemData(stack);
                return shipped != null && shipped.amount() >= this.amount;
            }
            return data.getShippedTypesAmount() >= this.amount;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.add("item", this.itemPredicate.serializeToJson());
            obj.addProperty("amount", this.amount);
            return obj;
        }
    }
}
