package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.world.family.FamilyEntry;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;
import java.util.UUID;

public record NPCRelationCondition(FamilyEntry.Relationship relation) implements LootItemCondition {

    public static LootItemCondition.Builder of(FamilyEntry.Relationship relation) {
        return () -> new NPCRelationCondition(relation);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.INTERACTINGPLAYER.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY, LootCtxParameters.UUID_CONTEXT);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityNPCBase npc) {
            UUID player = ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (player != null)
                return npc.relationFor(player) == this.relation;
        }
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<NPCRelationCondition> {

        @Override
        public void serialize(JsonObject object, NPCRelationCondition condition, JsonSerializationContext context) {
            object.addProperty("relation", condition.relation.name());
        }

        @Override
        public NPCRelationCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            String type = GsonHelper.getAsString(obj, "relation", FamilyEntry.Relationship.NONE.toString());
            try {
                return new NPCRelationCondition(FamilyEntry.Relationship.valueOf(type));
            } catch (IllegalArgumentException e) {
                throw new JsonSyntaxException("Unknown relation type '" + type + "'");
            }
        }
    }
}