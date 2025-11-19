package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.api.datapack.ExperienceCache;
import io.github.flemmli97.runecraftory.api.datapack.ExpressionHolder;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;

public class GeneralConfig {

    public static DefenceSystem defenceSystem = DefenceSystem.IGNORE_NONE;
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
    public static boolean hotSpringSource;

    public static float witherChance = 0.5f;
    public static float runeyChance = 0.05f;
    public static boolean disableFarmlandRandomtick = true;
    public static boolean disableFarmlandTrample = true;
    public static boolean tickUnloadedFarmland = true;
    public static boolean unloadedFarmlandCheckWater = true;

    public static final ServerValue<Boolean> SERENE_SEASONS = new ServerValue.SyncedBoolean(true, "config.integration.seasons");

    public static int maxLevel = 999;
    public static int startingHealth = 20;
    public static int startingRp = 100;
    public static int startingMoney = 100;
    public static int startingStr = 1;
    public static int startingVit = 0;
    public static int startingIntel = 1;
    public static float hpPerLevel = 3;
    public static float rpPerLevel = 2;
    public static float strPerLevel = 0.5f;
    public static float vitPerLevel = 0.4f;
    public static float intPerLevel = 0.5f;
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

    public static final ServerValue<Double> MOVE_SPEED_ATTACK = new ServerValue.SyncedDouble(0.2, "config.move.attack");

    public static float xpMultiplier = 1;
    public static float skillXpMultiplier = 1;
    public static float tamingMultiplier = 1;
    public static ExperienceCache experienceLevel = new ExperienceCache(() -> GeneralConfig.maxLevel, new ExpressionHolder("10 + level * 10 + 15 * level ^ 1.25 + (level / 10) * 250 + (level / 20) * (level / 20) * 1000"), true);
    public static ExperienceCache friendPointsExperience = new ExperienceCache(() -> 20, new ExpressionHolder("level >= 10 ? 1000 : 45 + level * 5 + level * level * 10"), true);

    public static boolean debugAttack = false;

    public static int getWaterFrom(ToolItemTier tier) {
        return switch (tier) {
            case SCRAP -> 25;
            case IRON -> 35;
            case SILVER -> 100;
            case GOLD -> 150;
            case PLATINUM -> 250;
        };
    }

    public enum DefenceSystem {
        NO_DEFENCE,
        VANILLA_IGNORE,
        IGNORE_VANILLA_MOBS,
        IGNORE_VANILLA_PLAYER_ATT,
        IGNORE_VANILLA_PLAYER_HURT,
        IGNORE_VANILLA_PLAYER,
        IGNORE_NONE,
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
