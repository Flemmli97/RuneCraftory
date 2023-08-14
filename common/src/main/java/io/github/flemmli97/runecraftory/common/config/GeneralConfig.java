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
    public static RecipeSystem recipeSystem = RecipeSystem.SKILL;
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
    public static boolean seasonedSnow = true;
    public static int maxPartySize = 3;

    public static float witherChance = 0.5f;
    public static float runeyChance = 0.05f;
    public static boolean disableFarmlandRandomtick = true;
    public static boolean disableFarmlandTrample = true;
    public static boolean tickUnloadedFarmland = true;
    public static boolean unloadedFarmlandCheckWater = true;

    public static boolean waila = true;
    public static boolean harvestCraft = true;
    public static boolean seasons = true;
    public static boolean dynamicTrees = true;

    public static int maxLevel = 999;
    public static int maxSkillLevel = 100;
    public static int startingHealth = 20;
    public static int startingRP = 100;
    public static int startingMoney = 100;
    public static int startingStr = 0;
    public static int startingVit = 0;
    public static int startingIntel = 3;
    public static float hpPerLevel = 5;
    public static float rpPerLevel = 2;
    public static float strPerLevel = 1;
    public static float vitPerLevel = 1.5f;
    public static float intPerLevel = 1;
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
        weaponProps.put(EnumWeaponType.FARM, new WeaponTypeProperties(0, 10));
        weaponProps.put(EnumWeaponType.SHORTSWORD, new WeaponTypeProperties(7, 13));
        weaponProps.put(EnumWeaponType.LONGSWORD, new WeaponTypeProperties(40, 17));
        weaponProps.put(EnumWeaponType.SPEAR, new WeaponTypeProperties(3, 13));
        weaponProps.put(EnumWeaponType.HAXE, new WeaponTypeProperties(23, 16));
        weaponProps.put(EnumWeaponType.DUAL, new WeaponTypeProperties(10, 10));
        weaponProps.put(EnumWeaponType.GLOVE, new WeaponTypeProperties(3, 10));
        weaponProps.put(EnumWeaponType.STAFF, new WeaponTypeProperties(0, 20));

        skillProps.put(EnumSkills.SHORTSWORD, new SkillProperties(100, 0, 0.5f, 0.25f, 0, 0, 1));
        skillProps.put(EnumSkills.LONGSWORD, new SkillProperties(100, 0, 0.5f, 0.25f, 0, 0, 1));
        skillProps.put(EnumSkills.SPEAR, new SkillProperties(100, 0, 0.5f, 0.25f, 0, 0, 1));
        skillProps.put(EnumSkills.HAMMERAXE, new SkillProperties(100, 0, 0.5f, 0.5f, 0, 0, 1));
        skillProps.put(EnumSkills.DUAL, new SkillProperties(100, 0, 0.5f, 0.25f, 0, 0, 1));
        skillProps.put(EnumSkills.FIST, new SkillProperties(100, 0, 0.5f, 0.25f, 0, 0, 1));

        skillProps.put(EnumSkills.FIRE, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.WATER, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.EARTH, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.WIND, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.DARK, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.LIGHT, new SkillProperties(100, 0, 0.5f, 0, 0, 0.25f, 1));
        skillProps.put(EnumSkills.LOVE, new SkillProperties(100, 0.1f, 0.5f, 0, 0, 0.25f, 1));

        skillProps.put(EnumSkills.FARMING, new SkillProperties(100, 1.25f, 1, 0, 0.5f, 0, 1));
        skillProps.put(EnumSkills.LOGGING, new SkillProperties(100, 1, 1, 0.25f, 0.2f, 0, 1));
        skillProps.put(EnumSkills.MINING, new SkillProperties(100, 1, 1, 0.2f, 0.4f, 0, 1));
        skillProps.put(EnumSkills.FISHING, new SkillProperties(100, 0, 0.5f, 0, 0, 0.5f, 1));

        skillProps.put(EnumSkills.COOKING, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0, 1));
        skillProps.put(EnumSkills.FORGING, new SkillProperties(100, 0, 0.25f, 0.25f, 0.1f, 0, 1));
        skillProps.put(EnumSkills.CHEMISTRY, new SkillProperties(100, 0, 0.25f, 0, 0.1f, 0.2f, 1));
        skillProps.put(EnumSkills.CRAFTING, new SkillProperties(100, 0, 0.25f, 0, 0, 0.1f, 1));

        skillProps.put(EnumSkills.SEARCHING, new SkillProperties(100, 0, 0.5f, 0, 0, 0.1f, 1));
        skillProps.put(EnumSkills.WALKING, new SkillProperties(100, 0.5f, 0.125f, 0, 0.1f, 0, 1));
        skillProps.put(EnumSkills.SLEEPING, new SkillProperties(100, 2f, 2, 0.5f, 1, 0.5f, 1));
        skillProps.put(EnumSkills.EATING, new SkillProperties(100, 1.5f, 5, 0.5f, 0.5f, 0.2f, 1));
        skillProps.put(EnumSkills.DEFENCE, new SkillProperties(100, 1.5f, 0.1f, 0, 1.5f, 0, 1));

        skillProps.put(EnumSkills.RESPOISON, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));
        skillProps.put(EnumSkills.RESSEAL, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));
        skillProps.put(EnumSkills.RESPARA, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));
        skillProps.put(EnumSkills.RESSLEEP, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));
        skillProps.put(EnumSkills.RESFAT, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));
        skillProps.put(EnumSkills.RESCOLD, new SkillProperties(100, 0.25f, 0, 0, 0.2f, 0, 1));

        skillProps.put(EnumSkills.BATH, new SkillProperties(100, 0.5f, 1, 0, 0.25f, 0, 1));
        skillProps.put(EnumSkills.TAMING, new SkillProperties(100, 0, 0.5f, 0, 0.25f, 0.25f, 1));
        skillProps.put(EnumSkills.LEADER, new SkillProperties(100, 1, 0, 0.25f, 0, 0.1f, 1));
    }

    public enum RecipeSystem {

        SKILL(false, true, true),
        SKILLIGNORELOCK(false, true, false),
        SKILLBLOCKLOCK(false, false, false),
        BASE(true, true, true),
        BASEIGNORELOCK(true, true, false),
        BASEBLOCKLOCK(true, false, false);

        public final boolean baseCost, allowLocked, lockedCostMore;

        RecipeSystem(boolean baseCost, boolean allowLocked, boolean lockedCostMore) {
            this.baseCost = baseCost;
            this.allowLocked = allowLocked;
            this.lockedCostMore = lockedCostMore;
        }

        public boolean lockIsIgnored() {
            return this.allowLocked && !this.lockedCostMore;
        }
    }
}
