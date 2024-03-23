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
        GeneralConfig.defenceSystem = spec.defenceSystem.get();
        GeneralConfig.gateSpawning = spec.gateSpawning.get();
        GeneralConfig.disableVanillaSpawning = spec.disableVanillaSpawning.get();
        GeneralConfig.randomDamage = spec.randomDamage.get();
        GeneralConfig.recipeSystem = spec.recipeSystem.get();
        GeneralConfig.useRp = spec.useRP.get();
        GeneralConfig.deathHpPercent = spec.deathHPPercent.get().floatValue();
        GeneralConfig.deathRpPercent = spec.deathRPPercent.get().floatValue();
        GeneralConfig.disableHunger = spec.disableHunger.get();
        GeneralConfig.modifyWeather = spec.modifyWeather.get();
        GeneralConfig.modifyBed = spec.modifyBed.get();
        GeneralConfig.healOnWakeUp = spec.healOnWakeUp.get();
        GeneralConfig.disableFoodSystem = spec.disableFoodSystem.get();
        GeneralConfig.disableItemStatSystem = spec.disableItemStatSystem.get();
        GeneralConfig.disableCropSystem = spec.disableCropSystem.get();
        GeneralConfig.seasonedSnow = spec.seasonedSnow.get();
        GeneralConfig.maxPartySize = spec.maxPartySize.get();

        GeneralConfig.witherChance = spec.witherChance.get().floatValue();
        GeneralConfig.runeyChance = spec.runeyChance.get().floatValue();
        GeneralConfig.disableFarmlandRandomtick = spec.disableFarmlandRandomtick.get();
        GeneralConfig.disableFarmlandTrample = spec.disableFarmlandTrample.get();
        GeneralConfig.tickUnloadedFarmland = spec.tickUnloadedFarmland.get();
        GeneralConfig.unloadedFarmlandCheckWater = spec.unloadedFarmlandCheckWater.get();

        GeneralConfig.maxLevel = spec.maxLevel.get();
        GeneralConfig.startingHealth = spec.startingHealth.get();
        GeneralConfig.startingRp = spec.startingRP.get();
        GeneralConfig.startingMoney = spec.startingMoney.get();
        GeneralConfig.startingStr = spec.startingStr.get();
        GeneralConfig.startingVit = spec.startingVit.get();
        GeneralConfig.startingIntel = spec.startingIntel.get();
        GeneralConfig.hpPerLevel = spec.hpPerLevel.get().floatValue();
        GeneralConfig.rpPerLevel = spec.rpPerLevel.get().floatValue();
        GeneralConfig.strPerLevel = spec.strPerLevel.get().floatValue();
        GeneralConfig.vitPerLevel = spec.vitPerLevel.get().floatValue();
        GeneralConfig.intPerLevel = spec.intPerLevel.get().floatValue();
        GeneralConfig.shortSwordUltimate = spec.shortSwordUltimate.get().floatValue();
        GeneralConfig.longSwordUltimate = spec.longSwordUltimate.get().floatValue();
        GeneralConfig.spearUltimate = spec.spearUltimate.get().floatValue();
        GeneralConfig.hammerAxeUltimate = spec.hammerAxeUltimate.get().floatValue();
        GeneralConfig.dualBladeUltimate = spec.dualBladeUltimate.get().floatValue();
        GeneralConfig.gloveUltimate = spec.gloveUltimate.get().floatValue();

        GeneralConfig.platinumChargeTime = spec.platinumChargeTime.get().floatValue();
        GeneralConfig.scrapWateringCanWater = spec.scrapWateringCanWater.get();
        GeneralConfig.ironWateringCanWater = spec.ironWateringCanWater.get();
        GeneralConfig.silverWateringCanWater = spec.silverWateringCanWater.get();
        GeneralConfig.goldWateringCanWater = spec.goldWateringCanWater.get();
        GeneralConfig.platinumWateringCanWater = spec.platinumWateringCanWater.get();
        GeneralConfig.allowMoveOnAttack.read(spec.allowMoveOnAttack.get());

        GeneralConfig.xpMultiplier = spec.xpMultiplier.get().floatValue();
        GeneralConfig.skillXpMultiplier = spec.skillXpMultiplier.get().floatValue();
        GeneralConfig.tamingMultiplier = spec.tamingMultiplier.get().floatValue();

        GeneralConfig.debugAttack = spec.debugAttack.get();

        if (RuneCraftoryFabric.getServerInstance() != null)
            Platform.INSTANCE.sendToAll(new S2CSyncConfig(), RuneCraftoryFabric.getServerInstance());
    }

    public static void loadClient(ClientConfigSpec spec) {
        ClientConfig.healthBarWidgetX = spec.healthBarWidgetX.get();
        ClientConfig.healthBarWidgetY = spec.healthBarWidgetY.get();
        ClientConfig.healthBarWidgetPosition = spec.healthBarWidgetPosition.get();
        ClientConfig.seasonDisplayX = spec.seasonDisplayX.get();
        ClientConfig.seasonDisplayY = spec.seasonDisplayY.get();
        ClientConfig.seasonDisplayPosition = spec.seasonDisplayPosition.get();
        ClientConfig.inventoryOffsetX = spec.inventoryOffsetX.get();
        ClientConfig.inventoryOffsetY = spec.inventoryOffsetY.get();
        ClientConfig.creativeInventoryOffsetX = spec.creativeInventoryOffsetX.get();
        ClientConfig.creativeInventoryOffsetY = spec.creativeInventoryOffsetY.get();
        ClientConfig.farmlandX = spec.farmlandX.get();
        ClientConfig.farmlandY = spec.farmlandY.get();
        ClientConfig.farmlandPosition = spec.farmlandPosition.get();
        ClientConfig.renderHealthRpBar = spec.renderHealthRPBar.get();
        ClientConfig.renderCalendar = spec.renderCalendar.get();
        ClientConfig.inventoryButton = spec.inventoryButton.get();
        ClientConfig.grassColor = spec.grassColor.get();
        ClientConfig.foliageColor = spec.foliageColor.get();
        ClientConfig.bossMusic = spec.bossMusic.get();
        ClientConfig.bossMusicFadeDelay = spec.bossMusicFadeDelay.get();
    }

    public static void loadMobs(MobConfigSpec spec) {
        MobConfig.disableNaturalSpawn = spec.disableNaturalSpawn.get();
        MobConfig.farmRadius = spec.farmRadius.get();
        MobConfig.mobAttackNpc = spec.mobAttackNPC.get();
        MobConfig.vanillaGiveXp = spec.vanillaGiveXp.get();
        MobConfig.monsterNeedBarn = spec.monsterNeedBarn.get();
        MobConfig.bellRadius = spec.bellRadius.get();
        MobConfig.gateHealth = spec.gateHealth.get();
        MobConfig.gateDef = spec.gateDef.get();
        MobConfig.gateMDef = spec.gateMDef.get();
        MobConfig.gateHealthGain = spec.gateHealthGain.get();
        MobConfig.gateDefGain = spec.gateDefGain.get();
        MobConfig.gateMDefGain = spec.gateMDefGain.get();
        MobConfig.gateXp = spec.gateXP.get();
        MobConfig.gateMoney = spec.gateMoney.get();
        MobConfig.minSpawnDelay = spec.minSpawnDelay.get();
        MobConfig.maxSpawnDelay = Math.max(MobConfig.minSpawnDelay, spec.maxSpawnDelay.get());
        MobConfig.minDist = spec.minDist.get();
        MobConfig.maxGroup = spec.maxGroup.get();
        MobConfig.minNearby = spec.minNearby.get();
        MobConfig.maxNearby = spec.maxNearby.get();
        MobConfig.baseGateLevel = spec.baseGateLevel.get();
        MobConfig.gateLevelType = spec.gateLevelType.get();
        MobConfig.levelZones.readFromString(spec.levelZones.get());
        MobConfig.playerLevelType = spec.playerLevelType.get();
        MobConfig.treasureChance = spec.treasureChance.get().floatValue();
        MobConfig.mimicChance = spec.mimicChance.get().floatValue();
        MobConfig.mimicStrongChance = spec.mimicStrongChance.get().floatValue();
        MobConfig.npcSpawnRateMin = spec.npcSpawnRateMin.get();
        MobConfig.npcSpawnRateMax = spec.npcSpawnRateMax.get();
        MobConfig.initialProcreationCooldown = spec.initialProcreationCooldown.get();
        MobConfig.procreationCooldown = spec.procreationCooldown.get();
    }

    public void reloadConfig() {
        this.loader.accept(this.configSpec);
    }
}
