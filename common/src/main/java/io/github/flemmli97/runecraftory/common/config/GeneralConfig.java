package io.github.flemmli97.runecraftory.common.config;

public class GeneralConfig {

    public static DefenceSystem DEFENCE_SYSTEM = DefenceSystem.IGNORE_VANILLA_PLAYER;
    public static boolean VANILLA_IGNORE_DEFENCE_PLAYER = true;
    public static boolean GATE_SPAWNING = true;
    public static boolean DISABLE_VANILLA_SPAWNING = false;
    public static boolean RANDOM_DAMAGE = true;
    public static RecipeSystem RECIPE_SYSTEM = RecipeSystem.SKILL;
    public static boolean USE_RP = true;
    public static float DEATH_HP_PERCENT = 1;
    public static float DEATH_RP_PERCENT = 0.5f;
    public static boolean DISABLE_HUNGER = false;
    public static boolean MODIFY_WEATHER = true;
    public static boolean MODIFY_BED = false;
    public static boolean HEAL_ON_WAKE_UP = true;
    public static boolean DISABLE_FOOD_SYSTEM = false;
    public static boolean DISABLE_ITEM_STAT_SYSTEM = false;
    public static boolean DISABLE_CROP_SYSTEM = false;
    public static boolean SEASONED_SNOW = true;
    public static int MAX_PARTY_SIZE = 3;

    public static float WITHER_CHANCE = 0.5f;
    public static float RUNEY_CHANCE = 0.05f;
    public static boolean DISABLE_FARMLAND_RANDOMTICK = true;
    public static boolean DISABLE_FARMLAND_TRAMPLE = true;
    public static boolean TICK_UNLOADED_FARMLAND = true;
    public static boolean UNLOADED_FARMLAND_CHECK_WATER = true;

    public static boolean SEASONS = true;
    public static boolean DYNAMIC_TREES = true;

    public static int MAX_LEVEL = 999;
    public static int STARTING_HEALTH = 20;
    public static int STARTING_RP = 100;
    public static int STARTING_MONEY = 100;
    public static int STARTING_STR = 0;
    public static int STARTING_VIT = 0;
    public static int STARTING_INTEL = 3;
    public static float HP_PER_LEVEL = 5;
    public static float RP_PER_LEVEL = 2;
    public static float STR_PER_LEVEL = 1;
    public static float VIT_PER_LEVEL = 1.5f;
    public static float INT_PER_LEVEL = 1;
    public static float SHORT_SWORD_ULTIMATE = 7;
    public static float LONG_SWORD_ULTIMATE = 7;
    public static float SPEAR_ULTIMATE = 7;
    public static float HAMMER_AXE_ULTIMATE = 7;
    public static float DUAL_BLADE_ULTIMATE = 7;
    public static float GLOVE_ULTIMATE = 7;

    public static float PLATINUM_CHARGE_TIME = 0.5f;
    public static int SCRAP_WATERING_CAN_WATER = 25;
    public static int IRON_WATERING_CAN_WATER = 35;
    public static int SILVER_WATERING_CAN_WATER = 100;
    public static int GOLD_WATERING_CAN_WATER = 150;
    public static int PLATINUM_WATERING_CAN_WATER = 250;

    public static final ServerValue<Boolean> ALLOW_MOVE_ON_ATTACK = new ServerValue.SyncedBoolean(false, "config.move.attack");

    public static float XP_MULTIPLIER = 1;
    public static float SKILL_XP_MULTIPLIER = 1;
    public static float TAMING_MULTIPLIER = 1;

    public static boolean DEBUG_ATTACK = false;

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
