package com.flemmli97.runecraftory.mobs.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.tenshilib.common.config.Configuration;
import com.google.common.collect.Maps;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Map;

public class MobConfigSpec {

    public static final Configuration<MobConfigSpec> config;

    public final ForgeConfigSpec.ConfigValue<Integer> gateHealth;
    public final ForgeConfigSpec.ConfigValue<Integer> gateDef;
    public final ForgeConfigSpec.ConfigValue<Integer> gateMDef;
    public final ForgeConfigSpec.ConfigValue<Integer> spawnChance;
    public final ForgeConfigSpec.ConfigValue<Integer> gateXP;
    public final ForgeConfigSpec.ConfigValue<Integer> gateMoney;

    public final Map<EntityType<? extends BaseMonster>, EntityPropertySpecs> mobSpecs = Maps.newHashMap();
    private MobConfigSpec(ForgeConfigSpec.Builder builder){
        builder.comment("Gate Configs").push("gate");
        this.gateHealth = builder.comment("Base health of gates").define("Health", 75);
        this.gateDef = builder.comment("Base defence of gates").define("Defence", 5);
        this.gateMDef = builder.comment("Base magic defence of gates").define("Magic Defence", 5);
        this.spawnChance = builder.comment("Chance for next spawn (1/x chance per tick)").define("Spawn", 150);
        gateXP = builder.comment("Base xp a gate gives").define("XP", 10);
        gateMoney = builder.comment("Money a gate gives").define("Money", 4);
        builder.pop();
        for(Map.Entry<EntityType<? extends BaseMonster>, EntityProperties> e : MobConfig.propertiesMap.entrySet()){
            builder.push(e.getKey().getRegistryName().toString());
            this.mobSpecs.put(e.getKey(), new EntityPropertySpecs(builder, e.getValue()));
            builder.pop();
        }
    }

    static {
        config = new Configuration<>(MobConfigSpec::new, (p)->p.resolve(RuneCraftory.MODID).resolve("mobs.toml"), MobConfig::load, RuneCraftory.MODID);
    }
}
