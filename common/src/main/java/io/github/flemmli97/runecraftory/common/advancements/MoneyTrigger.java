package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class MoneyTrigger extends SimpleCriterionTrigger<MoneyTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "money");

    @Override
    protected MoneyTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        return new MoneyTrigger.TriggerInstance(player, GsonHelper.getAsInt(json, "amount", 1));
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> inst.matches(player));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int amount;

        public TriggerInstance(EntityPredicate.Composite composite, int amount) {
            super(ID, composite);
            this.amount = Math.max(1, amount);
        }

        public static MoneyTrigger.TriggerInstance of(int amount) {
            return new MoneyTrigger.TriggerInstance(EntityPredicate.Composite.ANY, amount);
        }

        public boolean matches(ServerPlayer player) {
            return Platform.INSTANCE.getPlayerData(player).map(d -> d.getMoney() >= this.amount).orElse(false);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.addProperty("amount", this.amount);
            return obj;
        }
    }
}
