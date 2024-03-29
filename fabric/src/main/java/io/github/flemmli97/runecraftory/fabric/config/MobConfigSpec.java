package io.github.flemmli97.runecraftory.fabric.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class MobConfigSpec {

    public static final Pair<JsonConfig<CommentedJsonConfig>, MobConfigSpec> SPEC = CommentedJsonConfig.Builder
            .create(FabricLoader.getInstance().getConfigDir().resolve(RuneCraftory.MODID).resolve("mobs.json"), 1, MobConfigSpec::new);

    public final CommentedJsonConfig.CommentedVal<Boolean> disableNaturalSpawn;
    public final CommentedJsonConfig.IntVal farmRadius;
    public final CommentedJsonConfig.CommentedVal<Boolean> mobAttackNPC;
    public final CommentedJsonConfig.CommentedVal<Boolean> vanillaGiveXp;
    public final CommentedJsonConfig.CommentedVal<Boolean> monsterNeedBarn;

    public final CommentedJsonConfig.IntVal bellRadius;
    public final CommentedJsonConfig.DoubleVal gateHealth;
    public final CommentedJsonConfig.DoubleVal gateDef;
    public final CommentedJsonConfig.DoubleVal gateMDef;
    public final CommentedJsonConfig.DoubleVal gateHealthGain;
    public final CommentedJsonConfig.DoubleVal gateDefGain;
    public final CommentedJsonConfig.DoubleVal gateMDefGain;
    public final CommentedJsonConfig.IntVal gateXP;
    public final CommentedJsonConfig.IntVal gateMoney;
    public final CommentedJsonConfig.IntVal minSpawnDelay;
    public final CommentedJsonConfig.IntVal maxSpawnDelay;
    public final CommentedJsonConfig.DoubleVal minDist;
    public final CommentedJsonConfig.IntVal maxGroup;
    public final CommentedJsonConfig.IntVal minNearby;
    public final CommentedJsonConfig.IntVal maxNearby;
    public final CommentedJsonConfig.IntVal baseGateLevel;
    public final CommentedJsonConfig.CommentedVal<MobConfig.GateLevelType> gateLevelType;
    public final CommentedJsonConfig.CommentedVal<List<String>> levelZones;
    public final CommentedJsonConfig.CommentedVal<MobConfig.PlayerLevelType> playerLevelType;
    public final CommentedJsonConfig.DoubleVal treasureChance;
    public final CommentedJsonConfig.DoubleVal mimicChance;
    public final CommentedJsonConfig.DoubleVal mimicStrongChance;

    public final CommentedJsonConfig.IntVal npcSpawnRateMin;
    public final CommentedJsonConfig.IntVal npcSpawnRateMax;
    public final CommentedJsonConfig.IntVal initialProcreationCooldown;
    public final CommentedJsonConfig.IntVal procreationCooldown;

    public MobConfigSpec(CommentedJsonConfig.Builder builder) {

        builder.push("general");
        this.disableNaturalSpawn = builder.comment("Disable all spawning not from gates").define("Disable Spawn", MobConfig.disableNaturalSpawn);
        this.farmRadius = builder.comment("Radius in blocks for mobs to tend crops in", "Chests for seeds and drops can be placed within radius + 2").defineInRange("Farm Radius", MobConfig.farmRadius, 0, Integer.MAX_VALUE);
        this.mobAttackNPC = builder.comment("If monsters should attack npcs/villagers. Note if an npc follows you they will always attack").define("Attack NPC", MobConfig.mobAttackNpc);
        this.vanillaGiveXp = builder.comment("If true other mobs also give xp. The amount is based on the health they have").define("Vanilla mobs give XP", MobConfig.vanillaGiveXp);
        this.monsterNeedBarn = builder.comment("If true monster need to have a barn assigned or other interactions will not be possible").define("Monster need barn", MobConfig.monsterNeedBarn);
        this.npcSpawnRateMin = builder.comment("Min spawn rate of npc in villages").defineInRange("NPC Spawnrate Min", MobConfig.npcSpawnRateMin, 0, Integer.MAX_VALUE);
        this.npcSpawnRateMax = builder.comment("Max spawn rate of npc in villages").defineInRange("NPC Spawnrate Max", MobConfig.npcSpawnRateMax, 0, Integer.MAX_VALUE);
        this.initialProcreationCooldown = builder.comment("Initial procreation cooldown after marrying an npc").defineInRange("Initial procreation cooldown", MobConfig.initialProcreationCooldown, 0, Integer.MAX_VALUE);
        this.procreationCooldown = builder.comment("Cooldown for procreating with an npc").defineInRange("Procreation cooldown", MobConfig.procreationCooldown, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.comment("Gate Configs").push("gate");
        this.bellRadius = builder.comment("Radius in blocks for bells to prevent gate spawning").defineInRange("Bell Radius", MobConfig.bellRadius, 0, Integer.MAX_VALUE);
        this.gateHealth = builder.comment("Base health of gates").defineInRange("Health", MobConfig.gateHealth, 0, Double.MAX_VALUE);
        this.gateDef = builder.comment("Base defence of gates").defineInRange("Defence", MobConfig.gateDef, 0, Double.MAX_VALUE);
        this.gateMDef = builder.comment("Base magic defence of gates").defineInRange("Magic Defence", MobConfig.gateDef, 0, Double.MAX_VALUE);
        this.gateHealthGain = builder.comment("Health gain per level of gates").defineInRange("Health Gain", MobConfig.gateHealthGain, 0, Double.MAX_VALUE);
        this.gateDefGain = builder.comment("Defence gain per level of gates").defineInRange("Defence Gain", MobConfig.gateDefGain, 0, Double.MAX_VALUE);
        this.gateMDefGain = builder.comment("Magic defence gain per level of gates").defineInRange("Magic Defence Gain", MobConfig.gateMDef, 0, Double.MAX_VALUE);
        this.gateXP = builder.comment("Base xp a gate gives").defineInRange("XP", MobConfig.gateXp, 0, Integer.MAX_VALUE);
        this.gateMoney = builder.comment("Money a gate gives").defineInRange("Money", MobConfig.gateMoney, 0, Integer.MAX_VALUE);
        this.minSpawnDelay = builder.comment("Min delay in ticks for gates to spawn the next mob").defineInRange("MinSpawnDelay", MobConfig.minSpawnDelay, 0, Integer.MAX_VALUE);
        this.maxSpawnDelay = builder.comment("Max delay in ticks for gates to spawn the next mob").defineInRange("MaxSpawnDelay", MobConfig.maxSpawnDelay, 0, Integer.MAX_VALUE);
        this.minDist = builder.comment("Radius to check for other gates. If more than Max Group gates are in that radius no other gates will spawn").defineInRange("Min Dist", MobConfig.minDist, 0, Double.MAX_VALUE);
        this.maxGroup = builder.comment("Max amount of gates in Min Dist radius that can exist").defineInRange("Max Group", MobConfig.maxGroup, 0, Integer.MAX_VALUE);
        this.minNearby = builder.comment("See Max Nearby").defineInRange("Min Nearby", MobConfig.minNearby, 0, Integer.MAX_VALUE);
        this.maxNearby = builder.comment("When spawning gates roll a random number between Min Nearby and this to find the max amount of monsters nearby to stop spawning.").defineInRange("Max Nearby", MobConfig.maxNearby, 0, Integer.MAX_VALUE);
        this.baseGateLevel = builder.comment("Base level for gates. Level will be at least 1").defineInRange("Gate Base Level", MobConfig.baseGateLevel, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.gateLevelType = builder.comment("How the level of a gate is calculated.",
                "Also see -Player Level Type- config",
                "CONSTANT: Gate level is simply the value defined in <Gate Base Level>",
                "DISTANCESPAWN: The further away from spawn a gate is the stronger it gets",
                "DISTANCESPAWNPLAYER: The further away from a players spawn (or world spawn if not exist) a gate is the stronger it gets. Highest level of nearby player counts",
                "PLAYERLEVEL: The players level will be used to calculate the gates level",
                "The final gate level gets a 10% randomizer").define("Gate Level Calc", MobConfig.gateLevelType);
        this.levelZones = builder.comment("Define zones for levels here.",
                "Gate Level Calc needs to be either DISTANCESPAWN or DISTANCESPAWNPLAYER.",
                "The an entry is defined as: x-y-z",
                "x: The distance at which this zone gets used",
                "y: The starting level of that zone",
                "z: The increase of level per distance away from the center.",
                "E.g. 5-10-0.01 means a zone that starts at level 10 and from 5 blocks away from the center and the level will increase by 0.01 per block out.").define("Gate Zones", MobConfig.levelZones.writeToString());
        this.playerLevelType = builder.comment("How players in a 256 radius influence the calculation of a gates level",
                "For INCREASED an internal player specific value will be added on. That value is increased e.g. by defeating bosses.",
                "MEAN: The mean of all players will be used. Does nothing if Gate Level Calc is CONSTANT or DISTANCESPAWN",
                "MEANINCREASED: The mean of all players will be used.",
                "MAX: The max of all players will be used. Does nothing if Gate Level Calc is CONSTANT or DISTANCESPAWN",
                "MAXINCREASED: The max of all players will be used.").define("Player Level Type", MobConfig.playerLevelType);
        this.treasureChance = builder.comment("Chance for a gate to spawn a treasure chest upon first try").defineInRange("Treasure Chest Chance", MobConfig.treasureChance, 0, 1f);
        this.mimicChance = builder.comment("Chance for a spawned treasure chest to be a monster box").defineInRange("Mimic Chance", MobConfig.mimicChance, 0, 1f);
        this.mimicStrongChance = builder.comment("Chance for a monster box to be a gobble box").defineInRange("Strong Mimic Chance", MobConfig.mimicStrongChance, 0, 1f);
        builder.pop();
        builder.registerReloadHandler(() -> ConfigHolder.loadMobs(this));
    }
}
