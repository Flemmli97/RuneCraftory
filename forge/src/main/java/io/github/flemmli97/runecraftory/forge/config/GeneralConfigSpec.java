package io.github.flemmli97.runecraftory.forge.config;


import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class GeneralConfigSpec {

    public static final Pair<GeneralConfigSpec, ForgeConfigSpec> SPEC = new ForgeConfigSpec.Builder().configure(GeneralConfigSpec::new);

    public final ForgeConfigSpec.EnumValue<GeneralConfig.DefenceSystem> defenceSystem;
    public final ForgeConfigSpec.BooleanValue gateSpawning;
    public final ForgeConfigSpec.BooleanValue disableVanillaSpawning;
    public final ForgeConfigSpec.BooleanValue randomDamage;
    public final ForgeConfigSpec.EnumValue<GeneralConfig.RecipeSystem> recipeSystem;
    public final ForgeConfigSpec.BooleanValue useRP;
    public final ForgeConfigSpec.DoubleValue deathHPPercent;
    public final ForgeConfigSpec.DoubleValue deathRPPercent;
    public final ForgeConfigSpec.BooleanValue disableHunger;
    public final ForgeConfigSpec.BooleanValue modifyWeather;
    public final ForgeConfigSpec.BooleanValue modifyBed;
    public final ForgeConfigSpec.BooleanValue healOnWakeUp;
    public final ForgeConfigSpec.BooleanValue disableFoodSystem;
    public final ForgeConfigSpec.BooleanValue disableItemStatSystem;
    public final ForgeConfigSpec.BooleanValue disableCropSystem;
    public final ForgeConfigSpec.BooleanValue seasonedSnow;
    public final ForgeConfigSpec.IntValue maxPartySize;

    public final ForgeConfigSpec.DoubleValue witherChance;
    public final ForgeConfigSpec.DoubleValue runeyChance;
    public final ForgeConfigSpec.BooleanValue disableFarmlandRandomtick;
    public final ForgeConfigSpec.BooleanValue disableFarmlandTrample;
    public final ForgeConfigSpec.BooleanValue tickUnloadedFarmland;
    public final ForgeConfigSpec.BooleanValue unloadedFarmlandCheckWater;

    public final boolean waila = true;
    public final boolean jei = true;
    public final boolean harvestCraft = true;
    public final boolean seasons = true;
    public final boolean dynamicTrees = true;

    public final ForgeConfigSpec.IntValue maxLevel;
    public final ForgeConfigSpec.IntValue startingHealth;
    public final ForgeConfigSpec.IntValue startingRP;
    public final ForgeConfigSpec.IntValue startingMoney;
    public final ForgeConfigSpec.IntValue startingStr;
    public final ForgeConfigSpec.IntValue startingVit;
    public final ForgeConfigSpec.IntValue startingIntel;
    public final ForgeConfigSpec.DoubleValue hpPerLevel;
    public final ForgeConfigSpec.DoubleValue rpPerLevel;
    public final ForgeConfigSpec.DoubleValue strPerLevel;
    public final ForgeConfigSpec.DoubleValue vitPerLevel;
    public final ForgeConfigSpec.DoubleValue intPerLevel;
    public final ForgeConfigSpec.DoubleValue shortSwordUltimate;
    public final ForgeConfigSpec.DoubleValue longSwordUltimate;
    public final ForgeConfigSpec.DoubleValue spearUltimate;
    public final ForgeConfigSpec.DoubleValue hammerAxeUltimate;
    public final ForgeConfigSpec.DoubleValue dualBladeUltimate;
    public final ForgeConfigSpec.DoubleValue gloveUltimate;

    public final ForgeConfigSpec.DoubleValue platinumChargeTime;
    public final ForgeConfigSpec.IntValue scrapWateringCanWater;
    public final ForgeConfigSpec.IntValue ironWateringCanWater;
    public final ForgeConfigSpec.IntValue silverWateringCanWater;
    public final ForgeConfigSpec.IntValue goldWateringCanWater;
    public final ForgeConfigSpec.IntValue platinumWateringCanWater;
    public final ForgeConfigSpec.BooleanValue allowMoveOnAttack;

    public final ForgeConfigSpec.DoubleValue xpMultiplier;
    public final ForgeConfigSpec.DoubleValue skillXpMultiplier;
    public final ForgeConfigSpec.DoubleValue tamingMultiplier;

    public final ForgeConfigSpec.BooleanValue debugMode;
    public final ForgeConfigSpec.BooleanValue debugAttack;

    private GeneralConfigSpec(ForgeConfigSpec.Builder builder) {
        builder.comment("The current default values reflect a near vanilla experience since quite a few things are not done yet to my satisfaction.",
                "You can freely change the things here and they will work but you might also consider tweaking things for balancing").push("Modules");
        this.defenceSystem = builder.comment("How the defence stat in from this mod should be handled",
                        "NO_DEFENCE = Defence is ignored. Raising defence will do nothing",
                        "VANILLA_IGNORE: Damage sources not from this mod will ignore defence on attack except",
                        "IGNORE_VANILLA_MOBS = Entity damage sources not from this mod will ignore defence on attack except if its from a player",
                        "IGNORE_VANILLA_PLAYER_ATT = Player damage sources not from this (vanilla attacks) will ignore defence",
                        "IGNORE_VANILLA_PLAYER_HURT = Entity damage sources not from this will ignore defence if hitting the player",
                        "IGNORE_VANILLA_PLAYER = Combines IGNORE_VANILLA_PLAYER_ATT and IGNORE_VANILLA_PLAYER_HURT")
                .defineEnum("Defence System", GeneralConfig.defenceSystem);
        this.gateSpawning = builder.comment("Should gates spawn? If disabled will also disable all mobs from this mod to spawn. Needs server restart").define("Gate Spawning", GeneralConfig.gateSpawning);
        this.disableVanillaSpawning = builder.comment("If enabled mobs can only spawn through gates.").define("Disable vanilla spawn", GeneralConfig.disableVanillaSpawning);
        this.randomDamage = builder.comment("If enabled damage gets a +-10% randomness.").define("Random Damage", GeneralConfig.randomDamage);
        this.recipeSystem = builder.comment("The recipe system to use.",
                        "SKILL = Crafting skill influence the rp cost. Not unlocked recipes will cost more",
                        "SKILLIGNORELOCK = Crafting skill influence the rp cost and unlock system is ignored",
                        "SKILLBLOCKLOCK = Crafting skill influence the rp cost and locked recipes are not craftable at all",
                        "BASE = Rp cost is a fixed base cost but locked recipes cost more",
                        "BASEIGNORELOCK = Rp cost is a fixed base cost and unlock system is ignored",
                        "BASEBLOCKLOCK = Base cost and locked recipes are uncraftable")
                .defineEnum("Recipe System", GeneralConfig.recipeSystem);
        this.useRP = builder.comment("If actions consume rune points").define("Use RunePoints", GeneralConfig.useRP);
        this.deathHPPercent = builder.comment("Percent of HP that will be regenerated on death").defineInRange("Death HP Percent", GeneralConfig.deathHPPercent, 0, 1);
        this.deathRPPercent = builder.comment("Percent of RP that will be regenerated on death").defineInRange("Death RP Percent", GeneralConfig.deathRPPercent, 0, 1);
        this.disableHunger = builder.comment("Disable mc hunger system. Makes it so you can always eat food. Also disables hunger bar rendering on client").define("Disable Hunger", GeneralConfig.disableHunger);
        this.modifyWeather = builder.comment("If true weather will change only at specific time.", "Morning, mid day, evening and mid night").define("Modify Weather", GeneralConfig.modifyWeather);
        this.modifyBed = builder.comment("If true players can sleep anytime and upon waking up will restore all health and rp").define("Modify Bed", GeneralConfig.modifyBed);
        this.healOnWakeUp = builder.comment("If true players heal to full hp and rp upon waking up").define("Heal On Wake", GeneralConfig.healOnWakeUp);
        this.disableFoodSystem = builder.comment("If true food will not provide benefits such as hp restoration etc defined per datapack.").define("Disable Food System", GeneralConfig.disableFoodSystem);
        this.disableItemStatSystem = builder.comment("If true item get no stats assigned as defined per datapack. ", "Note: Weapons will then do no damage").define("Disable Item Stat System", GeneralConfig.disableItemStatSystem);
        this.disableCropSystem = builder.comment("If true crop data will be disabled.", "Note: Crops from this mod will not function anymore").define("Disable Crop System", GeneralConfig.disableCropSystem);
        this.seasonedSnow = builder.comment("If biome temperature should be adjusted based on current season. Can cause snowfall during winter").define("Seasoned Biome Temp", GeneralConfig.seasonedSnow);
        this.maxPartySize = builder.comment("Max size of a players party (Entities that follow you). Set to 0 for no limit").defineInRange("Max Party Size", GeneralConfig.maxPartySize, 0, Integer.MAX_VALUE);
        builder.pop();

        builder.push("Farming");
        this.witherChance = builder.comment("Change for a crop to wither if its not been watered", "If crop is already withered it and it doesnt get watered it will turn into withered grass").defineInRange("Wither Chance", GeneralConfig.witherChance, 0, 1);
        this.runeyChance = builder.comment("Chance for a runey to spawn when harvesting fully grown crops").defineInRange("Runey Chance", GeneralConfig.runeyChance, 0, 1);
        this.disableFarmlandRandomtick = builder.comment("If true farmland dont get random ticked. Which means nearby water don't water it and it doesn't turn to dirt if there is no water", "You would need to manually water the farmland").define("Disable farmland random ticks", GeneralConfig.disableFarmlandRandomtick);
        this.disableFarmlandTrample = builder.comment("If true disables trampling of farmland").define("Disable farmland trample", GeneralConfig.disableFarmlandTrample);
        this.tickUnloadedFarmland = builder.comment("If true unloaded farmland gets ticked. So crops there will grow without it being loaded", "This also means that without water crops will wilt and die").define("Tick unloaded farmland", GeneralConfig.tickUnloadedFarmland);
        this.unloadedFarmlandCheckWater = builder.comment("If true when loading farmland it will not check if the farmland had water during all the unloaded time.", "If the farmland is loaded it will still need water").define("Unloaded farmland check water", GeneralConfig.unloadedFarmlandCheckWater);
        builder.pop();

        builder.push("Multipliers");
        this.xpMultiplier = builder.comment("Gain base xp * multiplier. Default 0 for now cause its not balanced").defineInRange("XP Multiplier", GeneralConfig.xpMultiplier, 0, Double.MAX_VALUE);
        this.skillXpMultiplier = builder.comment("Gain base skill xp * multiplier. Default 0 for now cause its not balanced").defineInRange("Skill XP Multiplier", GeneralConfig.skillXpMultiplier, 0, Double.MAX_VALUE);
        this.tamingMultiplier = builder.comment("Increase/Decrease global taming chance").defineInRange("Taming Chance Multiplier", GeneralConfig.tamingMultiplier, 0, Double.MAX_VALUE);
        builder.pop();

        builder.comment("Debug configs").push("Debug");
        this.debugMode = builder.comment("Enable debug mode for various things. Does nothing ATM").define("Debug Mode", GeneralConfig.debugAttack);
        this.debugAttack = builder.comment("Show the attack AoE of mobs").define("Debug Attack", GeneralConfig.debugAttack);
        builder.pop();

        builder.comment("Configs for player stats").push("Player Stats");
        this.maxLevel = builder.defineInRange("Max Level", GeneralConfig.maxLevel, 1, Integer.MAX_VALUE);
        this.startingHealth = builder.defineInRange("Starting HP", GeneralConfig.startingHealth, 0, Integer.MAX_VALUE);
        this.startingRP = builder.defineInRange("Starting RP", GeneralConfig.startingRP, 0, Integer.MAX_VALUE);
        this.startingMoney = builder.defineInRange("Starting Money", GeneralConfig.startingMoney, 0, Integer.MAX_VALUE);
        this.startingStr = builder.comment("Starting strength value. 1 strength = 1 attack damage").defineInRange("Starting Strength", GeneralConfig.startingStr, 0, Integer.MAX_VALUE);
        this.startingVit = builder.comment("Starting vitality value. 1 vitality = 0.5 defence & magic defence").defineInRange("Starting Vit", GeneralConfig.startingVit, 0, Integer.MAX_VALUE);
        this.startingIntel = builder.comment("Starting intelligence value. 1 intelligence = 1 magic damage").defineInRange("Starting Int", GeneralConfig.startingIntel, 0, Integer.MAX_VALUE);
        this.hpPerLevel = builder.comment("HP increase per level").defineInRange("HP Increase", GeneralConfig.hpPerLevel, 0, Double.MAX_VALUE);
        this.rpPerLevel = builder.comment("RP increase per level").defineInRange("RP Increase", GeneralConfig.rpPerLevel, 0, Double.MAX_VALUE);
        this.strPerLevel = builder.comment("Strenghth increase per level").defineInRange("Strength Increase", GeneralConfig.strPerLevel, 0, Double.MAX_VALUE);
        this.vitPerLevel = builder.comment("Vitality increase per level").defineInRange("Vit Increase", GeneralConfig.vitPerLevel, 0, Double.MAX_VALUE);
        this.intPerLevel = builder.comment("Intelligence increase per level").defineInRange("Int Increase", GeneralConfig.intPerLevel, 0, Double.MAX_VALUE);
        this.shortSwordUltimate = builder.comment("RP Cost of shortsword ultimate").defineInRange("Shortsword Ultimate", GeneralConfig.shortSwordUltimate, 0, Double.MAX_VALUE);
        this.longSwordUltimate = builder.comment("RP Cost of longsword ultimate").defineInRange("Longsword Ultimate", GeneralConfig.longSwordUltimate, 0, Double.MAX_VALUE);
        this.spearUltimate = builder.comment("RP Cost of spear ultimate").defineInRange("Spear Ultimate", GeneralConfig.spearUltimate, 0, Double.MAX_VALUE);
        this.hammerAxeUltimate = builder.comment("RP Cost of hammer and axe ultimate").defineInRange("Hammer & Axe Ultimatee", GeneralConfig.hammerAxeUltimate, 0, Double.MAX_VALUE);
        this.dualBladeUltimate = builder.comment("RP Cost of dual blades ultimate").defineInRange("Dualblade Ultimate", GeneralConfig.dualBladeUltimate, 0, Double.MAX_VALUE);
        this.gloveUltimate = builder.comment("RP Cost of fist weapon ultimate").defineInRange("Glove Ultimate", GeneralConfig.gloveUltimate, 0, Double.MAX_VALUE);
        builder.pop();

        builder.comment("Configs for weapon and tools").push("Weapon and Tools");
        this.platinumChargeTime = builder.comment("Platinum tier charge up time multiplier").defineInRange("Platinum Charge", GeneralConfig.platinumChargeTime, 0, Double.MAX_VALUE);
        builder.comment("X blocks the watering can can water").push("Watering Can Water");
        this.scrapWateringCanWater = builder.defineInRange("Scrap Watering Can Water", GeneralConfig.scrapWateringCanWater, 0, Integer.MAX_VALUE);
        this.ironWateringCanWater = builder.defineInRange("Iron Watering Can Water", GeneralConfig.ironWateringCanWater, 0, Integer.MAX_VALUE);
        this.silverWateringCanWater = builder.defineInRange("Silver Watering Can Water", GeneralConfig.silverWateringCanWater, 0, Integer.MAX_VALUE);
        this.goldWateringCanWater = builder.defineInRange("Gold Watering Can Water", GeneralConfig.goldWateringCanWater, 0, Integer.MAX_VALUE);
        this.platinumWateringCanWater = builder.defineInRange("Platinum Watering Can Water", GeneralConfig.platinumWateringCanWater, 0, Integer.MAX_VALUE);
        builder.pop();
        this.allowMoveOnAttack = builder.comment("Whether to allow moving during a normal melee attack. Synced to clients. Will look janky as the animation will not match player movements!").define("Allow Move On Attack", GeneralConfig.allowMoveOnAttack.get().booleanValue());
        builder.pop();
    }
}