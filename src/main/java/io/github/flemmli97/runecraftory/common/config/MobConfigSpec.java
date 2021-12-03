package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.common.config.values.EntityPropertySpecs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class MobConfigSpec {

    public static final Pair<MobConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(MobConfigSpec::new);

    public final ForgeConfigSpec.BooleanValue disableNaturalSpawn;
    public final ForgeConfigSpec.ConfigValue<Integer> gateHealth;
    public final ForgeConfigSpec.ConfigValue<Integer> gateDef;
    public final ForgeConfigSpec.ConfigValue<Integer> gateMDef;
    public final ForgeConfigSpec.ConfigValue<Integer> gateHealthGain;
    public final ForgeConfigSpec.ConfigValue<Integer> gateDefGain;
    public final ForgeConfigSpec.ConfigValue<Integer> gateMDefGain;
    public final ForgeConfigSpec.ConfigValue<Integer> spawnChance;
    public final ForgeConfigSpec.ConfigValue<Integer> gateXP;
    public final ForgeConfigSpec.ConfigValue<Integer> gateMoney;

    public final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

    public MobConfigSpec(ForgeConfigSpec.Builder builder) {
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
