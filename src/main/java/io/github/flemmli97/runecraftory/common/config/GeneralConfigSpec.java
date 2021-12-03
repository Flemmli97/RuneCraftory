package io.github.flemmli97.runecraftory.common.config;


import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.config.values.SkillPropertySpecs;
import io.github.flemmli97.runecraftory.common.config.values.WeaponTypePropertySpecs;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.EnumMap;

public class GeneralConfigSpec {

    public static final Pair<GeneralConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(GeneralConfigSpec::new);

    public final ForgeConfigSpec.BooleanValue disableDefence;
    public final ForgeConfigSpec.BooleanValue gateSpawning;
    public final ForgeConfigSpec.BooleanValue disableVanillaSpawning;
    public final ForgeConfigSpec.BooleanValue randomDamage;
    public final ForgeConfigSpec.ConfigValue<Integer> recipeSystem;

    /*
    public final boolean crops;
    public final boolean rp;

    public final int mineralRarity;*/

    public final boolean waila = true;
    public final boolean jei = true;
    public final boolean harvestCraft = true;
    public final boolean seasons = true;
    public final boolean dynamicTrees = true;

    public final ForgeConfigSpec.ConfigValue<Integer> startingHealth;
    public final ForgeConfigSpec.ConfigValue<Integer> startingRP;
    public final ForgeConfigSpec.ConfigValue<Integer> startingMoney;
    public final ForgeConfigSpec.ConfigValue<Integer> startingStr;
    public final ForgeConfigSpec.ConfigValue<Integer> startingVit;
    public final ForgeConfigSpec.ConfigValue<Integer> startingIntel;
    public final ForgeConfigSpec.ConfigValue<Double> hpPerLevel;
    public final ForgeConfigSpec.ConfigValue<Integer> rpPerLevel;
    public final ForgeConfigSpec.ConfigValue<Double> strPerLevel;
    public final ForgeConfigSpec.ConfigValue<Double> vitPerLevel;
    public final ForgeConfigSpec.ConfigValue<Double> intPerLevel;
    public final EnumMap<EnumSkills, SkillPropertySpecs> skillProps = new EnumMap<>(EnumSkills.class);

    public final ForgeConfigSpec.ConfigValue<Double> scrapMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> ironMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> silverMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> goldMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> platinumMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> platinumChargeTime;
    public final ForgeConfigSpec.ConfigValue<Integer> scrapWateringCanWater;
    public final ForgeConfigSpec.ConfigValue<Integer> ironWateringCanWater;
    public final ForgeConfigSpec.ConfigValue<Integer> silverWateringCanWater;
    public final ForgeConfigSpec.ConfigValue<Integer> goldWateringCanWater;
    public final ForgeConfigSpec.ConfigValue<Integer> platinumWateringCanWater;
    public final EnumMap<EnumWeaponType, WeaponTypePropertySpecs> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public final ForgeConfigSpec.ConfigValue<Double> xpMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> skillXpMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> tamingMultiplier;
    public final ForgeConfigSpec.ConfigValue<Double> dropRateMultiplier;

    public final ForgeConfigSpec.BooleanValue debugMode;
    public final ForgeConfigSpec.BooleanValue debugAttack;

