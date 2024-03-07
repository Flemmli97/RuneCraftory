package io.github.flemmli97.runecraftory.fabric.config;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.network.S2CSyncConfig;
import io.github.flemmli97.runecraftory.fabric.RuneCraftoryFabric;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public record ConfigHolder<T>(T configSpec, Consumer<T> loader) {

    public static final Map<JsonConfig<CommentedJsonConfig>, ConfigHolder<?>> CONFIGS = new LinkedHashMap<>();

    static {
        CONFIGS.put(GeneralConfigSpec.SPEC.getLeft(), new ConfigHolder<>(GeneralConfigSpec.SPEC.getRight(), ConfigHolder::loadGeneral));
        CONFIGS.put(ClientConfigSpec.SPEC.getLeft(), new ConfigHolder<>(ClientConfigSpec.SPEC.getRight(), ConfigHolder::loadClient));
        CONFIGS.put(MobConfigSpec.SPEC.getLeft(), new ConfigHolder<>(MobConfigSpec.SPEC.getRight(), ConfigHolder::loadMobs));
    }

    public static void loadGeneral(GeneralConfigSpec spec) {
        GeneralConfig.DEFENCE_SYSTEM = spec.defenceSystem.get();
        GeneralConfig.GATE_SPAWNING = spec.gateSpawning.get();
        GeneralConfig.DISABLE_VANILLA_SPAWNING = spec.disableVanillaSpawning.get();
        GeneralConfig.RANDOM_DAMAGE = spec.randomDamage.get();
        GeneralConfig.RECIPE_SYSTEM = spec.recipeSystem.get();
        GeneralConfig.USE_RP = spec.useRP.get();
        GeneralConfig.DEATH_HP_PERCENT = spec.deathHPPercent.get().floatValue();
        GeneralConfig.DEATH_RP_PERCENT = spec.deathRPPercent.get().floatValue();
        GeneralConfig.DISABLE_HUNGER = spec.disableHunger.get();
        GeneralConfig.MODIFY_WEATHER = spec.modifyWeather.get();
        GeneralConfig.MODIFY_BED = spec.modifyBed.get();
        GeneralConfig.HEAL_ON_WAKE_UP = spec.healOnWakeUp.get();
        GeneralConfig.DISABLE_FOOD_SYSTEM = spec.disableFoodSystem.get();
        GeneralConfig.DISABLE_ITEM_STAT_SYSTEM = spec.disableItemStatSystem.get();
        GeneralConfig.DISABLE_CROP_SYSTEM = spec.disableCropSystem.get();
        GeneralConfig.SEASONED_SNOW = spec.seasonedSnow.get();
        GeneralConfig.MAX_PARTY_SIZE = spec.maxPartySize.get();

        GeneralConfig.WITHER_CHANCE = spec.witherChance.get().floatValue();
        GeneralConfig.RUNEY_CHANCE = spec.runeyChance.get().floatValue();
        GeneralConfig.DISABLE_FARMLAND_RANDOMTICK = spec.disableFarmlandRandomtick.get();
        GeneralConfig.DISABLE_FARMLAND_TRAMPLE = spec.disableFarmlandTrample.get();
        GeneralConfig.TICK_UNLOADED_FARMLAND = spec.tickUnloadedFarmland.get();
        GeneralConfig.UNLOADED_FARMLAND_CHECK_WATER = spec.unloadedFarmlandCheckWater.get();

        GeneralConfig.MAX_LEVEL = spec.maxLevel.get();
        GeneralConfig.STARTING_HEALTH = spec.startingHealth.get();
        GeneralConfig.STARTING_RP = spec.startingRP.get();
        GeneralConfig.STARTING_MONEY = spec.startingMoney.get();
        GeneralConfig.STARTING_STR = spec.startingStr.get();
        GeneralConfig.STARTING_VIT = spec.startingVit.get();
        GeneralConfig.STARTING_INTEL = spec.startingIntel.get();
        GeneralConfig.HP_PER_LEVEL = spec.hpPerLevel.get().floatValue();
        GeneralConfig.RP_PER_LEVEL = spec.rpPerLevel.get().floatValue();
        GeneralConfig.STR_PER_LEVEL = spec.strPerLevel.get().floatValue();
        GeneralConfig.VIT_PER_LEVEL = spec.vitPerLevel.get().floatValue();
        GeneralConfig.INT_PER_LEVEL = spec.intPerLevel.get().floatValue();
        GeneralConfig.SHORT_SWORD_ULTIMATE = spec.shortSwordUltimate.get().floatValue();
        GeneralConfig.LONG_SWORD_ULTIMATE = spec.longSwordUltimate.get().floatValue();
        GeneralConfig.SPEAR_ULTIMATE = spec.spearUltimate.get().floatValue();
        GeneralConfig.HAMMER_AXE_ULTIMATE = spec.hammerAxeUltimate.get().floatValue();
        GeneralConfig.DUAL_BLADE_ULTIMATE = spec.dualBladeUltimate.get().floatValue();
        GeneralConfig.GLOVE_ULTIMATE = spec.gloveUltimate.get().floatValue();

        GeneralConfig.PLATINUM_CHARGE_TIME = spec.platinumChargeTime.get().floatValue();
        GeneralConfig.SCRAP_WATERING_CAN_WATER = spec.scrapWateringCanWater.get();
        GeneralConfig.IRON_WATERING_CAN_WATER = spec.ironWateringCanWater.get();
        GeneralConfig.SILVER_WATERING_CAN_WATER = spec.silverWateringCanWater.get();
        GeneralConfig.GOLD_WATERING_CAN_WATER = spec.goldWateringCanWater.get();
        GeneralConfig.PLATINUM_WATERING_CAN_WATER = spec.platinumWateringCanWater.get();
        GeneralConfig.ALLOW_MOVE_ON_ATTACK.read(spec.allowMoveOnAttack.get());

        GeneralConfig.XP_MULTIPLIER = spec.xpMultiplier.get().floatValue();
        GeneralConfig.SKILL_XP_MULTIPLIER = spec.skillXpMultiplier.get().floatValue();
        GeneralConfig.TAMING_MULTIPLIER = spec.tamingMultiplier.get().floatValue();

        GeneralConfig.DEBUG_ATTACK = spec.debugAttack.get();

        if (RuneCraftoryFabric.getServerInstance() != null)
            Platform.INSTANCE.sendToAll(new S2CSyncConfig(), RuneCraftoryFabric.getServerInstance());
    }

    public static void loadClient(ClientConfigSpec spec) {
        ClientConfig.HEALTH_BAR_WIDGET_X = spec.healthBarWidgetX.get();
        ClientConfig.HEALTH_BAR_WIDGET_Y = spec.healthBarWidgetY.get();
        ClientConfig.HEALTH_BAR_WIDGET_POSITION = spec.healthBarWidgetPosition.get();
        ClientConfig.SEASON_DISPLAY_X = spec.seasonDisplayX.get();
        ClientConfig.SEASON_DISPLAY_Y = spec.seasonDisplayY.get();
        ClientConfig.SEASON_DISPLAY_POSITION = spec.seasonDisplayPosition.get();
        ClientConfig.INVENTORY_OFFSET_X = spec.inventoryOffsetX.get();
        ClientConfig.INVENTORY_OFFSET_Y = spec.inventoryOffsetY.get();
        ClientConfig.CREATIVE_INVENTORY_OFFSET_X = spec.creativeInventoryOffsetX.get();
        ClientConfig.CREATIVE_INVENTORY_OFFSET_Y = spec.creativeInventoryOffsetY.get();
        ClientConfig.FARMLAND_X = spec.farmlandX.get();
        ClientConfig.FARMLAND_Y = spec.farmlandY.get();
        ClientConfig.FARMLAND_POSITION = spec.farmlandPosition.get();
        ClientConfig.RENDER_HEALTH_RP_BAR = spec.renderHealthRPBar.get();
        ClientConfig.RENDER_CALENDAR = spec.renderCalendar.get();
        ClientConfig.INVENTORY_BUTTON = spec.inventoryButton.get();
        ClientConfig.GRASS_COLOR = spec.grassColor.get();
        ClientConfig.FOLIAGE_COLOR = spec.foliageColor.get();
    }

    public static void loadMobs(MobConfigSpec spec) {
        MobConfig.DISABLE_NATURAL_SPAWN = spec.disableNaturalSpawn.get();
        MobConfig.FARM_RADIUS = spec.farmRadius.get();
        MobConfig.MOB_ATTACK_NPC = spec.mobAttackNPC.get();
        MobConfig.VANILLA_GIVE_XP = spec.vanillaGiveXp.get();
        MobConfig.MONSTER_NEED_BARN = spec.monsterNeedBarn.get();
        MobConfig.BELL_RADIUS = spec.bellRadius.get();
        MobConfig.GATE_HEALTH = spec.gateHealth.get();
        MobConfig.GATE_DEF = spec.gateDef.get();
        MobConfig.GATE_M_DEF = spec.gateMDef.get();
        MobConfig.GATE_HEALTH_GAIN = spec.gateHealthGain.get();
        MobConfig.GATE_DEF_GAIN = spec.gateDefGain.get();
        MobConfig.GATE_M_DEF_GAIN = spec.gateMDefGain.get();
        MobConfig.GATE_XP = spec.gateXP.get();
        MobConfig.GATE_MONEY = spec.gateMoney.get();
        MobConfig.MIN_SPAWN_DELAY = spec.minSpawnDelay.get();
        MobConfig.MAX_SPAWN_DELAY = Math.max(MobConfig.MIN_SPAWN_DELAY, spec.maxSpawnDelay.get());
        MobConfig.MIN_DIST = spec.minDist.get();
        MobConfig.MAX_GROUP = spec.maxGroup.get();
        MobConfig.MIN_NEARBY = spec.minNearby.get();
        MobConfig.MAX_NEARBY = spec.maxNearby.get();
        MobConfig.BASE_GATE_LEVEL = spec.baseGateLevel.get();
        MobConfig.GATE_LEVEL_TYPE = spec.gateLevelType.get();
        MobConfig.PLAYER_LEVEL_TYPE = spec.playerLevelType.get();
        MobConfig.TREASURE_CHANCE = spec.treasureChance.get().floatValue();
        MobConfig.MIMIC_CHANCE = spec.mimicChance.get().floatValue();
        MobConfig.MIMIC_STRONG_CHANCE = spec.mimicStrongChance.get().floatValue();
        MobConfig.NPC_SPAWN_RATE_MIN = spec.npcSpawnRateMin.get();
        MobConfig.NPC_SPAWN_RATE_MAX = spec.npcSpawnRateMax.get();
    }

    public void reloadConfig() {
        this.loader.accept(this.configSpec);
    }
}
