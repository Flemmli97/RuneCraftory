package com.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.IFeatureConfig;

public class ChancedBlockCluster implements IFeatureConfig {

    public static final Codec<ChancedBlockCluster> CODEC = RecordCodecBuilder.create(inst->
            inst.group(BlockClusterFeatureConfig.CODEC.fieldOf("cluster").forGetter((cb) -> cb.cluster),
            Codec.INT.fieldOf("chance").orElse(3).forGetter((cb) -> cb.chance)).apply(inst, ChancedBlockCluster::new));

    private final BlockClusterFeatureConfig cluster;
    private int chance;

    public ChancedBlockCluster(BlockClusterFeatureConfig cluster, int chance) {
        this.cluster = cluster;
        this.chance = chance;
    }

    public int getChance(){
        return this.chance;
    }

    public BlockClusterFeatureConfig getCluster(){
        return this.cluster;
    }
}
