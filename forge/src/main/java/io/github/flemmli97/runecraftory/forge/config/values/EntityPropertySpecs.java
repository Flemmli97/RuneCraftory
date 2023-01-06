package io.github.flemmli97.runecraftory.forge.config.values;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class EntityPropertySpecs {

    public final ForgeConfigSpec.ConfigValue<List<String>> baseValues;
    public final ForgeConfigSpec.ConfigValue<List<String>> levelGains;
    public final ForgeConfigSpec.IntValue xp;
    public final ForgeConfigSpec.IntValue money;
    public final ForgeConfigSpec.DoubleValue taming;
    public final ForgeConfigSpec.BooleanValue ridable;
    public final ForgeConfigSpec.BooleanValue flying;
    public final ForgeConfigSpec.IntValue size;
    public final ForgeConfigSpec.BooleanValue needsRoof;

    public EntityPropertySpecs(ForgeConfigSpec.Builder builder, EntityProperties def) {
        this.baseValues = builder.comment("Base Values at level 1 of the mob").define("Base Values", def.attString());
        this.levelGains = builder.comment("Attribute values gained per level. Actual attributes are always rounded down to the nearest integer so a gain of 0.1 means 1 every 10 levels").define("Level Gains", def.gainString());
        this.xp = builder.comment("Xp this mob gives").defineInRange("XP", def.getXp(), 0, Integer.MAX_VALUE);
        this.money = builder.comment("Money this mob drops").defineInRange("Money", def.getMoney(), 0, Integer.MAX_VALUE);
        this.taming = builder.comment("Base chance to tame this mob").defineInRange("Taming Chance", def.tamingChance(), 0, 1);
        this.ridable = builder.define("Ridable", def.ridable());
        this.flying = builder.define("Can Fly", def.flying());
        this.size = builder.comment("The amount of space this monster takes in a barn").defineInRange("Size", def.getSize(), 1, Integer.MAX_VALUE);
        this.needsRoof = builder.define("If the monsters barn needs a roof", def.needsRoof());
    }

    public static EntityProperties ofSpec(EntityPropertySpecs spec) {
        EntityProperties.Builder builder = new EntityProperties.Builder();
        for (String s : spec.baseValues.get()) {
            String[] sp = s.split("-");
            if (sp.length != 2)
                continue;
            builder.putAttributes(sp[0], Double.parseDouble(sp[1]));
        }
        for (String s : spec.levelGains.get()) {
            String[] sp = s.split("-");
            if (sp.length != 2)
                continue;
            builder.putLevelGains(sp[0], Double.parseDouble(sp[1]));
        }
        builder.xp(spec.xp.get());
        builder.money(spec.money.get());
        builder.tamingChance(spec.taming.get().floatValue());
        builder.money(spec.money.get());
        builder.money(spec.money.get());
        if (spec.ridable.get())
            builder.setRidable();
        if (spec.flying.get())
            builder.setFlying();
        builder.setBarnOccupancy(spec.size.get());
        if (!spec.needsRoof.get())
            builder.doesntNeedBarnRoof();
        return builder.build();
    }

    private List<String> toList(SimpleItemStackWrapper[] arr) {
        List<String> list = new ArrayList<>();
        for (SimpleItemStackWrapper stack : arr)
            list.add(stack.toString());
        return list;
    }
}
