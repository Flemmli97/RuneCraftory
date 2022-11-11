package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class ShippingTrigger extends SimpleCriterionTrigger<ShippingTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "shipping");

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(player, itemPredicate);
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        this.trigger(player, inst -> inst.matches(stack));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate itemPredicate;

        public TriggerInstance(EntityPredicate.Composite composite, ItemPredicate itemPredicate) {
            super(ID, composite);
            this.itemPredicate = itemPredicate;
        }

        public static ShippingTrigger.TriggerInstance shipAny() {
            return new ShippingTrigger.TriggerInstance(EntityPredicate.Composite.ANY, ItemPredicate.ANY);
        }

        public static ShippingTrigger.TriggerInstance shipSpecific(ItemPredicate.Builder item) {
            return new ShippingTrigger.TriggerInstance(EntityPredicate.Composite.ANY, item.build());
        }

        public boolean matches(ItemStack stack) {
            return this.itemPredicate.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.add("item", this.itemPredicate.serializeToJson());
            return obj;
        }
    }
}
