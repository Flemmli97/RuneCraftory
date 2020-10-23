package com.flemmli97.runecraftory.mobs.config;

import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.flemmli97.tenshilib.common.utils.MapUtils;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class EntityPropertySpecs {

    public final ForgeConfigSpec.ConfigValue<List<String>> baseValues;
    public final ForgeConfigSpec.ConfigValue<Integer> xp;
    public final ForgeConfigSpec.ConfigValue<Integer> money;
    public final ForgeConfigSpec.ConfigValue<Float> taming;
    public final ForgeConfigSpec.ConfigValue<List<String>> tamingItem;
    public final ForgeConfigSpec.ConfigValue<List<String>> daily;
    public final ForgeConfigSpec.BooleanValue ridable;
    public final ForgeConfigSpec.BooleanValue flying;

    public EntityPropertySpecs(ForgeConfigSpec.Builder builder, EntityProperties def) {
        this.baseValues = builder.comment("Base Values of the mob").define("Base Values", def.attString());
        this.xp = builder.comment("Xp this mob gives").define("XP", def.getXp());
        this.money = builder.comment("Money this mob drops").define("Money", def.getMoney());
        this.taming = builder.comment("Base chance to tame this mob").define("Taming Chance", def.tamingChance());
        this.tamingItem = builder.comment("Items that boost the taming chance").define("Taming Items", this.toList(def.getTamingItem()));
        this.daily = builder.comment("Daily Products this mob gives").define("Daily", MapUtils.toListKey(def.dailyDrops(), SimpleItemStackWrapper::writeToString));
        this.ridable = builder.define("Ridable", def.ridable());
        this.flying = builder.define("Can Fly", def.flying());
    }

    private List<String> toList(SimpleItemStackWrapper[] arr){
        List<String> list = Lists.newArrayList();
        for(SimpleItemStackWrapper stack : arr)
            list.add(stack.toString());
        return list;
    }
}
