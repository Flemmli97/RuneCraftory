package io.github.flemmli97.runecraftory.forge.config;

import io.github.flemmli97.runecraftory.common.config.MobConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.function.Predicate;

public class MobConfigSpec {

    public static final Pair<MobConfigSpec, ForgeConfigSpec> SPEC = new ForgeConfigSpec.Builder().configure(MobConfigSpec::new);

    public final ForgeConfigSpec.BooleanValue disableNaturalSpawn;
    public final ForgeConfigSpec.IntValue farmRadius;
    public final ForgeConfigSpec.BooleanValue mobAttackNPC;
    public final ForgeConfigSpec.BooleanValue vanillaGiveXp;
    public final ForgeConfigSpec.BooleanValue monsterNeedBarn;

    public final ForgeConfigSpec.IntValue bellRadius;
    public final ForgeConfigSpec.DoubleValue gateHealth;
    public final ForgeConfigSpec.DoubleValue gateDef;
    public final ForgeConfigSpec.DoubleValue gateMDef;
    public final ForgeConfigSpec.DoubleValue gateHealthGain;
    public final ForgeConfigSpec.DoubleValue gateDefGain;
    public final ForgeConfigSpec.DoubleValue gateMDefGain;
    public final ForgeConfigSpec.IntValue gateXP;
    public final ForgeConfigSpec.IntValue gateMoney;
    public final ForgeConfigSpec.IntValue minSpawnDelay;
    public final ForgeConfigSpec.IntValue maxSpawnDelay;
    public final ForgeConfigSpec.DoubleValue minDist;
    public final ForgeConfigSpec.IntValue maxGroup;
    public final ForgeConfigSpec.IntValue minNearby;
    public final ForgeConfigSpec.IntValue maxNearby;
    public final ForgeConfigSpec.IntValue baseGateLevel;
    public final ForgeConfigSpec.EnumValue<MobConfig.GateLevelType> gateLevelType;
    public final ForgeConfigSpec.ConfigValue<List<String>> levelZones;
    public final ForgeConfigSpec.EnumValue<MobConfig.PlayerLevelType> playerLevelType;
    public final ForgeConfigSpec.DoubleValue treasureChance;
    public final ForgeConfigSpec.DoubleValue mimicChance;
    public final ForgeConfigSpec.DoubleValue mimicStrongChance;

    public final ForgeConfigSpec.IntValue npcSpawnRateMin;
    public final ForgeConfigSpec.IntValue npcSpawnRateMax;
    public final ForgeConfigSpec.IntValue initialProcreationCooldown;
    public final ForgeConfigSpec.IntValue procreationCooldown;

    public MobConfigSpec(ForgeConfigSpec.Builder builder) {
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
        this.gateMDef = builder.comment("Base magic defence of gates").defineInRange("Magic Defence", MobConfig.gateMDef, 0, Double.MAX_VALUE);
        this.gateHealthGain = builder.comment("Health gain per level of gates").defineInRange("Health Gain", MobConfig.gateHealthGain, 0, Double.MAX_VALUE);
        this.gateDefGain = builder.comment("Defence gain per level of gates").defineInRange("Defence Gain", MobConfig.gateDefGain, 0, Double.MAX_VALUE);
        this.gateMDefGain = builder.comment("Magic defence gain per level of gates").defineInRange("Magic Defence Gain", MobConfig.gateMDefGain, 0, Double.MAX_VALUE);
        this.gateXP = builder.comment("Base xp a gate gives").defineInRange("XP", MobConfig.gateXp, 0, Integer.MAX_VALUE);
        this.gateMoney = builder.comment("Money a gate gives").defineInRange("Money", MobConfig.gateMoney, 0, Integer.MAX_VALUE);
        this.minSpawnDelay = builder.comment("Min delay in ticks for gates to spawn the next mob").defineInRange("MinSpawnDelay", MobConfig.minSpawnDelay, 0, Integer.MAX_VALUE);
        this.maxSpawnDelay = builder.comment("Max delay in ticks for gates to spawn the next mob").defineInRange("MaxSpawnDelay", MobConfig.maxSpawnDelay, 0, Integer.MAX_VALUE);
        this.minDist = builder.comment("Radius to check for other gates. If more than Max Group gates are in that radius no other gates will spawn").defineInRange("Min Dist", MobConfig.minDist, 0, Double.MAX_VALUE);
        this.maxGroup = builder.comment("Max amount of gates in Min Dist radius that can exist").defineInRange("Max Group", MobConfig.maxGroup, 0, Integer.MAX_VALUE);
        this.minNearby = builder.comment("See Max Nearby").defineInRange("Min Nearby", MobConfig.minNearby, 0, Integer.MAX_VALUE);
        this.maxNearby = builder.comment("When spawning gates roll a random number between Min Nearby and this to find the max amount of monsters nearby to stop spawning.").defineInRange("Max Nearby", MobConfig.maxNearby, 0, Integer.MAX_VALUE);
        this.baseGateLevel = builder.comment("Base level for gates").defineInRange("Gate Base Level", MobConfig.baseGateLevel, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.gateLevelType = builder.comment("How the level of a gate is calculated.",
                "Also see -Player Level Type- config",
                "CONSTANT: Gate level is simply the value defined in <Gate Base Level>",
                "DISTANCESPAWN: The further away from spawn a gate is the stronger it gets",
                "DISTANCESPAWNPLAYER: The further away from a players spawn (or world spawn if not exist) a gate is the stronger it gets. Highest level of nearby player counts",
                "PLAYERLEVEL: The players level will be used to calculate the gates level",
                "The final gate level gets a 10% randomizer").defineEnum("Gate Level Calc", MobConfig.gateLevelType);
        this.levelZones = builder.comment("Define zones for levels here.",
                "Gate Level Calc needs to be either DISTANCESPAWN or DISTANCESPAWNPLAYER.",
                "The an entry is defined as: x-y-z",
                "x: The distance at which this zone gets used",
                "y: The starting level of that zone",
                "z: The increase of level per distance away from the center.",
                "E.g. 5-10-0.01 means a zone that starts at level 10 and from 5 blocks away from the center and the level will increase by 0.01 per block out.").define("Gate Zones", MobConfig.levelZones.writeToString(), stringList());
        this.playerLevelType = builder.comment("How players in a 256 radius influence the calculation of a gates level",
                "For INCREASED an internal player specific value will be added on. That value is increased e.g. by defeating bosses.",
                "MEAN: The mean of all players will be used. Does nothing if Gate Level Calc is CONSTANT or DISTANCESPAWN",
                "MEANINCREASED: The mean of all players will be used.",
                "MAX: The max of all players will be used. Does nothing if Gate Level Calc is CONSTANT or DISTANCESPAWN",
                "MAXINCREASED: The max of all players will be used.").defineEnum("Player Level Type", MobConfig.playerLevelType);
        this.treasureChance = builder.comment("Chance for a gate to spawn a treasure chest upon first try").defineInRange("Treasure Chest Chance", MobConfig.treasureChance, 0, 1f);
        this.mimicChance = builder.comment("Chance for a spawned treasure chest to be a monster box").defineInRange("Mimic Chance", MobConfig.mimicChance, 0, 1f);
        this.mimicStrongChance = builder.comment("Chance for a monster box to be a gobble box").defineInRange("Strong Mimic Chance", MobConfig.mimicStrongChance, 0, 1f);
        builder.pop();
    }

    private static Predicate<Object> stringList() {
        return p -> p instanceof List<?> list && list.stream().allMatch(e -> e instanceof String);
    }
}
