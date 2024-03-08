package io.github.flemmli97.runecraftory.common.advancements;

import com.google.gson.JsonObject;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.TamedEntityTracker;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class TameMonsterTrigger extends SimpleCriterionTrigger<TameMonsterTrigger.TriggerInstance> {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "tame_monster");

    @Override
    protected TriggerInstance createInstance(JsonObject json, EntityPredicate.Composite player, DeserializationContext context) {
        return new TriggerInstance(player, EntityPredicate.fromJson(json.get("entity")), GsonHelper.getAsInt(json, "amount", 1), GsonHelper.getAsBoolean(json, "bosses", false));
    }

    public void trigger(ServerPlayer player, BaseMonster monster, TamedEntityTracker tracker) {
        this.trigger(player, inst -> inst.matches(player, monster, tracker));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final int amount;
        private final boolean bossOnly;
        private final EntityPredicate entityPredicate;

        public TriggerInstance(EntityPredicate.Composite playerPredicate, EntityPredicate entityPredicate, int amount, boolean bossOnly) {
            super(ID, playerPredicate);
            this.amount = amount;
            this.bossOnly = bossOnly;
            this.entityPredicate = entityPredicate;
        }

        public static TriggerInstance of(int amount) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.ANY, amount, false);
        }

        public static Advancement.Builder amountOfSteps(Advancement.Builder builder, String key, int amount, boolean boss) {
            for (int i = 0; i < amount; i++) {
                builder.addCriterion(key + "_" + i, boss ? bossOf(i + 1) : of(i + 1));
            }
            return builder;
        }

        public static TriggerInstance bossOf(int amount) {
            return new TriggerInstance(EntityPredicate.Composite.ANY, EntityPredicate.ANY, amount, true);
        }

        /**
         * If the entityPredicate is defined get the tame count for the specific entity type.
         * Else it will use the total tame count
         */
        public boolean matches(ServerPlayer player, BaseMonster monster, TamedEntityTracker tracker) {
            if (this.entityPredicate != null && this.entityPredicate != EntityPredicate.ANY) {
                if (!this.entityPredicate.matches(player, monster))
                    return false;
                return tracker.getTameCount(monster.getType()) >= this.amount;
            }
            if (this.bossOnly) {
                if (!monster.getType().is(RunecraftoryTags.BOSS_MONSTERS))
                    return false;
                return tracker.getTotalTameCount(true) >= this.amount;
            }
            return tracker.getTotalTameCount(false) >= this.amount;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject obj = super.serializeToJson(context);
            obj.addProperty("amount", this.amount);
            obj.addProperty("bosses", this.bossOnly);
            obj.add("entity", this.entityPredicate.serializeToJson());
            return obj;
        }
    }
}
