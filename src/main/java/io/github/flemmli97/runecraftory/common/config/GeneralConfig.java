package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.config.values.SkillProperties;
import io.github.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;

import java.util.EnumMap;

public class GeneralConfig {

    public static boolean disableDefence;
    public static boolean gateSpawning;
    public static boolean disableVanillaSpawning;
    public static boolean randomDamage;
    public static boolean dropVanillaLoot = true;
    public static int recipeSystem = 0;

    public static boolean crops;
    public static boolean rp;

    public static int mineralRarity;

    public static boolean waila = true;
    public static boolean jei = true;
    public static boolean harvestCraft = true;
    public static boolean seasons = true;
    public static boolean dynamicTrees = true;

    public static int startingHealth;
    public static int startingRP;
    public static int startingMoney;
    public static int startingStr;
    public static int startingVit;
    public static int startingIntel;
    public static float hpPerLevel;
    public static int rpPerLevel;
    public static float strPerLevel;
    public static float vitPerLevel;
    public static float intPerLevel;
    public static EnumMap<EnumSkills, SkillProperties> skillProps = new EnumMap<>(EnumSkills.class);

    public static float scrapMultiplier;
    public static float ironMultiplier;
    public static float silverMultiplier;
    public static float goldMultiplier;
    public static float platinumMultiplier;
    public static float platinumChargeTime;
    public static int scrapWateringCanWater;
    public static int ironWateringCanWater;
    public static int silverWateringCanWater;
    public static int goldWateringCanWater;
    public static int platinumWateringCanWater;
    public static EnumMap<EnumWeaponType, WeaponTypeProperties> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public static float xpMultiplier;
    public static float skillXpMultiplier;
    public static float tamingMultiplier;
    public static float dropRateMultiplier;

    public static boolean debugMode;
    public static boolean debugAttack;

    public static void load(GeneralConfigSpec spec) {
        disableDefence = spec.disableDefence.get();
        gateSpawning = spec.gateSpawning.get();
        disableVanillaSpawning = spec.disableVanillaSpawning.get();
        randomDamage = spec.randomDamage.get();
        recipeSystem = spec.recipeSystem.get();

        startingHealth = spec.startingHealth.get();
        startingRP = spec.startingRP.get();
        startingMoney = spec.startingMoney.get();
        startingStr = spec.startingStr.get();
        startingVit = spec.startingVit.get();
        startingIntel = spec.startingIntel.get();
        hpPerLevel = spec.hpPerLevel.get().floatValue();
        rpPerLevel = spec.rpPerLevel.get();
        strPerLevel = spec.strPerLevel.get().floatValue();
        vitPerLevel = spec.vitPerLevel.get().floatValue();
        intPerLevel = spec.intPerLevel.get().floatValue();
        spec.skillProps.forEach((type, specs) -> skillProps.compute(type, (t, val) -> val == null ? new SkillProperties(specs) : val.read(specs)));

        scrapMultiplier = spec.scrapMultiplier.get().floatValue();
        ironMultiplier = spec.ironMultiplier.get().floatValue();
        silverMultiplier = spec.silverMultiplier.get().floatValue();
        goldMultiplier = spec.goldMultiplier.get().floatValue();
        platinumMultiplier = spec.platinumMultiplier.get().floatValue();
        platinumChargeTime = spec.platinumChargeTime.get().floatValue();
        scrapWateringCanWater = spec.scrapWateringCanWater.get();
        ironWateringCanWater = spec.ironWateringCanWater.get();
        silverWateringCanWater = spec.silverWateringCanWater.get();
        goldWateringCanWater = spec.goldWateringCanWater.get();
        platinumWateringCanWater = spec.platinumWateringCanWater.get();
        spec.weaponProps.forEach((type, specs) -> weaponProps.compute(type, (t, val) -> val == null ? new WeaponTypeProperties(specs) : val.read(specs)));

        xpMultiplier = spec.xpMultiplier.get().floatValue();
        skillXpMultiplier = spec.skillXpMultiplier.get().floatValue();
        tamingMultiplier = spec.tamingMultiplier.get().floatValue();
        dropRateMultiplier = spec.dropRateMultiplier.get().floatValue();

        debugMode = spec.debugMode.get();
        debugAttack = spec.debugAttack.get();
    }

