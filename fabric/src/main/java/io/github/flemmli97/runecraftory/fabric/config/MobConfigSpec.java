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

    public final Map<ResourceLocation, EntityPropertySpecs> mobSpecs = new HashMap<>();

    public MobConfigSpec(CommentedJsonConfig.Builder builder) {
        builder.push("general");
        this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", false);
        builder.pop();

        builder.comment("Gate Configs").push("gate");
        this.gateHealth = builder.comment("Base health of gates").defineInRange("Health", 100, 0, Double.MAX_VALUE);
        this.gateDef = builder.comment("Base defence of gates").defineInRange("Defence", 0, 0, Double.MAX_VALUE);
        this.gateMDef = builder.comment("Base magic defence of gates").defineInRange("Magic Defence", 0, 0, Double.MAX_VALUE);
        this.gateHealthGain = builder.comment("Health gain per level of gates").defineInRange("Health Gain", 25, 0, Double.MAX_VALUE);
        this.gateDefGain = builder.comment("Defence gain per level of gates").defineInRange("Defence Gain", 5, 0, Double.MAX_VALUE);
        this.gateMDefGain = builder.comment("Magic defence gain per level of gates").defineInRange("Magic Defence Gain", 5, 0, Double.MAX_VALUE);
        this.gateXP = builder.comment("Base xp a gate gives").defineInRange("XP", 12, 0, Integer.MAX_VALUE);
        this.gateMoney = builder.comment("Money a gate gives").defineInRange("Money", 4, 0, Integer.MAX_VALUE);
        this.spawnChance = builder.comment("Chance for next spawn (1/x chance per tick)").defineInRange("Spawn", 150, 0, Integer.MAX_VALUE);
        this.minDist = builder.comment("Radius to check for other gates. If more than Max Group gates are in that radius no other gates will spawn").defineInRange("Min Dist", 48d, 0, Double.MAX_VALUE);
        this.maxGroup = builder.comment("Max amount of gates in Min Dist radius that can exist").defineInRange("Max Group", 2, 0, Integer.MAX_VALUE);
        this.maxNearby = builder.comment("If more than x mobs are near a gate the gate will not spawn more").defineInRange("Max Nearby", 4, 0, Integer.MAX_VALUE);

        builder.pop();
        for (Map.Entry<ResourceLocation, EntityProperties> e : MobConfig.propertiesMap.entrySet()) {
            builder.push(e.getKey().toString());
            this.mobSpecs.put(e.getKey(), new EntityPropertySpecs(builder, e.getValue()));
            builder.pop();
        }
        builder.registerReloadHandler(() -> ConfigHolder.loadMobs(this));
    }
}
