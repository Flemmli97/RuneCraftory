package io.github.flemmli97.runecraftory.forge.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.forge.config.values.EntityPropertySpecs;
import io.github.flemmli97.runecraftory.forge.config.values.SkillPropertySpecs;
import io.github.flemmli97.runecraftory.forge.config.values.WeaponTypePropertySpecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public record ConfigHolder<T>(ModConfig.Type configType, String configName,
                              T configSpec, Consumer<T> loader) {

    public static final Map<ForgeConfigSpec, ConfigHolder<?>> configs = new LinkedHashMap<>();

    static {
        configs.put(GeneralConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.COMMON, RuneCraftory.MODID + File.separator + "general.toml",
                GeneralConfigSpec.spec.getLeft(), ConfigHolder::loadGeneral));
        configs.put(ClientConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.CLIENT, RuneCraftory.MODID + File.separator + "client.toml",
                ClientConfigSpec.spec.getLeft(), ConfigHolder::loadClient));
        configs.put(MobConfigSpec.spec.getRight(), new ConfigHolder<>(ModConfig.Type.COMMON, RuneCraftory.MODID + File.separator + "mobs.toml",
                MobConfigSpec.spec.getLeft(), ConfigHolder::loadMobs));
    }

    public void reloadConfig() {
        this.loader.accept(this.configSpec);
    }

    public static void loadGeneral(GeneralConfigSpec spec) {
        GeneralConfig.disableDefence = spec.disableDefence.get();
        GeneralConfig.gateSpawning = spec.gateSpawning.get();
        GeneralConfig.disableVanillaSpawning = spec.disableVanillaSpawning.get();
        GeneralConfig.randomDamage = spec.randomDamage.get();
        GeneralConfig.recipeSystem = spec.recipeSystem.get();
        GeneralConfig.useRP = spec.useRP.get();
        GeneralConfig.deathHPPercent = spec.deathHPPercent.get().floatValue();
        GeneralConfig.deathRPPercent = spec.deathRPPercent.get().floatValue();
        GeneralConfig.disableHunger = spec.disableHunger.get();
        GeneralConfig.modifyWeather = spec.modifyWeather.get();
        GeneralConfig.modifyBed = spec.modifyBed.get();
        GeneralConfig.disableFoodSystem = spec.disableFoodSystem.get();
        GeneralConfig.disableItemStatSystem = spec.disableItemStatSystem.get();
        GeneralConfig.disableCropSystem = spec.disableCropSystem.get();

        GeneralConfig.maxLevel = spec.maxLevel.get();
        GeneralConfig.maxSkillLevel = spec.maxSkillLevel.get();
        GeneralConfig.startingHealth = spec.startingHealth.get();
        GeneralConfig.startingRP = spec.startingRP.get();
        GeneralConfig.startingMoney = spec.startingMoney.get();
        GeneralConfig.startingStr = spec.startingStr.get();
        GeneralConfig.startingVit = spec.startingVit.get();
        GeneralConfig.startingIntel = spec.startingIntel.get();
        GeneralConfig.hpPerLevel = spec.hpPerLevel.get().floatValue();
        GeneralConfig.rpPerLevel = spec.rpPerLevel.get().floatValue();
        GeneralConfig.strPerLevel = spec.strPerLevel.get().floatValue();
        GeneralConfig.vitPerLevel = spec.vitPerLevel.get().floatValue();
        GeneralConfig.intPerLevel = spec.intPerLevel.get().floatValue();
        spec.skillProps.forEach((type, specs) -> GeneralConfig.skillProps.put(type, SkillPropertySpecs.ofSpec(specs)));

        GeneralConfig.scrapMultiplier = spec.scrapMultiplier.get().floatValue();
        GeneralConfig.ironMultiplier = spec.ironMultiplier.get().floatValue();
        GeneralConfig.silverMultiplier = spec.silverMultiplier.get().floatValue();
        GeneralConfig.goldMultiplier = spec.goldMultiplier.get().floatValue();
        GeneralConfig.platinumMultiplier = spec.platinumMultiplier.get().floatValue();
        GeneralConfig.platinumChargeTime = spec.platinumChargeTime.get().floatValue();
        GeneralConfig.scrapWateringCanWater = spec.scrapWateringCanWater.get();
        GeneralConfig.ironWateringCanWater = spec.ironWateringCanWater.get();
        GeneralConfig.silverWateringCanWater = spec.silverWateringCanWater.get();
        GeneralConfig.goldWateringCanWater = spec.goldWateringCanWater.get();
        GeneralConfig.platinumWateringCanWater = spec.platinumWateringCanWater.get();
        spec.weaponProps.forEach((type, specs) -> GeneralConfig.weaponProps.put(type, WeaponTypePropertySpecs.ofSpec(specs)));

        GeneralConfig.xpMultiplier = spec.xpMultiplier.get().floatValue();
        GeneralConfig.skillXpMultiplier = spec.skillXpMultiplier.get().floatValue();
        GeneralConfig.tamingMultiplier = spec.tamingMultiplier.get().floatValue();

        GeneralConfig.debugAttack = spec.debugAttack.get();
    }

    public static void loadClient(ClientConfigSpec spec) {
        ClientConfig.healthBarWidgetX = spec.healthBarWidgetX.get();
        ClientConfig.healthBarWidgetY = spec.healthBarWidgetY.get();
        ClientConfig.seasonDisplayX = spec.seasonDisplayX.get();
        ClientConfig.seasonDisplayY = spec.seasonDisplayY.get();
        ClientConfig.inventoryOffsetX = spec.inventoryOffsetX.get();
        ClientConfig.inventoryOffsetY = spec.inventoryOffsetY.get();
        ClientConfig.creativeInventoryOffsetX = spec.creativeInventoryOffsetX.get();
        ClientConfig.creativeInventoryOffsetY = spec.creativeInventoryOffsetY.get();
        ClientConfig.renderOverlay = spec.renderOverlay.get();
        ClientConfig.inventoryButton = spec.inventoryButton.get();
    }

    public static void loadMobs(MobConfigSpec spec) {
        MobConfig.disableNaturalSpawn = spec.disableNaturalSpawn.get();
        MobConfig.gateHealth = spec.gateHealth.get();
        MobConfig.gateDef = spec.gateDef.get();
        MobConfig.gateMDef = spec.gateMDef.get();
        MobConfig.gateHealthGain = spec.gateHealthGain.get();
        MobConfig.gateDefGain = spec.gateDefGain.get();
        MobConfig.gateMDefGain = spec.gateMDefGain.get();
        MobConfig.spawnChance = spec.spawnChance.get();
        MobConfig.gateXP = spec.gateXP.get();
        MobConfig.gateMoney = spec.gateMoney.get();

        for (Map.Entry<ResourceLocation, EntityPropertySpecs> e : spec.mobSpecs.entrySet()) {
            MobConfig.propertiesMap.put(e.getKey(), EntityPropertySpecs.ofSpec(e.getValue()));
        }
    }
}