    static {
        weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(3, 0, 25, 20));
        weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(2.5f, 7, 25, 11));
        weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(4.0f, 40, 25, 42));
        weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(5.0f, 3, 25, 15));
        weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(4.5f, 23, 25, 19));
        weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(2.5f, 5, 25, 8));
        weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(3.0f, 3, 25, 9));
        weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(3.5f, 0, 25, 20));

        skillProps.put(EnumSkills.SHORTSWORD, new SkillProperties(1.5f, 1, 0.5f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.LONGSWORD, new SkillProperties(1.5f, 1, 1.0f, 0.0f, 0.0f, 1));
        skillProps.put(EnumSkills.SPEAR, new SkillProperties(1, 1, 0.0f, 0.0f, 0, 1));
        skillProps.put(EnumSkills.HAMMERAXE, new SkillProperties(1.5f, 1, 1.0f, 0.0f, 0.0f, 1));
        skillProps.put(EnumSkills.DUAL, new SkillProperties(1.5f, 1, 1.0f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.FIST, new SkillProperties(1.0f, 2, 1.0f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.FIRE, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.WATER, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.EARTH, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.WIND, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.DARK, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.LIGHT, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.LOVE, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 1));
        skillProps.put(EnumSkills.FARMING, new SkillProperties(0.0f, 3, 0.5f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.LOGGING, new SkillProperties(0.0f, 4, 1.5f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.MINING, new SkillProperties(0.0f, 2, 1.5f, 0.5f, 0.0f, 1));
        skillProps.put(EnumSkills.FISHING, new SkillProperties(0.0f, 5, 0.0f, 0.5f, 1.0f, 2));
        skillProps.put(EnumSkills.COOKING, new SkillProperties(1.0f, 2, 0.0f, 0.5f, 1.0f, 1));
        skillProps.put(EnumSkills.FORGING, new SkillProperties(1.0f, 2, 1.0f, 0.5f, 1.0f, 5));
        skillProps.put(EnumSkills.CHEMISTRY, new SkillProperties(1.0f, 2, 0.0f, 0.5f, 1.5f, 5));
        skillProps.put(EnumSkills.CRAFTING, new SkillProperties(1.0f, 2, 0.5f, 0.5f, 1.0f, 5));
        skillProps.put(EnumSkills.SLEEPING, new SkillProperties(7.0f, 5, 0.0f, 2.5f, 0.0f, 15));
        skillProps.put(EnumSkills.EATING, new SkillProperties(2.0f, 3, 0.0f, 0.5f, 0.0f, 3));
        skillProps.put(EnumSkills.DEFENCE, new SkillProperties(5.0f, 3, 0.0f, 1.0f, 0.0f, 1));
        skillProps.put(EnumSkills.RESPOISON, new SkillProperties(10.0f, 2, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.RESSEAL, new SkillProperties(5.0f, 6, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.RESPARA, new SkillProperties(3.0f, 3, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.RESSLEEP, new SkillProperties(5.0f, 2, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.RESFAT, new SkillProperties(10.0f, 6, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.RESCOLD, new SkillProperties(12.0f, 3, 0.0f, 0.5f, 0.5f, 1));
        skillProps.put(EnumSkills.BATH, new SkillProperties(3.0f, 4, 0.0f, 1.0f, 0.0f, 20));
        skillProps.put(EnumSkills.TAMING, new SkillProperties(3.0f, 4, 1.0f, 1.0f, 1.0f, 3));
        skillProps.put(EnumSkills.LEADER, new SkillProperties(7.0f, 5, 1.0f, 1.0f, 1.0f, 1));
    }
}
