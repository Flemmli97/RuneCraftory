package io.github.flemmli97.runecraftory.common.config.values;

import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.world.features.ChancedBlockCluster;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MineralGenConfig {

    private ResourceLocation block;
    private Set<BiomeDictionary.Type> whiteList;
    private Set<BiomeDictionary.Type> blackList;
    private int chance, minAmount, maxAmount, xSpread, ySpread, zSpread;

    private ConfiguredFeature<?, ?> bakedConf, bakedConfNether;

    private MineralGenConfig(ResourceLocation block, Set<BiomeDictionary.Type> types, Set<BiomeDictionary.Type> blackList, int chance, int min, int max, int xSpread, int ySpread, int zSpread) {
        this.block = block;
        this.whiteList = types;
        this.blackList = blackList;
        this.chance = chance;
        this.minAmount = min;
        this.maxAmount = max;
        this.xSpread = xSpread;
        this.ySpread = ySpread;
        this.zSpread = zSpread;
    }

    public Block getBlock() {
        return ForgeRegistries.BLOCKS.getValue(this.block);
    }

    public ResourceLocation blockRes() {
        return this.block;
    }

    public Set<BiomeDictionary.Type> whiteList() {
        return this.whiteList;
    }

    public Set<BiomeDictionary.Type> blackList() {
        return this.blackList;
    }

    public int chance() {
        return this.chance;
    }

    public int minAmount() {
        return this.minAmount;
    }

    public int maxAmount() {
        return this.maxAmount;
    }

    public int xSpread() {
        return this.xSpread;
    }

    public int ySpread() {
        return this.ySpread;
    }

    public int zSpread() {
        return this.zSpread;
    }

    public void bake() {
        if (this.bakedConf == null) {
            this.bakedConf = ModFeatures.MINERALFEATURE.get()
                    .withConfiguration(new ChancedBlockCluster(new SimpleBlockStateProvider(this.getBlock().getDefaultState()), this.minAmount(), this.maxAmount(), this.xSpread(), this.ySpread(), this.zSpread())).chance(this.chance()).withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT);
            this.bakedConfNether = ModFeatures.MINERALFEATURE.get()
                    .withConfiguration(new ChancedBlockCluster(new SimpleBlockStateProvider(this.getBlock().getDefaultState()), this.minAmount(), this.maxAmount(), this.xSpread(), this.ySpread(), this.zSpread())).chance(this.chance()).withPlacement(Placement.COUNT_MULTILAYER.configure(new FeatureSpreadConfig(6)));
        } else
            throw new IllegalStateException("Feature already configured");
    }

    public ConfiguredFeature<?, ?> configuredFeature() {
        return this.bakedConf;
    }

    public ConfiguredFeature<?, ?> configuredFeatureNether() {
        return this.bakedConfNether;
    }

    public MineralGenConfig read(MineralGenConfigSpec spec) {
        this.whiteList.clear();
        this.blackList.clear();
        this.bakedConf = null;
        this.bakedConfNether = null;
        for (String s : spec.whiteList.get())
            this.whiteList.add(BiomeDictionary.Type.getType(s));
        for (String s : spec.blackList.get())
            this.blackList.add(BiomeDictionary.Type.getType(s));
        this.chance = spec.chance.get();
        this.minAmount = spec.minAmount.get();
        this.maxAmount = spec.maxAmount.get();
        this.xSpread = spec.xSpread.get();
        this.ySpread = spec.ySpread.get();
        this.zSpread = spec.zSpread.get();
        return this;
    }

    @Override
    public int hashCode() {
        return this.block.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof MineralGenConfig)
            return this.block.equals(((MineralGenConfig) obj).block);
        return false;
    }

    @Override
    public String toString() {
        return "GenConf for " + this.block;
    }

    public static class Builder {

        private ResourceLocation block;
        private Set<BiomeDictionary.Type> whiteList = new HashSet<>();
        private Set<BiomeDictionary.Type> blackList = new HashSet<>();
        private int chance = 1, weight = 1, minAmount = 1, maxAmount = 1, xSpread = 4, ySpread = 3, zSpread = 4;

        public Builder(ResourceLocation block) {
            this.block = block;
        }

        public Builder addWhiteListType(BiomeDictionary.Type... types) {
            this.whiteList.addAll(Arrays.asList(types));
            return this;
        }

        public Builder addBlackListType(BiomeDictionary.Type... types) {
            this.blackList.addAll(Arrays.asList(types));
            return this;
        }

        public Builder withChance(int chance) {
            this.chance = chance;
            return this;
        }

        public Builder setAmount(int min, int max) {
            if (max == 0)
                throw new IllegalStateException("Cant set 0 amount. Use 0 chance to disable");
            this.minAmount = min;
            this.maxAmount = Math.max(min, max);
            return this;
        }

        public Builder setSpread(int x, int y, int z) {
            this.xSpread = x;
            this.ySpread = y;
            this.zSpread = z;
            return this;
        }

        public MineralGenConfig build() {
            return new MineralGenConfig(this.block, this.whiteList, this.blackList, this.chance, this.minAmount, this.maxAmount, this.xSpread, this.ySpread, this.zSpread);
        }
    }
}
