package com.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Set;

public class GenConfigSpec {

    public final ForgeConfigSpec.ConfigValue<String> block;
    private Set<BiomeDictionary.Type> types;
    private boolean blackList;
    private int chance;
    public final ForgeConfigSpec.ConfigValue<Integer> weight;
    public final ForgeConfigSpec.ConfigValue<Integer> maxAmount;
    public final ForgeConfigSpec.ConfigValue<Integer> xSpread;
    public final ForgeConfigSpec.ConfigValue<Integer> ySpread;
    public final ForgeConfigSpec.ConfigValue<Integer> zSpread;

    public GenConfigSpec(ForgeConfigSpec.Builder builder, GenConfig def) {
        block = builder.comment("Block to place").define("Block", def.getBlock().toString());
        weight = builder.comment("").define("Weight", def.weight());
        maxAmount = builder.comment("").define("Max Amount", def.maxAmount());
        xSpread = builder.comment("Max x spread from center").define("XSpread", def.xSpread());
        ySpread = builder.comment("Max y spread from center").define("YSpread", def.ySpread());
        zSpread = builder.comment("Max z spread from center").define("ZSpread", def.zSpread());
    }
}