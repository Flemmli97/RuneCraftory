package io.github.flemmli97.runecraftory.common.config.values;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.stream.Collectors;

public class MineralGenConfigSpec {

    public final ForgeConfigSpec.ConfigValue<List<String>> whiteList;
    public final ForgeConfigSpec.ConfigValue<List<String>> blackList;
    public final ForgeConfigSpec.ConfigValue<Integer> chance;
    public final ForgeConfigSpec.ConfigValue<Integer> minAmount;
    public final ForgeConfigSpec.ConfigValue<Integer> maxAmount;
    public final ForgeConfigSpec.ConfigValue<Integer> xSpread;
    public final ForgeConfigSpec.ConfigValue<Integer> ySpread;
    public final ForgeConfigSpec.ConfigValue<Integer> zSpread;

    public MineralGenConfigSpec(ForgeConfigSpec.Builder builder, MineralGenConfig def) {
        this.whiteList = builder.comment("WhiteList of BiomeDictionary Types").define("WhiteList", def.whiteList().stream().map(BiomeDictionary.Type::getName).collect(Collectors.toList()));
        this.blackList = builder.comment("BlackList of BiomeDictionary Types").define("BlackList", def.blackList().stream().map(BiomeDictionary.Type::getName).collect(Collectors.toList()));
        this.chance = builder.comment("Chance to generate each chunk in 1/x").define("Chance", def.chance());
        this.minAmount = builder.comment("").define("Min Amount", def.minAmount());
        this.maxAmount = builder.comment("").define("Max Amount", def.maxAmount());
        this.xSpread = builder.comment("Max x spread from center").define("XSpread", def.xSpread());
        this.ySpread = builder.comment("Max y spread from center").define("YSpread", def.ySpread());
        this.zSpread = builder.comment("Max z spread from center").define("ZSpread", def.zSpread());
    }
}