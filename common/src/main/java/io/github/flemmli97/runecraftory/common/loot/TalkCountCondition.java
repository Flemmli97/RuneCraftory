package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;
import java.util.UUID;

public record TalkCountCondition(int count) implements LootItemCondition {

    public static Builder of(int count) {
        return () -> new TalkCountCondition(count);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.TALKCOUNT.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof EntityNPCBase npc) {
            Player player = ctx.getParamOrNull(LootCtxParameters.INTERACTING_PLAYER);
            UUID uuid = player != null ? player.getUUID() : ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (uuid != null)
                return npc.talkCount(uuid) >= this.count;
        }
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<TalkCountCondition> {

        @Override
        public void serialize(JsonObject object, TalkCountCondition condition, JsonSerializationContext context) {
            object.addProperty("count", condition.count);
        }

        @Override
        public TalkCountCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new TalkCountCondition(GsonHelper.getAsInt(obj, "count", 0));
        }
    }
}