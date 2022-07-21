package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public class VanillaDropCondition implements LootItemCondition {

    private VanillaDropCondition() {
    }

    public static LootItemCondition.Builder get() {
        return VanillaDropCondition::new;
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootCondition.VANILLADROP.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of();
    }

    @Override
    public boolean test(LootContext ctx) {
        return GeneralConfig.dropVanillaLoot;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<VanillaDropCondition> {

        @Override
        public void serialize(JsonObject object, VanillaDropCondition condition, JsonSerializationContext context) {
        }

        @Override
        public VanillaDropCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            return new VanillaDropCondition();
        }
    }
}

