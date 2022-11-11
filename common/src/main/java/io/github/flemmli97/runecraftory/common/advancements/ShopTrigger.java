package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public class ShopTrigger extends SimpleCriterionTrigger<ShopTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "shop_buy");

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        EntityPredicate.Composite target = EntityPredicate.Composite.fromJson(json, "npc", context);
        ItemPredicate itemPredicate = ItemPredicate.fromJson(json.get("item"));
        return new TriggerInstance(player, target, itemPredicate);
    }

    public void trigger(ServerPlayer player, EntityNPCBase npc, ItemStack stack) {
        LootContext lootContext = EntityPredicate.createContext(player, npc);
        this.trigger(player, inst -> inst.matches(lootContext, stack));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final EntityPredicate.Composite target;
        private final ItemPredicate itemPredicate;

        public TriggerInstance(EntityPredicate.Composite composite, EntityPredicate.Composite target, ItemPredicate itemPredicate) {
            super(ID, composite);
            this.target = target;
            this.itemPredicate = itemPredicate;
        }

        public static ShopTrigger.TriggerInstance buyAny() {
            return new ShopTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.ANY, ItemPredicate.ANY);
        }

        public static ShopTrigger.TriggerInstance buyFromItem(EntityPredicate.Builder npc, ItemPredicate.Builder item) {
            return new ShopTrigger.TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.Composite.wrap(npc.build()), item.build());
        }

        public boolean matches(LootContext context, ItemStack stack) {
            if (!this.target.matches(context)) {
                return false;
            }
            return this.itemPredicate.matches(stack);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.add("item", this.itemPredicate.serializeToJson());
            obj.add("npc", this.target.toJson(context));
            return obj;
        }
    }
}
