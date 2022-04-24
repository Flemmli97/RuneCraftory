package io.github.flemmli97.runecraftory.forge.config;


import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.forge.config.values.SkillPropertySpecs;
import io.github.flemmli97.runecraftory.forge.config.values.WeaponTypePropertySpecs;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;

public class GeneralConfigSpec {

    public static final Pair<GeneralConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(GeneralConfigSpec::new);

    public final ForgeConfigSpec.BooleanValue disableDefence;
    public final ForgeConfigSpec.BooleanValue gateSpawning;
    public final ForgeConfigSpec.BooleanValue disableVanillaSpawning;
    public final ForgeConfigSpec.BooleanValue randomDamage;
    public final ForgeConfigSpec.IntValue recipeSystem;
    public final ForgeConfigSpec.BooleanValue useRP;
    public final ForgeConfigSpec.DoubleValue deathHPPercent;
    public final ForgeConfigSpec.DoubleValue deathRPPercent;
    public final ForgeConfigSpec.BooleanValue disableHunger;
    public final ForgeConfigSpec.BooleanValue modifyWeather;
    public final ForgeConfigSpec.BooleanValue modifyBed;
    public final ForgeConfigSpec.BooleanValue disableFoodSystem;
    public final ForgeConfigSpec.BooleanValue disableItemStatSystem;
    public final ForgeConfigSpec.BooleanValue disableCropSystem;

    public final boolean waila = true;
    public final boolean jei = true;
    public final boolean harvestCraft = true;
    public final boolean seasons = true;
    public final boolean dynamicTrees = true;

    public final ForgeConfigSpec.IntValue maxLevel;
    public final ForgeConfigSpec.IntValue maxSkillLevel;
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
    public final EnumMap<EnumSkills, SkillPropertySpecs> skillProps = new EnumMap<>(EnumSkills.class);

    public final ForgeConfigSpec.DoubleValue scrapMultiplier;
    public final ForgeConfigSpec.DoubleValue ironMultiplier;
    public final ForgeConfigSpec.DoubleValue silverMultiplier;
    public final ForgeConfigSpec.DoubleValue goldMultiplier;
    public final ForgeConfigSpec.DoubleValue platinumMultiplier;
    public final ForgeConfigSpec.DoubleValue platinumChargeTime;
    public final ForgeConfigSpec.IntValue scrapWateringCanWater;
    public final ForgeConfigSpec.IntValue ironWateringCanWater;
    public final ForgeConfigSpec.IntValue silverWateringCanWater;
    public final ForgeConfigSpec.IntValue goldWateringCanWater;
    public final ForgeConfigSpec.IntValue platinumWateringCanWater;
    public final EnumMap<EnumWeaponType, WeaponTypePropertySpecs> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public final ForgeConfigSpec.DoubleValue xpMultiplier;
    public final ForgeConfigSpec.DoubleValue skillXpMultiplier;
    public final ForgeConfigSpec.DoubleValue tamingMultiplier;
    public final ForgeConfigSpec.DoubleValue dropRateMultiplier;

    public final ForgeConfigSpec.BooleanValue debugMode;
    public final ForgeConfigSpec.BooleanValue debugAttack;

