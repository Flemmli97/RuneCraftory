package io.github.flemmli97.runecraftory.common.loot;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.MapCodec;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.Set;

public record BiomeLootCondition(TagKey<Biome> tag) implements LootItemCondition {

    public static final MapCodec<BiomeLootCondition> CODEC = TagKey.codec(Registries.BIOME).fieldOf("biome_tag")
            .xmap(BiomeLootCondition::new, BiomeLootCondition::tag);

    public static LootItemCondition.Builder get(TagKey<Biome> tag) {
        return () -> new BiomeLootCondition(tag);
    }

    @Override
    public LootItemConditionType getType() {
        return RuneCraftoryLootRegistries.BIOME.get();
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.ORIGIN);
    }

    @Override
    public boolean test(LootContext ctx) {
        return ctx.getLevel().getBiome(BlockPos.containing(ctx.getParam(LootContextParams.ORIGIN))).is(this.tag);
    }
}
