package io.github.flemmli97.runecraftory.common.config;

import com.flemmli97.tenshilib.common.config.Configuration;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.config.values.EntityPropertySpecs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class MobConfig {

    public static boolean disableNaturalSpawn;

    public static float gateDef;
    public static float gateMDef;
    public static float gateHealth;
    public static float gateDefGain;
    public static float gateMDefGain;
    public static float gateHealthGain;
    public static int gateXP;
    public static int gateMoney;
    public static int spawnChance;

    public static Map<ResourceLocation, EntityProperties> propertiesMap = new HashMap<>();

    public static void load(MobConfigSpec spec) {
        disableNaturalSpawn = spec.disableNaturalSpawn.get();
        gateHealth = spec.gateHealth.get();
        gateDef = spec.gateDef.get();
        gateMDef = spec.gateMDef.get();
        gateHealthGain = spec.gateHealthGain.get();
        gateDefGain = spec.gateDefGain.get();
        gateMDefGain = spec.gateMDefGain.get();
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
        private final ForgeConfigSpec.ConfigValue<Integer> gateHealthGain;
        private final ForgeConfigSpec.ConfigValue<Integer> gateDefGain;
        private final ForgeConfigSpec.ConfigValue<Integer> gateMDefGain;
        private final ForgeConfigSpec.ConfigValue<Integer> spawnChance;
        private final ForgeConfigSpec.ConfigValue<Integer> gateXP;
        private final ForgeConfigSpec.ConfigValue<Integer> gateMoney;

        private final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

        private MobConfigSpec(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", false);
            builder.pop();

            builder.comment("Gate Configs").push("gate");
            this.gateHealth = builder.comment("Base health of gates").define("Health", 100);
            this.gateDef = builder.comment("Base defence of gates").define("Defence", 0);
            this.gateMDef = builder.comment("Base magic defence of gates").define("Magic Defence", 0);
            this.gateHealthGain = builder.comment("Health gain per level of gates").define("Health Gain", 25);
            this.gateDefGain = builder.comment("Defence gain per level of gates").define("Defence Gain", 5);
            this.gateMDefGain = builder.comment("Magic defence gain per level of gates").define("Magic Defence Gain", 5);
            this.spawnChance = builder.comment("Chance for next spawn (1/x chance per tick)").define("Spawn", 150);
            this.gateXP = builder.comment("Base xp a gate gives").define("XP", 12);
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
