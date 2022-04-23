/*
 * Decompiled with CFR 0.0.9 (FabricMC cc05e23f).
 */
package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.platform.PlatformUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HerbFeature extends Feature<HerbFeatureConfig> {

    public HerbFeature(Codec<HerbFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HerbFeatureConfig> ctx) {
        BlockPos blockPos = ctx.origin();
        HerbFeatureConfig randomPatchConfiguration = ctx.config();
        Holder<Biome> biome = ctx.level().getBiome(blockPos);
        List<Entry> bakedList = randomPatchConfiguration.entries().stream()
                .filter(p -> p.getWeight().asInt() > 0)
                .filter(e -> this.match(e, biome)).toList();
        Random random = ctx.random();
        Entry entry = WeightedRandom.getRandomItem(random, bakedList).orElse(null);
        if (entry == null)
            return false;
        WorldGenLevel worldGenLevel = ctx.level();
        int i = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int j = randomPatchConfiguration.radius() + 1;
        int k = randomPatchConfiguration.ySpread() + 1;
        BlockState state = entry.block.defaultBlockState();
        for (int l = 0; l < randomPatchConfiguration.tries(); ++l) {
            mutableBlockPos.setWithOffset(blockPos, random.nextInt(j) - random.nextInt(j), random.nextInt(k) - random.nextInt(k), random.nextInt(j) - random.nextInt(j));
            if (worldGenLevel.isEmptyBlock(mutableBlockPos) && state.canSurvive(worldGenLevel, mutableBlockPos)) {
                worldGenLevel.setBlock(mutableBlockPos, state, Block.UPDATE_ALL);
                i++;
            }
        }
        return i > 0;
    }

    private boolean match(HerbFeature.Entry cfg, Holder<Biome> biome) {
        return (cfg.whitelist == null || biome.is(cfg.whitelist))
                && (cfg.blacklist == null || !biome.is(cfg.blacklist));
    }

    public static class Entry implements WeightedEntry {

        public static final Codec<HerbFeature.Entry> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                        Registry.BLOCK.byNameCodec().fieldOf("block").forGetter((cb) -> cb.block),
                        TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag_whitelist").forGetter((cb) -> Optional.ofNullable(cb.whitelist)),
                        TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag_blacklist").forGetter((cb) -> Optional.ofNullable(cb.blacklist)),
                        Codec.INT.fieldOf("weight").orElse(3).forGetter((cb) -> cb.weight.asInt()))
                .apply(codec, (block, whitelist, blacklist, weight) -> new Entry(block, whitelist.orElse(null), blacklist.orElse(null), weight)));

        public final Block block;
        public final TagKey<Biome> whitelist;
        public final TagKey<Biome> blacklist;
        public final Weight weight;

        public Entry(Block block, int weight) {
            this.block = block;
            this.weight = Weight.of(weight);
            ResourceLocation res = PlatformUtils.INSTANCE.blocks().getIDFrom(block);
            this.whitelist = PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "herb/in_" + res.getPath()));
            this.blacklist = PlatformUtils.INSTANCE.tag(Registry.BIOME_REGISTRY, new ResourceLocation(RuneCraftory.MODID, "herb/not_" + res.getPath()));
        }

        public Entry(Block block, TagKey<Biome> whitelist, TagKey<Biome> blacklist, int weight) {
            this.block = block;
            this.weight = Weight.of(weight);
            this.whitelist = whitelist;
            this.blacklist = blacklist;
        }

        @Override
        public Weight getWeight() {
            return this.weight;
        }
    }
}

