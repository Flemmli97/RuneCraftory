package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record BiomeLootCondition(TagKey<Biome> tag) implements LootItemCondition {

    public static LootItemCondition.Builder get(TagKey<Biome> tag) {
        return () -> new BiomeLootCondition(tag);
    }

    @Override
    public LootItemConditionType getType() {
        return ModLootRegistries.BIOME.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public boolean test(LootContext ctx) {
        return ctx.getLevel().getBiome(new BlockPos(ctx.getParam(LootContextParams.ORIGIN))).is(this.tag);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<BiomeLootCondition> {

        @Override
        public void serialize(JsonObject object, BiomeLootCondition condition, JsonSerializationContext context) {
            object.addProperty("biome_tag", condition.tag.location().toString());
        }

        @Override
        public BiomeLootCondition deserialize(JsonObject obj, JsonDeserializationContext context) {
            String tag = GsonHelper.getAsString(obj, "biome_tag");
            if (tag.isEmpty())
                throw new JsonSyntaxException("Tag key is empty");
            return new BiomeLootCondition(TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(tag)));
        }
    }
}
