package com.flemmli97.runecraftory.common.config.values;

import com.flemmli97.tenshilib.api.config.IConfigValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.BiomeDictionary;

import java.util.Set;

public class GenConfig implements IConfigValue<GenConfig> {

    private ResourceLocation block;
    private Set<BiomeDictionary.Type> whiteList;
    private Set<BiomeDictionary.Type> blackList;
    private int chance, weight, maxAmount, xSpread, ySpread, zSpread;

    public GenConfig(ResourceLocation block, Set<BiomeDictionary.Type> types, Set<BiomeDictionary.Type> blackList, int chance, int weight, int tries, int xSpread, int ySpread, int zSpread) {
        this.block = block;
        this.whiteList = types;
        this.blackList = blackList;
        this.chance = chance;
        this.weight = weight;
        this.maxAmount = tries;
        this.xSpread = xSpread;
        this.ySpread = ySpread;
        this.zSpread = zSpread;
    }

    public ResourceLocation getBlock() {
        return this.block;
    }

    public Set<BiomeDictionary.Type> whiteList(){
        return this.whiteList;
    }

    public Set<BiomeDictionary.Type> blackList(){
        return this.blackList;
    }

    public int chance(){
        return this.chance;
    }

    public int weight(){
        return this.weight;
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

    public GenConfig read(GenConfigSpec spec) {
        this.block = new ResourceLocation(spec.block.get());
        this.weight = spec.weight.get();
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
        if(obj == this)
            return true;
        if(obj instanceof GenConfig)
            return this.block.equals(((GenConfig) obj).block);
        return false;
    }

    @Override
    public GenConfig readFromString(String s) {
        return null;
    }

    @Override
    public String writeToString() {
        return null;
    }
}