    private GeneralConfigSpec(ForgeConfigSpec.Builder builder) {
        builder.comment("The current default values reflect a near vanilla experience since quite a few things are not done yet to my satisfaction.",
                "You can freely change the things here and they will work but you might also consider tweaking things for balancing").push("Modules");
        this.disableDefence = builder.comment("If disabled attacks will ignore defence of mobs. Should be near vanilla then except effects like poison attacks etc.").define("Ignore Defence", false);
        this.gateSpawning = builder.comment("Should gates spawn? If disabled will also disable all mobs from this mod to spawn. Needs server restart").define("Gate Spawning", true);
        this.disableVanillaSpawning = builder.comment("If enabled mobs can only spawn through gates.").define("Disable vanilla spawn", false);
        this.randomDamage = builder.comment("If enabled damage gets a +-10% randomness.").define("Random Damage", true);
        this.recipeSystem = builder.comment("The recipe system to use.", "0 = Only unlocked recipes are craftable and crafting skill influence the rp cost", "1 = Unlocked recipes are craftable and rp cost are the base cost.",
                "2 = All recipes are craftable without unlocking but crafting skill influence the cost.", "3 = All craftable and only base cost taken into consideration.").defineInRange("Recipe System", 0, 0, 3);
        this.useRP = builder.comment("If actions consume rune points").define("Use RunePoints", false);
        this.deathHPPercent = builder.comment("Percent of HP that will be regenerated on death").defineInRange("Death HP Percent", 1d, 0, 1);
        this.deathRPPercent = builder.comment("Percent of RP that will be regenerated on death").defineInRange("Death RP Percent", 1d, 0, 1);
        this.disableHunger = builder.comment("Disable mc hunger system. Makes it so you can always eat food").define("Disable Hunger", false);
        this.modifyWeather = builder.comment("If true weather will change only at specific time.", "Morning, mid day, evening and mid night").define("Modify Weather", false);
        this.modifyBed = builder.comment("If true players can sleep anytime and upon waking up will restore all health and rp").define("Modify Bed", false);
        this.disableFoodSystem = builder.comment("If true food will not provide benefits such as hp restoration etc defined per datapack. ", "Note: Crops from this mod will not function anymore").define("Disable Food System", true);
        this.disableItemStatSystem = builder.comment("If true item get no stats assigned as defined per datapack. ", "Note: Weapons will then do no damage").define("Disable Item Stat System", true);
        this.disableCropSystem = builder.comment("If true crop data will be disabled.", "Note: Crops from this mod will not function anymore").define("Disable Crop System", true);
        builder.pop();

        builder.push("Multipliers");
        this.xpMultiplier = builder.comment("Gain base xp * multiplier. Default 0 for now cause its not balanced").defineInRange("XP Multiplier", 1.0, 0, Double.MAX_VALUE);
        this.skillXpMultiplier = builder.comment("Gain base skill xp * multiplier. Default 0 for now cause its not balanced").defineInRange("Skill XP Multiplier", 1.0, 0, Double.MAX_VALUE);
        this.tamingMultiplier = builder.comment("Increase/Decrease global taming chance").defineInRange("Taming Chance Multiplier", 1.0, 0, Double.MAX_VALUE);
        this.dropRateMultiplier = builder.comment("Increase/Decrease global drop chance").defineInRange("Drop Chance Multiplier", 1.0, 0, Double.MAX_VALUE);
        builder.pop();

        builder.comment("Debug configs").push("Debug");
        this.debugMode = builder.comment("Enable debug mode for various things").define("Debug Mode", false);
        this.debugAttack = builder.comment("Show the attack AoE of mobs").define("Debug Attack", false);
        builder.pop();

        builder.comment("Configs for player stats").push("Player Stats");
        this.maxLevel = builder.defineInRange("Max Level", 100, 1, Integer.MAX_VALUE);
        this.maxSkillLevel = builder.defineInRange("Max Skill Level", 100, 1, Integer.MAX_VALUE);
        this.startingHealth = builder.defineInRange("Starting HP", 25, 0, Integer.MAX_VALUE);
        this.startingRP = builder.defineInRange("Starting RP", 50, 0, Integer.MAX_VALUE);
        this.startingMoney = builder.defineInRange("Starting Money", 100, 0, Integer.MAX_VALUE);
        this.startingStr = builder.comment("Starting strength value. 1 strength = 1 attack damage").defineInRange("Starting Strength", 0, 0, Integer.MAX_VALUE);
        this.startingVit = builder.comment("Starting vitality value. 1 vitality = 0.5 defence & magic defence").defineInRange("Starting Vit", 0, 0, Integer.MAX_VALUE);
        this.startingIntel = builder.comment("Starting intelligence value. 1 intelligence = 1 magic damage").defineInRange("Starting Int", 1, 0, Integer.MAX_VALUE);
        this.hpPerLevel = builder.comment("HP increase per level").defineInRange("HP Increase", 4, 0, Double.MAX_VALUE);
        this.rpPerLevel = builder.comment("RP increase per level").defineInRange("RP Increase", 5, 0, Double.MAX_VALUE);
        this.strPerLevel = builder.comment("Strenghth increase per level").defineInRange("Strength Increase", 0.8, 0, Double.MAX_VALUE);
        this.vitPerLevel = builder.comment("Vitality increase per level").defineInRange("Vit Increase", 1, 0, Double.MAX_VALUE);
        this.intPerLevel = builder.comment("Intelligence increase per level").defineInRange("Int Increase", 0.8, 0, Double.MAX_VALUE);
        GeneralConfig.skillProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.skillProps.put(type, new SkillPropertySpecs(builder, prop));
            builder.pop();
        });
        builder.pop();

        builder.comment("Configs for weapon and tools").push("Weapon and Tools");
        this.scrapMultiplier = builder.comment("Scrap tier xp multiplier").defineInRange("Scrap Multiplier", 0.5, 0, Double.MAX_VALUE);
        this.ironMultiplier = builder.comment("Iron tier xp multiplier").defineInRange("Iron Multiplier", 1.0, 0, Double.MAX_VALUE);
        this.silverMultiplier = builder.comment("Silver tier xp multiplier").defineInRange("Silver Multiplier", 1.5, 0, Double.MAX_VALUE);
        this.goldMultiplier = builder.comment("Gold tier xp multiplier").defineInRange("Gold Multiplier", 2.5, 0, Double.MAX_VALUE);
        this.platinumMultiplier = builder.comment("Platinum tier xp multiplier").defineInRange("Platinum Multiplier", 3.5, 0, Double.MAX_VALUE);
        this.platinumChargeTime = builder.comment("Platinum tier charge up time multiplier").defineInRange("Platinum Charge", 0.5, 0, Double.MAX_VALUE);
        builder.comment("X blocks the watering can can water").push("Watering Can Water");
        this.scrapWateringCanWater = builder.defineInRange("Scrap Watering Can Water", 25, 0, Integer.MAX_VALUE);
        this.ironWateringCanWater = builder.defineInRange("Iron Watering Can Water", 35, 0, Integer.MAX_VALUE);
        this.silverWateringCanWater = builder.defineInRange("Silver Watering Can Water", 50, 0, Integer.MAX_VALUE);
        this.goldWateringCanWater = builder.defineInRange("Gold Watering Can Water", 70, 0, Integer.MAX_VALUE);
        this.platinumWateringCanWater = builder.defineInRange("Platinum Watering Can Water", 150, 0, Integer.MAX_VALUE);
        builder.pop();
        GeneralConfig.weaponProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.weaponProps.put(type, new WeaponTypePropertySpecs(builder, prop));
            builder.pop();
        });
        builder.pop();
    }
}