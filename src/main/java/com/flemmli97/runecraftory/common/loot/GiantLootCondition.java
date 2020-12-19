package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.tenshilib.common.utils.JsonUtils;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.Set;

public class GiantLootCondition implements ILootCondition {

    private final boolean isGiant;

    private GiantLootCondition(boolean isGiant) {
        this.isGiant = isGiant;
    }

    @Override
    public LootConditionType getType() {
        return ModLootModifier.GIANTCROP;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootParameters.BLOCK_ENTITY);
    }

    @Override
    public boolean test(LootContext ctx) {
        if (ctx.get(LootParameters.BLOCK_ENTITY) instanceof TileCrop)
            return ((TileCrop) ctx.get(LootParameters.BLOCK_ENTITY)).isGiant() == this.isGiant;
        return false;
    }

    public static ILootCondition.IBuilder get(boolean giant) {
        return () -> new GiantLootCondition(giant);
    }

    public static class Serializer implements ILootSerializer<GiantLootCondition> {

        @Override
        public void toJson(JsonObject object, GiantLootCondition condition, JsonSerializationContext context) {
            object.addProperty("giant", condition.isGiant);
        }

        @Override
        public GiantLootCondition fromJson(JsonObject obj, JsonDeserializationContext context) {
            return new GiantLootCondition(JsonUtils.get(obj, "giant", false));
        }
    }
}
