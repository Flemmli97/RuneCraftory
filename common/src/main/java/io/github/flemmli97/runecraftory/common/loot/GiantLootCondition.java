package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.blocks.tile.CropBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record GiantLootCondition(boolean isGiant) implements LootItemCondition {

    @Override
    public LootItemConditionType getType() {
        return ModLootCondition.GIANTCROP.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.getParamOrNull(LootContextParams.BLOCK_ENTITY) instanceof CropBlockEntity crop)
            return crop.isGiant() == this.isGiant;
        return false;
    }

    public static LootItemCondition.Builder get(boolean giant) {
        return () -> new GiantLootCondition(giant);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<GiantLootCondition> {

        @Override
        public void serialize(JsonObject object, GiantLootCondition condition, JsonSerializationContext context) {
            object.addProperty("giant", condition.isGiant);
        }

        @Override
        public GiantLootCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new GiantLootCondition(GsonHelper.getAsBoolean(obj, "giant", false));
        }
    }
}
