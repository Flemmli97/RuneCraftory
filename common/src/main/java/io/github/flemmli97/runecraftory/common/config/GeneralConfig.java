package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.common.config.values.SkillProperties;
import io.github.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;

import java.util.EnumMap;

public class GeneralConfig {

    public static boolean disableDefence = false;
    public static boolean vanillaIgnoreDefence = true;
    public static boolean gateSpawning = true;
    public static boolean disableVanillaSpawning = false;
    public static boolean randomDamage = true;
    public static boolean dropVanillaLoot = true;
    public static int recipeSystem = 3;
    public static boolean useRP = true;
    public static float deathHPPercent = 1;
    public static float deathRPPercent = 1;
    public static boolean disableHunger = false;
    public static boolean modifyWeather = true;
    public static boolean modifyBed = false;
    public static boolean healOnWakeUp = true;
    public static boolean disableFoodSystem = false;
    public static boolean disableItemStatSystem = false;
    public static boolean disableCropSystem = false;
    public static float witherChance = 0.5f;
    public static boolean seasonedSnow = true;

    public static boolean waila = true;
    public static boolean jei = true;
    public static boolean harvestCraft = true;
    public static boolean seasons = true;
    public static boolean dynamicTrees = true;

    public static int maxLevel = 250;
    public static int maxSkillLevel = 100;
    public static int startingHealth = 20;
    public static int startingRP = 100;
    public static int startingMoney = 100;
    public static int startingStr = 0;
    public static int startingVit = 0;
    public static int startingIntel = 1;
    public static float hpPerLevel = 7;
    public static float rpPerLevel = 3;
    public static float strPerLevel = 0.8f;
    public static float vitPerLevel = 1;
    public static float intPerLevel = 0.6f;
    public static EnumMap<EnumSkills, SkillProperties> skillProps = new EnumMap<>(EnumSkills.class);

    public static float platinumChargeTime = 0.5f;
    public static int scrapWateringCanWater = 25;
    public static int ironWateringCanWater = 35;
    public static int silverWateringCanWater = 100;
    public static int goldWateringCanWater = 150;
    public static int platinumWateringCanWater = 250;
    public static EnumMap<EnumWeaponType, WeaponTypeProperties> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public static float xpMultiplier = 1;
    public static float skillXpMultiplier = 1;
    public static float tamingMultiplier = 1;

    public static boolean debugAttack = false;

    static {
        weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(3.0f, 0, 20, 20));
        weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(2.5f, 7, 20, 11));
        weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(4.0f, 40, 20, 30));
        weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(5.0f, 3, 20, 15));
        weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(4.0f, 23, 20, 20));
        weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(2.5f, 10, 20, 7));
        weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(2.5f, 3, 20, 9));
        weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(3.0f, 0, 20, 20));

        skillProps.put(EnumSkills.SHORTSWORD, new SkillProperties(0, 0.5f, 0.25f, 0, 0, 2));
        skillProps.put(EnumSkills.LONGSWORD, new SkillProperties(0, 0.5f, 0.25f, 0, 0, 3));
        skillProps.put(EnumSkills.SPEAR, new SkillProperties(0, 0.5f, 0.25f, 0, 0, 2));
        skillProps.put(EnumSkills.HAMMERAXE, new SkillProperties(0, 0.5f, 0.6f, 0, 0, 3));
        skillProps.put(EnumSkills.DUAL, new SkillProperties(0, 0.5f, 0.25f, 0, 0, 2));
        skillProps.put(EnumSkills.FIST, new SkillProperties(0, 0.5f, 0.25f, 0, 0, 2));
        skillProps.put(EnumSkills.FIRE, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 2));
        skillProps.put(EnumSkills.WATER, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 6));
        skillProps.put(EnumSkills.EARTH, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 6));
        skillProps.put(EnumSkills.WIND, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 6));
        skillProps.put(EnumSkills.DARK, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 6));
        skillProps.put(EnumSkills.LIGHT, new SkillProperties(0, 0.5f, 0, 0, 0.25f, 6));
        skillProps.put(EnumSkills.LOVE, new SkillProperties(0.1f, 0.5f, 0, 0, 0.25f, 10));
        skillProps.put(EnumSkills.FARMING, new SkillProperties(1, 1, 0, 0.5f, 0, 1));
        skillProps.put(EnumSkills.LOGGING, new SkillProperties(1, 0.7f, 0, 0.3f, 0, 2));
        skillProps.put(EnumSkills.MINING, new SkillProperties(1, 0.7f, 0.1f, 0.3f, 0, 1));
        skillProps.put(EnumSkills.FISHING, new SkillProperties(1, 0.5f, 0, 0, 0.5f, 25));
        skillProps.put(EnumSkills.COOKING, new SkillProperties(0, 0.25f, 0, 0.1f, 0, 10));
        skillProps.put(EnumSkills.FORGING, new SkillProperties(0, 0.25f, 0, 0.1f, 0, 20));
        skillProps.put(EnumSkills.CHEMISTRY, new SkillProperties(0, 0.25f, 0, 0.1f, 0, 20));
        skillProps.put(EnumSkills.CRAFTING, new SkillProperties(0, 0.25f, 0, 0, 0.1f, 20));
        skillProps.put(EnumSkills.SEARCHING, new SkillProperties(0, 0.5f, 0, 0, 0.3f, 15));
        skillProps.put(EnumSkills.WALKING, new SkillProperties(0.3f, 0.125f, 0, 0.1f, 0, 1));
        skillProps.put(EnumSkills.SLEEPING, new SkillProperties(3, 2, 0.5f, 1, 0.5f, 75));
        skillProps.put(EnumSkills.EATING, new SkillProperties(2, 5, 0.5f, 0.5f, 0.2f, 50));
        skillProps.put(EnumSkills.DEFENCE, new SkillProperties(2, 0.1f, 0, 1.5f, 0, 5));
        skillProps.put(EnumSkills.RESPOISON, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.RESSEAL, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.RESPARA, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.RESSLEEP, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.RESFAT, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.RESCOLD, new SkillProperties(0.1f, 0, 0, 0.2f, 0.05f, 7));
        skillProps.put(EnumSkills.BATH, new SkillProperties(2, 1, 0, 0.1f, 0, 5));
        skillProps.put(EnumSkills.TAMING, new SkillProperties(0, 0.5f, 0, 0.25f, 0.5f, 1));
        skillProps.put(EnumSkills.LEADER, new SkillProperties(1, 0, 0.25f, 0, 0.1f, 1));
    }
}
