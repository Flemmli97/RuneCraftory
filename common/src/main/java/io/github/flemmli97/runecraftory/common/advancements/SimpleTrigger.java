package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SimpleTrigger extends SimpleCriterionTrigger<SimpleTrigger.TriggerInstance> {

    private final ResourceLocation id;

    public SimpleTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    protected SimpleTrigger.TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        return new SimpleTrigger.TriggerInstance(this.id, player);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, inst -> true);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation id, EntityPredicate.Composite composite) {
            super(id, composite);
        }

        public static TriggerInstance of(SimpleTrigger trig) {
            return new TriggerInstance(trig.getId(), EntityPredicate.Composite.ANY);
        }
    }
}
