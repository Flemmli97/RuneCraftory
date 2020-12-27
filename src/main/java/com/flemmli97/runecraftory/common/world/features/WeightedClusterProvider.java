package com.flemmli97.runecraftory.common.world.features;

import com.mojang.serialization.Codec;
import net.minecraft.util.WeightedList;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Random;

public class WeightedClusterProvider implements IFeatureConfig {

    public static final Codec<WeightedClusterProvider> CODEC = WeightedList.createCodec(ChancedBlockCluster.CODEC).xmap(WeightedClusterProvider::new, (inst) -> inst.confs).fieldOf("entries").codec();

    private final WeightedList<ChancedBlockCluster> confs;

    public WeightedClusterProvider() {
        this(new WeightedList<>());
    }

    private WeightedClusterProvider(WeightedList<ChancedBlockCluster> confs) {
        this.confs = confs;
    }

    public WeightedClusterProvider add(ChancedBlockCluster conf, int weight) {
        this.confs.add(conf, weight);
        return this;
    }

    public ChancedBlockCluster get(Random rand) {
        return this.confs.pickRandom(rand);
    }
}
