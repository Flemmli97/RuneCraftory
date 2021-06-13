package io.github.flemmli97.runecraftory.common.config.values;

import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.flemmli97.tenshilib.common.utils.MapUtils;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class EntityPropertySpecs {

    public final ForgeConfigSpec.ConfigValue<List<String>> baseValues;
    public final ForgeConfigSpec.ConfigValue<List<String>> levelGains;
    public final ForgeConfigSpec.ConfigValue<Integer> xp;
    public final ForgeConfigSpec.ConfigValue<Integer> money;
    public final ForgeConfigSpec.ConfigValue<Float> taming;
    public final ForgeConfigSpec.ConfigValue<List<String>> tamingItem;
    public final ForgeConfigSpec.ConfigValue<List<String>> daily;
    public final ForgeConfigSpec.BooleanValue ridable;
    public final ForgeConfigSpec.BooleanValue flying;

    public EntityPropertySpecs(ForgeConfigSpec.Builder builder, EntityProperties def) {
        this.baseValues = builder.comment("Base Values at level 1 of the mob").define("Base Values", def.attString());
        this.levelGains = builder.comment("Attribute values gained per level. Actual attributes are always rounded down to the nearest integer so a gain of 0.1 means 1 every 10 levels").define("Level Gains", def.gainString());
        this.xp = builder.comment("Xp this mob gives").define("XP", def.getXp());
        this.money = builder.comment("Money this mob drops").define("Money", def.getMoney());
        this.taming = builder.comment("Base chance to tame this mob").define("Taming Chance", def.tamingChance());
        this.tamingItem = builder.comment("Items that boost the taming chance").define("Taming Items", this.toList(def.getTamingItem()));
        this.daily = builder.comment("Daily Products this mob gives").define("Daily", MapUtils.toListKey(def.dailyDrops(), SimpleItemStackWrapper::writeToString));
        this.ridable = builder.define("Ridable", def.ridable());
        this.flying = builder.define("Can Fly", def.flying());
    }

    private List<String> toList(SimpleItemStackWrapper[] arr) {
        List<String> list = new ArrayList<>();
        for (SimpleItemStackWrapper stack : arr)
            list.add(stack.toString());
        return list;
    }
}
