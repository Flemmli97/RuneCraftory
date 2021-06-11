package com.flemmli97.runecraftory.common.loot;

import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.conditions.ILootCondition;

import java.util.Set;

public class VanillaDropCondition implements ILootCondition {

    private VanillaDropCondition() {
    }

    @Override
    public LootConditionType getConditionType() {
        return ModLootModifier.VANILLADROP;
    }

    @Override
    public Set<LootParameter<?>> getRequiredParameters() {
        return ImmutableSet.of();
    }

    @Override
    public boolean test(LootContext ctx) {
        return GeneralConfig.dropVanillaLoot;
    }

    public static ILootCondition.IBuilder get() {
        return () -> new VanillaDropCondition();
    }

    public static class Serializer implements ILootSerializer<VanillaDropCondition> {

        @Override
        public void serialize(JsonObject object, VanillaDropCondition condition, JsonSerializationContext context) {
        }

        @Override
        public VanillaDropCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new VanillaDropCondition();
        }
    }
}

