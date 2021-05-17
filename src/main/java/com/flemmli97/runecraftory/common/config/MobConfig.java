package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.values.EntityProperties;
import com.flemmli97.runecraftory.common.config.values.EntityPropertySpecs;
import com.flemmli97.tenshilib.common.config.Configuration;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn;

    public static float gateDef = 5f;
    public static float gateMDef = 5f;
    public static float gateHealth = 100;
    public static int gateXP = 10;
    public static int gateMoney = 4;
    public static int spawnChance = 150;

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public static void load(MobConfigSpec spec) {
        disableNaturalSpawn = spec.disableNaturalSpawn.get();
        gateHealth = spec.gateHealth.get();
        gateDef = spec.gateDef.get();
        gateMDef = spec.gateMDef.get();
        spawnChance = spec.spawnChance.get();
        gateXP = spec.gateXP.get();
        gateMoney = spec.gateMoney.get();

        for (Map.Entry<ResourceLocation, EntityPropertySpecs> e : spec.mobSpecs.entrySet()) {
            propertiesMap.merge(e.getKey(), new EntityProperties.Builder().build().read(e.getValue()), (old, v) -> old.read(e.getValue()));
        }
    }

    public static class MobConfigSpec {

        public static final Configuration<MobConfigSpec> config = new Configuration<>(MobConfigSpec::new, (p) -> p.resolve(RuneCraftory.MODID).resolve("mobs.toml"), MobConfig::load, RuneCraftory.MODID);

        private final ForgeConfigSpec.BooleanValue disableNaturalSpawn;
        private final ForgeConfigSpec.ConfigValue<Integer> gateHealth;
        private final ForgeConfigSpec.ConfigValue<Integer> gateDef;
        private final ForgeConfigSpec.ConfigValue<Integer> gateMDef;
        private final ForgeConfigSpec.ConfigValue<Integer> spawnChance;
        private final ForgeConfigSpec.ConfigValue<Integer> gateXP;
        private final ForgeConfigSpec.ConfigValue<Integer> gateMoney;

        private final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

        private MobConfigSpec(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", false);
            builder.pop();

            builder.comment("Gate Configs").push("gate");
            this.gateHealth = builder.comment("Base health of gates").define("Health", 75);
            this.gateDef = builder.comment("Base defence of gates").define("Defence", 5);
            this.gateMDef = builder.comment("Base magic defence of gates").define("Magic Defence", 5);
            this.spawnChance = builder.comment("Chance for next spawn (1/x chance per tick)").define("Spawn", 150);
            this.gateXP = builder.comment("Base xp a gate gives").define("XP", 10);
            this.gateMoney = builder.comment("Money a gate gives").define("Money", 4);
            builder.pop();
            for (Map.Entry<ResourceLocation, EntityProperties> e : MobConfig.propertiesMap.entrySet()) {
                builder.push(e.getKey().toString());
                this.mobSpecs.put(e.getKey(), new EntityPropertySpecs(builder, e.getValue()));
                builder.pop();
            }
        }
    }
}
