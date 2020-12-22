package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.config.values.SkillProperties;
import com.flemmli97.runecraftory.common.config.values.WeaponTypeProperties;

import java.util.EnumMap;

public class GeneralConfig {

    public static boolean combatModule;
    public static boolean gateSpawning;
    public static boolean disableVanillaSpawning;

    public static boolean crops;
    public static boolean rp;

    public static int mineralRarity;

    public static boolean waila = true;
    public static boolean jei = true;
    public static boolean harvestCraft=true;
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
    public static EnumMap<EnumWeaponType, WeaponTypeProperties> weaponProps = new EnumMap<>(EnumWeaponType.class);

    public static float xpMultiplier;
    public static float skillXpMultiplier;
    public static float tamingMultiplier;
    public static float dropRateMultiplier;

    public static boolean debugMode;
    public static boolean debugAttack;

    public static void load() {
        combatModule = GeneralConfigSpec.generalConf.combatModule.get();
        gateSpawning = GeneralConfigSpec.generalConf.gateSpawning.get();
        disableVanillaSpawning = GeneralConfigSpec.generalConf.disableVanillaSpawning.get();

        startingHealth = GeneralConfigSpec.generalConf.startingHealth.get();
        startingRP = GeneralConfigSpec.generalConf.startingRP.get();
        startingMoney = GeneralConfigSpec.generalConf.startingMoney.get();
        startingStr = GeneralConfigSpec.generalConf.startingStr.get();
        startingVit = GeneralConfigSpec.generalConf.startingVit.get();
        startingIntel = GeneralConfigSpec.generalConf.startingIntel.get();
        hpPerLevel = GeneralConfigSpec.generalConf.hpPerLevel.get();
        rpPerLevel = GeneralConfigSpec.generalConf.rpPerLevel.get();
        strPerLevel = GeneralConfigSpec.generalConf.strPerLevel.get();
        vitPerLevel = GeneralConfigSpec.generalConf.vitPerLevel.get();
        intPerLevel = GeneralConfigSpec.generalConf.intPerLevel.get();
        GeneralConfigSpec.generalConf.skillProps.forEach((type, specs)->skillProps.compute(type, (t, val)->val == null ? new SkillProperties(specs):val.read(specs)));

        scrapMultiplier = GeneralConfigSpec.generalConf.scrapMultiplier.get();
        ironMultiplier = GeneralConfigSpec.generalConf.ironMultiplier.get();
        silverMultiplier = GeneralConfigSpec.generalConf.silverMultiplier.get();
        goldMultiplier = GeneralConfigSpec.generalConf.goldMultiplier.get();
        platinumMultiplier = GeneralConfigSpec.generalConf.platinumMultiplier.get();
        platinumChargeTime = GeneralConfigSpec.generalConf.platinumChargeTime.get();
        GeneralConfigSpec.generalConf.weaponProps.forEach((type, specs)->weaponProps.compute(type, (t,val)->val == null ? new WeaponTypeProperties(specs):val.read(specs)));

        xpMultiplier = GeneralConfigSpec.generalConf.xpMultiplier.get();
        skillXpMultiplier = GeneralConfigSpec.generalConf.skillXpMultiplier.get();
        tamingMultiplier = GeneralConfigSpec.generalConf.tamingMultiplier.get();
        dropRateMultiplier = GeneralConfigSpec.generalConf.dropRateMultiplier.get();

        debugMode = GeneralConfigSpec.generalConf.debugMode.get();
        debugAttack = GeneralConfigSpec.generalConf.debugAttack.get();
    }

    static {
        weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(3, 0, 25, 20));
        weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(2.5f, 6, 25, 10));
        weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(4.0f, 25, 25, 15));
        weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(5.0f, 1.5f, 25, 12));
        weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(4.5f, 40, 25, 25));
        weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(3.0f, 10, 25, 7));
        weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(3.0f, 3, 25, 7));
        weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(3.5f, 0, 25, 20));

        skillProps.put(EnumSkills.SHORTSWORD, new SkillProperties(1.5f, 1, 0.5f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.LONGSWORD, new SkillProperties(1.5f, 1, 1.0f, 0.0f, 0.0f, 0));
        skillProps.put(EnumSkills.SPEAR, new SkillProperties(1, 1, 0.0f, 0.0f, 0, 0));
        skillProps.put(EnumSkills.HAMMERAXE, new SkillProperties(1.5f, 1, 1.0f, 0.0f, 0.0f, 0));
        skillProps.put(EnumSkills.DUAL, new SkillProperties(1.5f, 1, 1.0f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.FIST, new SkillProperties(1.0f, 2, 1.0f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.FIRE, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.WATER, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.EARTH, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.WIND, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.DARK, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.LIGHT, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.LOVE, new SkillProperties(1.0f, 2, 0.0f, 0.0f, 1.0f, 0));
        skillProps.put(EnumSkills.FARMING, new SkillProperties(0.0f, 3, 0.5f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.LOGGING, new SkillProperties(0.0f, 4, 1.5f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.MINING, new SkillProperties(0.0f, 2, 1.5f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.FISHING, new SkillProperties(0.0f, 5, 0.0f, 0.5f, 1.0f, 0));
        skillProps.put(EnumSkills.COOKING, new SkillProperties(1.0f, 2, 0.0f, 0.5f, 1.0f, 0));
        skillProps.put(EnumSkills.FORGING, new SkillProperties(1.0f, 2, 1.0f, 0.5f, 1.0f, 0));
        skillProps.put(EnumSkills.CHEMISTRY, new SkillProperties(1.0f, 2, 0.0f, 0.5f, 1.5f, 0));
        skillProps.put(EnumSkills.CRAFTING, new SkillProperties(1.0f, 2, 0.5f, 0.5f, 1.0f, 0));
        skillProps.put(EnumSkills.SLEEPING, new SkillProperties(7.0f, 5, 0.0f, 2.5f, 0.0f, 0));
        skillProps.put(EnumSkills.EATING, new SkillProperties(2.0f, 3, 0.0f, 0.5f, 0.0f, 0));
        skillProps.put(EnumSkills.DEFENCE, new SkillProperties(5.0f, 3, 0.0f, 1.0f, 0.0f, 0));
        skillProps.put(EnumSkills.RESPOISON, new SkillProperties(10.0f, 2, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.RESSEAL, new SkillProperties(5.0f, 6, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.RESPARA, new SkillProperties(3.0f, 3, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.RESSLEEP, new SkillProperties(5.0f, 2, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.RESFAT, new SkillProperties(10.0f, 6, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.RESCOLD, new SkillProperties(12.0f, 3, 0.0f, 0.5f, 0.5f, 0));
        skillProps.put(EnumSkills.BATH, new SkillProperties(3.0f, 4, 0.0f, 1.0f, 0.0f, 0));
        skillProps.put(EnumSkills.TAMING, new SkillProperties(3.0f, 4, 1.0f, 1.0f, 1.0f, 0));
        skillProps.put(EnumSkills.LEADER, new SkillProperties(7.0f, 5, 1.0f, 1.0f, 1.0f, 0));
    }
}