    private GeneralConfigSpec(ForgeConfigSpec.Builder builder) {
        builder.push("Modules");
        this.disableDefence = builder.comment("If disabled attacks will ignore defence of mobs. Should be near vanilla then except effects like poison attacks etc.").define("Ignore Defence", false);
        this.gateSpawning = builder.comment("Should gates spawn? If disabled will also disable all mobs from this mod to spawn. Needs server restart").define("Gate Spawning", true);
        this.disableVanillaSpawning = builder.comment("If enabled mobs can only spawn through gates.").define("Disable vanilla spawn", false);
        this.randomDamage = builder.comment("If enabled damage gets a +-10% randomness.").define("Random Damage", true);
        this.recipeSystem = builder.comment("The recipe system to use.", "0 = Only unlocked recipes are craftable and crafting skill influence the rp cost", "1 = Unlocked recipes are craftable and rp cost are the base cost.",
                "2 = All recipes are craftable without unlocking but crafting skill influence the cost.", "3 = All craftable and only base cost taken into consideration.").define("Recipe System", 0);
        builder.pop();

        builder.push("Multipliers");
        this.xpMultiplier = builder.comment("Gain base xp * multiplier. Default 0 for now cause its not balanced").define("XP Multiplier", 0.0);
        this.skillXpMultiplier = builder.comment("Gain base skill xp * multiplier. Default 0 for now cause its not balanced").define("Skill XP Multiplier", 0.0);
        this.tamingMultiplier = builder.comment("Increase/Decrease global taming chance").define("Taming Chance Multiplier", 1.0);
        this.dropRateMultiplier = builder.comment("Increase/Decrease global drop chance").define("Drop Chance Multiplier", 1.0);
        builder.pop();

        builder.comment("Debug configs").push("Debug");
        this.debugMode = builder.comment("Enable debug mode for various things").define("Debug Mode", false);
        this.debugAttack = builder.comment("Show the attack AoE of mobs").define("Debug Attack", false);
        builder.pop();

        builder.comment("Configs for player stats").push("Player Stats");
        this.startingHealth = builder.define("Starting HP", 25);
        this.startingRP = builder.define("Starting RP", 50);
        this.startingMoney = builder.define("Starting Money", 100);
        this.startingStr = builder.comment("Starting strength value. 1 strength = 1 attack damage").define("Starting Strength", 0);
        this.startingVit = builder.comment("Starting vitality value. 1 vitality = 0.5 defence & magic defence").define("Starting Vit", 0);
        this.startingIntel = builder.comment("Starting intelligence value. 1 intelligence = 1 magic damage").define("Starting Int", 1);
        this.hpPerLevel = builder.comment("HP increase per level").define("HP Increase", 4.0);
        this.rpPerLevel = builder.comment("RP increase per level").define("RP Increase", 5);
        this.strPerLevel = builder.comment("Strenghth increase per level").define("Strength Increase", 0.8);
        this.vitPerLevel = builder.comment("Vitality increase per level").define("Vit Increase", 1.0);
        this.intPerLevel = builder.comment("Intelligence increase per level").define("Int Increase", 0.8);
        GeneralConfig.skillProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.skillProps.put(type, new SkillPropertySpecs(builder, prop));
            builder.pop();
        });
        builder.pop();

        builder.comment("Configs for weapon and tools").push("Weapon and Tools");
        this.scrapMultiplier = builder.comment("Scrap tier xp multiplier").define("Scrap Multiplier", 0.5);
        this.ironMultiplier = builder.comment("Iron tier xp multiplier").define("Iron Multiplier", 1.0);
        this.silverMultiplier = builder.comment("Silver tier xp multiplier").define("Silver Multiplier", 1.5);
        this.goldMultiplier = builder.comment("Gold tier xp multiplier").define("Gold Multiplier", 2.5);
        this.platinumMultiplier = builder.comment("Platinum tier xp multiplier").define("Platinum Multiplier", 3.5);
        this.platinumChargeTime = builder.comment("Platinum tier charge up time multiplier").define("Platinum Charge", 0.5);
        builder.comment("X blocks the watering can can water").push("Watering Can Water");
        this.scrapWateringCanWater = builder.define("Scrap Watering Can Water", 25);
        this.ironWateringCanWater = builder.define("Iron Watering Can Water", 35);
        this.silverWateringCanWater = builder.define("Silver Watering Can Water", 50);
        this.goldWateringCanWater = builder.define("Gold Watering Can Water", 70);
        this.platinumWateringCanWater = builder.define("Platinum Watering Can Water", 150);
        builder.pop();
        GeneralConfig.weaponProps.forEach((type, prop) -> {
            builder.push(type.toString());
            this.weaponProps.put(type, new WeaponTypePropertySpecs(builder, prop));
            builder.pop();
        });
        builder.pop();
    }
}