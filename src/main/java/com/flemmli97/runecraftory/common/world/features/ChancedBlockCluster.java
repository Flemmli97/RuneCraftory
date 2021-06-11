package com.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ChancedBlockCluster implements IFeatureConfig {

    public static final Codec<ChancedBlockCluster> CODEC = RecordCodecBuilder.create(codec -> codec.group(
            BlockStateProvider.CODEC.fieldOf("state_provider").forGetter((cb) -> cb.stateProvider),
            Codec.INT.fieldOf("min").orElse(3).forGetter((cb) -> (int) cb.amount.getMin()),
            Codec.INT.fieldOf("max").orElse(3).forGetter((cb) -> (int) cb.amount.getMax()),
            Codec.INT.fieldOf("spread_x").orElse(3).forGetter((cb) -> cb.spreadX),
            Codec.INT.fieldOf("spread_y").orElse(3).forGetter((cb) -> cb.spreadY),
            Codec.INT.fieldOf("spread_z").orElse(3).forGetter((cb) -> cb.spreadZ)).apply(codec, ChancedBlockCluster::new));

    public final BlockStateProvider stateProvider;
    public final RandomValueRange amount;
    public final int spreadX;
    public final int spreadY;
    public final int spreadZ;

    public ChancedBlockCluster(BlockStateProvider stateProvider, int min, int max, int spreadX, int spreadY, int spreadZ) {
        this.stateProvider = stateProvider;
        this.amount = new RandomValueRange(min, max);
        this.spreadX = spreadX;
        this.spreadY = spreadY;
        this.spreadZ = spreadZ;
    }
}
