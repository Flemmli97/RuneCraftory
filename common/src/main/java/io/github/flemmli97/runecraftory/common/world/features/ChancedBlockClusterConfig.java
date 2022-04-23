package io.github.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.Optional;

public class ChancedBlockClusterConfig implements FeatureConfiguration {

    public static final Codec<ChancedBlockClusterConfig> CODEC = RecordCodecBuilder.create(codec -> codec.group(
                    BlockStateProvider.CODEC.fieldOf("state_provider").forGetter((cb) -> cb.stateProvider),
                    TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag_whitelist").forGetter((cb) -> Optional.ofNullable(cb.whitelist)),
                    TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biome_tag_blacklist").forGetter((cb) -> Optional.ofNullable(cb.blacklist)),
                    Codec.INT.fieldOf("min").orElse(3).forGetter((cb) -> cb.amount.getMinValue()),
                    Codec.INT.fieldOf("max").orElse(3).forGetter((cb) -> cb.amount.getMaxValue()),
                    Codec.INT.fieldOf("radius").orElse(3).forGetter((cb) -> cb.radius),
                    Codec.INT.fieldOf("tries").orElse(3).forGetter((cb) -> cb.tries))
            .apply(codec, (provider, whitelist, blacklist, min, max, radius, tries) -> new ChancedBlockClusterConfig(provider, whitelist.orElse(null), blacklist.orElse(null), min, max, radius, tries)));

    public final BlockStateProvider stateProvider;
    public final TagKey<Biome> whitelist;
    public final TagKey<Biome> blacklist;
    public final IntProvider amount;
    public final int radius, tries;

    public ChancedBlockClusterConfig(BlockStateProvider stateProvider, TagKey<Biome> whitelist, TagKey<Biome> blacklist, int min, int max, int radius, int tries) {
        this.stateProvider = stateProvider;
        this.whitelist = whitelist;
        this.blacklist = blacklist;
        this.amount = UniformInt.of(min, max);
        this.radius = radius;
        this.tries = tries;
    }
}
