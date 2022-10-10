package io.github.flemmli97.runecraftory.forge.config;

import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.forge.config.values.EntityPropertySpecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class MobConfigSpec {

    public static final Pair<MobConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(MobConfigSpec::new);

    public final ForgeConfigSpec.BooleanValue disableNaturalSpawn;
    public final ForgeConfigSpec.IntValue farmRadius;

    public final ForgeConfigSpec.DoubleValue gateHealth;
    public final ForgeConfigSpec.DoubleValue gateDef;
    public final ForgeConfigSpec.DoubleValue gateMDef;
    public final ForgeConfigSpec.DoubleValue gateHealthGain;
    public final ForgeConfigSpec.DoubleValue gateDefGain;
    public final ForgeConfigSpec.DoubleValue gateMDefGain;
    public final ForgeConfigSpec.IntValue gateXP;
    public final ForgeConfigSpec.IntValue gateMoney;
    public final ForgeConfigSpec.IntValue spawnChance;
    public final ForgeConfigSpec.DoubleValue minDist;
    public final ForgeConfigSpec.IntValue maxGroup;
    public final ForgeConfigSpec.IntValue maxNearby;
    public final ForgeConfigSpec.IntValue baseGateLevel;
    public final ForgeConfigSpec.EnumValue<MobConfig.GateLevelType> gateLevelType;
    public final ForgeConfigSpec.DoubleValue treasureChance;
    public final ForgeConfigSpec.DoubleValue mimicChance;
    public final ForgeConfigSpec.DoubleValue mimicStrongChance;

    public final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

    public MobConfigSpec(ForgeConfigSpec.Builder builder) {
        builder.push("general");
        this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", MobConfig.disableNaturalSpawn);
        this.farmRadius = builder.comment("Radius in blocks for mobs to tend crops in", "Chests for seeds and drops can be placed within radius + 2").defineInRange("Farm Radius", MobConfig.farmRadius, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Gate Configs").push("gate");
        this.gateHealth = builder.comment("Base health of gates").defineInRange("Health", MobConfig.gateHealth, 0, Double.MAX_VALUE);
        this.gateDef = builder.comment("Base defence of gates").defineInRange("Defence", MobConfig.gateDef, 0, Double.MAX_VALUE);
        this.gateMDef = builder.comment("Base magic defence of gates").defineInRange("Magic Defence", MobConfig.gateMDef, 0, Double.MAX_VALUE);
        this.gateHealthGain = builder.comment("Health gain per level of gates").defineInRange("Health Gain", MobConfig.gateHealthGain, 0, Double.MAX_VALUE);
        this.gateDefGain = builder.comment("Defence gain per level of gates").defineInRange("Defence Gain", MobConfig.gateDefGain, 0, Double.MAX_VALUE);
        this.gateMDefGain = builder.comment("Magic defence gain per level of gates").defineInRange("Magic Defence Gain", MobConfig.gateMDefGain, 0, Double.MAX_VALUE);
        this.gateXP = builder.comment("Base xp a gate gives").defineInRange("XP", MobConfig.gateXP, 0, Integer.MAX_VALUE);
        this.gateMoney = builder.comment("Money a gate gives").defineInRange("Money", MobConfig.gateMoney, 0, Integer.MAX_VALUE);
        this.spawnChance = builder.comment("Chance for next spawn (1/x chance per tick)").defineInRange("Spawn", MobConfig.spawnChance, 0, Integer.MAX_VALUE);
        this.minDist = builder.comment("Radius to check for other gates. If more than Max Group gates are in that radius no other gates will spawn").defineInRange("Min Dist", MobConfig.minDist, 0, Double.MAX_VALUE);
        this.maxGroup = builder.comment("Max amount of gates in Min Dist radius that can exist").defineInRange("Max Group", MobConfig.maxGroup, 0, Integer.MAX_VALUE);
        this.maxNearby = builder.comment("If more than x mobs are near a gate the gate will not spawn more").defineInRange("Max Nearby", MobConfig.maxNearby, 0, Integer.MAX_VALUE);
        this.baseGateLevel = builder.comment("Base level for gates").defineInRange("Gate Base Level", MobConfig.baseGateLevel, 1, Integer.MAX_VALUE);
        this.gateLevelType = builder.comment("How the level of a gate is calculated.",
                "CONSTANT: Gate level is simply the value defined in <Gate Base Level>",
                "DISTANCESPAWN: The further away from spawn a gate is the stronger it gets",
                "PLAYERLEVELMAX: The player in a 256 radius with the highest level defines how strong a gate is",
                "PLAYERLEVELMEAN: Average player level in a 256 radius is considered",
                "Except for CONSTANT all other types gets also a + <Gate Base Level> addition").defineEnum("Gate Level Calc", MobConfig.gateLevelType);
        this.treasureChance = builder.comment("Chance for a gate to spawn a treasure chest upon first try").defineInRange("Treasure Chest Chance", MobConfig.treasureChance, 0, 1f);
        this.mimicChance = builder.comment("Chance for a spawned treasure chest to be a monster box").defineInRange("Mimic Chance", MobConfig.mimicChance, 0, 1f);
        this.mimicStrongChance = builder.comment("Chance for a monster box to be a gobble box").defineInRange("Strong Mimic Chance", MobConfig.mimicStrongChance, 0, 1f);
        builder.pop();
        for (Map.Entry<ResourceLocation, EntityProperties> e : MobConfig.propertiesMap.entrySet()) {
            builder.push(e.getKey().toString());
            this.mobSpecs.put(e.getKey(), new EntityPropertySpecs(builder, e.getValue()));
            builder.pop();
        }
    }
}
