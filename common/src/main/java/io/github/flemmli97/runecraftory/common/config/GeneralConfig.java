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
    public static boolean useRP;
    public static float deathHPPercent;
    public static float deathRPPercent;
    public static boolean disableHunger;
    public static boolean modifyWeather;
    public static boolean modifyBed;
    public static boolean disableDatapack;

    public static boolean waila = true;
    public static boolean jei = true;
    public static boolean harvestCraft = true;
    public static boolean seasons = true;
    public static boolean dynamicTrees = true;

    public static int maxLevel;
    public static int maxSkillLevel;
    public static int startingHealth;
    public static int startingRP;
    public static int startingMoney;
    public static int startingStr;
    public static int startingVit;
    public static int startingIntel;
    public static float hpPerLevel;
    public static float rpPerLevel;
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

    public static boolean debugAttack;

    static {
        weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(3.0f, 0, 25, 20));
        weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(2.5f, 7, 25, 12));
        weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(4.0f, 40, 25, 30));
        weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(5.0f, 3, 25, 15));
        weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(4.0f, 23, 25, 20));
        weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(2.5f, 10, 25, 8));
        weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(2.5f, 3, 25, 10));
        weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(3.0f, 0, 25, 20));

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
        skillProps.put(EnumSkills.DEFENCE, new SkillProperties(4.0f, 3, 0.0f, 1.0f, 0.0f, 1));
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
