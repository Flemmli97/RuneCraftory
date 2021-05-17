package com.flemmli97.runecraftory.common.config.values;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class HerbGenConfig {

    private ResourceLocation block;
    private Set<BiomeDictionary.Type> whiteList;
    private Set<BiomeDictionary.Type> blackList;
    private int weight;

    private HerbGenConfig(ResourceLocation block, Set<BiomeDictionary.Type> types, Set<BiomeDictionary.Type> blackList, int weight) {
        this.block = block;
        this.whiteList = types;
        this.blackList = blackList;
        this.weight = weight;
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

    public int weight() {
        return this.weight;
    }

    public HerbGenConfig read(HerbGenCofigSpecs spec) {
        this.whiteList.clear();
        this.blackList.clear();
        for (String s : spec.whiteList.get())
            this.whiteList.add(BiomeDictionary.Type.getType(s));
        for (String s : spec.blackList.get())
            this.blackList.add(BiomeDictionary.Type.getType(s));
        this.weight = spec.weight.get();
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
        if (obj instanceof HerbGenConfig)
            return this.block.equals(((HerbGenConfig) obj).block);
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
        private int weight = 1;

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

        public Builder withWeight(int weight) {
            this.weight = weight;
            return this;
        }

        public HerbGenConfig build() {
            return new HerbGenConfig(this.block, this.whiteList, this.blackList, this.weight);
        }
    }
}
