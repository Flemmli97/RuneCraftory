package io.github.flemmli97.runecraftory.fabric.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.config.values.EntityProperties;
import io.github.flemmli97.runecraftory.fabric.config.values.EntityPropertySpecs;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class MobConfigSpec {

    public static final Pair<JsonConfig<CommentedJsonConfig>, MobConfigSpec> spec = CommentedJsonConfig.Builder
            .create(FabricLoader.getInstance().getConfigDir().resolve(RuneCraftory.MODID).resolve("mobs.json"), 1, MobConfigSpec::new);

    public final CommentedJsonConfig.CommentedVal<Boolean> disableNaturalSpawn;
    public final CommentedJsonConfig.IntVal farmRadius;

    public final CommentedJsonConfig.IntVal bellRadius;
    public final CommentedJsonConfig.DoubleVal gateHealth;
    public final CommentedJsonConfig.DoubleVal gateDef;
    public final CommentedJsonConfig.DoubleVal gateMDef;
    public final CommentedJsonConfig.DoubleVal gateHealthGain;
    public final CommentedJsonConfig.DoubleVal gateDefGain;
    public final CommentedJsonConfig.DoubleVal gateMDefGain;
    public final CommentedJsonConfig.IntVal gateXP;
    public final CommentedJsonConfig.IntVal gateMoney;
    public final CommentedJsonConfig.IntVal spawnChance;
    public final CommentedJsonConfig.DoubleVal minDist;
    public final CommentedJsonConfig.IntVal maxGroup;
    public final CommentedJsonConfig.IntVal maxNearby;
    public final CommentedJsonConfig.IntVal baseGateLevel;
    public final CommentedJsonConfig.CommentedVal<MobConfig.GateLevelType> gateLevelType;
    public final CommentedJsonConfig.DoubleVal treasureChance;
    public final CommentedJsonConfig.DoubleVal mimicChance;
    public final CommentedJsonConfig.DoubleVal mimicStrongChance;

    public final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

    public MobConfigSpec(CommentedJsonConfig.Builder builder) {

        builder.push("general");
        this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", MobConfig.disableNaturalSpawn);
        this.farmRadius = builder.comment("Radius in blocks for mobs to tend crops in", "Chests for seeds and drops can be placed within radius + 2").defineInRange("Farm Radius", MobConfig.farmRadius, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Gate Configs").push("gate");
        this.bellRadius = builder.comment("Radius in blocks for bells to prevent gate spawning").defineInRange("Bell Radius", MobConfig.bellRadius, 0, Integer.MAX_VALUE);
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
                "Except for CONSTANT all other types gets also a + <Gate Base Level> addition").define("Gate Level Calc", MobConfig.gateLevelType);
        this.treasureChance = builder.comment("Chance for a gate to spawn a treasure chest upon first try").defineInRange("Treasure Chest Chance", MobConfig.treasureChance, 0, 1f);
        this.mimicChance = builder.comment("Chance for a spawned treasure chest to be a monster box").defineInRange("Mimic Chance", MobConfig.mimicChance, 0, 1f);
        this.mimicStrongChance = builder.comment("Chance for a monster box to be a gobble box").defineInRange("Strong Mimic Chance", MobConfig.mimicStrongChance, 0, 1f);
        builder.pop();
        for (Map.Entry<ResourceLocation, EntityProperties> e : MobConfig.propertiesMap.entrySet()) {
            builder.push(e.getKey().toString());
            this.mobSpecs.put(e.getKey(), new EntityPropertySpecs(builder, e.getValue()));
            builder.pop();
        }
        builder.registerReloadHandler(() -> ConfigHolder.loadMobs(this));
    }
}
