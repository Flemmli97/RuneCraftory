package io.github.flemmli97.runecraftory.fabric.config;


import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.fabric.config.values.SkillPropertySpecs;
import io.github.flemmli97.runecraftory.fabric.config.values.WeaponTypePropertySpecs;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;

public class GeneralConfigSpec {

    public static final Pair<JsonConfig<CommentedJsonConfig>, GeneralConfigSpec> spec = CommentedJsonConfig.Builder
            .create(FabricLoader.getInstance().getConfigDir().resolve(RuneCraftory.MODID).resolve("general.json"), 1, GeneralConfigSpec::new);

    public final CommentedJsonConfig.CommentedVal<Boolean> disableDefence;
    public final CommentedJsonConfig.CommentedVal<Boolean> vanillaIgnoreDefence;
    public final CommentedJsonConfig.CommentedVal<Boolean> gateSpawning;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableVanillaSpawning;
    public final CommentedJsonConfig.CommentedVal<Boolean> randomDamage;
    public final CommentedJsonConfig.CommentedVal<GeneralConfig.RecipeSystem> recipeSystem;
    public final CommentedJsonConfig.CommentedVal<Boolean> useRP;
    public final CommentedJsonConfig.DoubleVal deathHPPercent;
    public final CommentedJsonConfig.DoubleVal deathRPPercent;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableHunger;
    public final CommentedJsonConfig.CommentedVal<Boolean> modifyWeather;
    public final CommentedJsonConfig.CommentedVal<Boolean> modifyBed;
    public final CommentedJsonConfig.CommentedVal<Boolean> healOnWakeUp;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableFoodSystem;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableItemStatSystem;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableCropSystem;
    public final CommentedJsonConfig.CommentedVal<Boolean> seasonedSnow;
    public final CommentedJsonConfig.IntVal maxPartySize;

    public final CommentedJsonConfig.DoubleVal witherChance;
    public final CommentedJsonConfig.DoubleVal runeyChance;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableFarmlandRandomtick;
    public final CommentedJsonConfig.CommentedVal<Boolean> disableFarmlandTrample;
    public final CommentedJsonConfig.CommentedVal<Boolean> tickUnloadedFarmland;
    public final CommentedJsonConfig.CommentedVal<Boolean> unloadedFarmlandCheckWater;

    public final boolean waila = true;
    public final boolean jei = true;
    public final boolean harvestCraft = true;
    public final boolean seasons = true;
    public final boolean dynamicTrees = true;

    public final CommentedJsonConfig.IntVal maxLevel;
    public final CommentedJsonConfig.IntVal startingHealth;
    public final CommentedJsonConfig.IntVal startingRP;
    public final CommentedJsonConfig.IntVal startingMoney;
    public final CommentedJsonConfig.IntVal startingStr;
    public final CommentedJsonConfig.IntVal startingVit;
    public final CommentedJsonConfig.IntVal startingIntel;
    public final CommentedJsonConfig.DoubleVal hpPerLevel;
    public final CommentedJsonConfig.DoubleVal rpPerLevel;
    public final CommentedJsonConfig.DoubleVal strPerLevel;
    public final CommentedJsonConfig.DoubleVal vitPerLevel;
    public final CommentedJsonConfig.DoubleVal intPerLevel;
    public final EnumMap<EnumSkills, SkillPropertySpecs> skillProps = new EnumMap<>(EnumSkills.class);

    public final CommentedJsonConfig.DoubleVal platinumChargeTime;
    public final CommentedJsonConfig.IntVal scrapWateringCanWater;
    public final CommentedJsonConfig.IntVal ironWateringCanWater;
    public final CommentedJsonConfig.IntVal silverWateringCanWater;
    public final CommentedJsonConfig.IntVal goldWateringCanWater;
    public final CommentedJsonConfig.IntVal platinumWateringCanWater;
    public final EnumMap<EnumWeaponType, WeaponTypePropertySpecs> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public final CommentedJsonConfig.DoubleVal xpMultiplier;
    public final CommentedJsonConfig.DoubleVal skillXpMultiplier;
    public final CommentedJsonConfig.DoubleVal tamingMultiplier;

    public final CommentedJsonConfig.CommentedVal<Boolean> debugMode;
    public final CommentedJsonConfig.CommentedVal<Boolean> debugAttack;

    private GeneralConfigSpec(CommentedJsonConfig.Builder builder) {
        builder.comment("The current default values reflect a near vanilla experience since quite a few things are not done yet to my satisfaction.",
                "You can freely change the things here and they will work but you might also consider tweaking things for balancing").push("Modules");
        this.disableDefence = builder.comment("If disabled attacks will ignore defence of mobs. Should be near vanilla then except effects like poison attacks etc.").define("Ignore Defence", GeneralConfig.disableDefence);
        this.vanillaIgnoreDefence = builder.comment("If damage sources not from this mod should simply ignore defence.",
                "Since vanilla dmg doesnt scale much after gaining some defence you are pretty much immune to it.",
                "Turn it off if you have some mods that do damage scaling. Or want to just feel op.").define("Vanilla Ignore Defence", GeneralConfig.vanillaIgnoreDefence);
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
                .define("Recipe System", GeneralConfig.recipeSystem);
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
        this.maxLevel = builder.defineInRange("Max Level", GeneralConfig.maxLevel, 1, 1000);
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
        GeneralConfig.skillProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.skillProps.put(type, new SkillPropertySpecs(builder, prop));
            builder.pop();
        });
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
        GeneralConfig.weaponProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.weaponProps.put(type, new WeaponTypePropertySpecs(builder, prop));
            builder.pop();
        });
        builder.pop();
        builder.registerReloadHandler(() -> ConfigHolder.loadGeneral(this));
    }
}