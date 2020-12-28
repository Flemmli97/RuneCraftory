package com.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.stream.Collectors;

public class HerbGenCofigSpecs {

    public final ForgeConfigSpec.ConfigValue<List<String>> whiteList;
    public final ForgeConfigSpec.ConfigValue<List<String>> blackList;
    public final ForgeConfigSpec.ConfigValue<Integer> weight;

    public HerbGenCofigSpecs(ForgeConfigSpec.Builder builder, HerbGenConfig def) {
        this.whiteList = builder.comment("WhiteList of BiomeDictionary Types").define("WhiteList", def.whiteList().stream().map(BiomeDictionary.Type::getName).collect(Collectors.toList()));
        this.blackList = builder.comment("BlackList of BiomeDictionary Types").define("BlackList", def.blackList().stream().map(BiomeDictionary.Type::getName).collect(Collectors.toList()));
        this.weight = builder.comment("").define("Weight", def.weight());
    }
}