package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.entities.IBaseMob;
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

public record FriendPointCondition(int points) implements LootItemCondition {

    public static Builder of(int points) {
        return () -> new FriendPointCondition(points);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.FRIENDPOINTS.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.THIS_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.THIS_ENTITY) instanceof IBaseMob mob) {
            Player player = ctx.getParamOrNull(LootCtxParameters.INTERACTING_PLAYER);
            if (player != null)
                return mob.friendPoints(player) >= this.points;
            UUID uuid = ctx.getParamOrNull(LootCtxParameters.UUID_CONTEXT);
            if (uuid != null)
                return mob.friendPoints(uuid) >= this.points;
        }
        return false;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<FriendPointCondition> {

        @Override
        public void serialize(JsonObject object, FriendPointCondition condition, JsonSerializationContext context) {
            object.addProperty("points", condition.points);
        }

        @Override
        public FriendPointCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new FriendPointCondition(GsonHelper.getAsInt(obj, "points", 0));
        }
    }
}