package io.github.flemmli97.runecraftory.common.config;

public class GeneralConfig {

    public static DefenceSystem defenceSystem = DefenceSystem.IGNORE_VANILLA_PLAYER;
    public static boolean gateSpawning = true;
    public static boolean disableVanillaSpawning = false;
    public static boolean randomDamage = true;
    public static RecipeSystem recipeSystem = RecipeSystem.SKILL;
    public static boolean useRp = true;
    public static float deathHpPercent = 1;
    public static float deathRpPercent = 0.5f;
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

    public static boolean seasons = true;
    public static boolean dynamicTrees = true;

    public static int maxLevel = 999;
    public static int startingHealth = 20;
    public static int startingRp = 100;
    public static int startingMoney = 100;
    public static int startingStr = 0;
    public static int startingVit = 0;
    public static int startingIntel = 3;
    public static float hpPerLevel = 5;
    public static float rpPerLevel = 2;
    public static float strPerLevel = 1;
    public static float vitPerLevel = 1.5f;
    public static float intPerLevel = 1;
    public static float shortSwordUltimate = 7;
    public static float longSwordUltimate = 7;
    public static float spearUltimate = 7;
    public static float hammerAxeUltimate = 7;
    public static float dualBladeUltimate = 7;
    public static float gloveUltimate = 7;

    public static float platinumChargeTime = 0.5f;
    public static int scrapWateringCanWater = 25;
    public static int ironWateringCanWater = 35;
    public static int silverWateringCanWater = 100;
    public static int goldWateringCanWater = 150;
    public static int platinumWateringCanWater = 250;

    public static final ServerValue<Boolean> allowMoveOnAttack = new ServerValue.SyncedBoolean(false, "config.move.attack");

    public static float xpMultiplier = 1;
    public static float skillXpMultiplier = 1;
    public static float tamingMultiplier = 1;

    public static boolean debugAttack = false;

    public enum DefenceSystem {
        NO_DEFENCE,
        VANILLA_IGNORE,
        IGNORE_VANILLA_MOBS,
        IGNORE_VANILLA_PLAYER_ATT,
        IGNORE_VANILLA_PLAYER_HURT,
        IGNORE_VANILLA_PLAYER,
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
