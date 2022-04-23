package io.github.flemmli97.runecraftory.forge;

import io.github.flemmli97.runecraftory.common.config.SpawnConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModLootCondition;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.forge.client.ClientEvents;
import io.github.flemmli97.runecraftory.forge.config.ConfigHolder;
import io.github.flemmli97.runecraftory.forge.event.EntityEvents;
import io.github.flemmli97.runecraftory.forge.event.WorldEvents;
import io.github.flemmli97.runecraftory.forge.network.PacketHandler;
import io.github.flemmli97.runecraftory.forge.registry.ModLootModifier;
import io.github.flemmli97.runecraftory.mixin.AttributeAccessor;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Map;

@Mod(value = RuneCraftoryForge.MODID)
public class RuneCraftoryForge {

    public static final String MODID = "runecraftory";
    public static final Logger logger = LogManager.getLogger(RuneCraftoryForge.MODID);

    public static Path confDir;

    public RuneCraftoryForge() {
        Path confDir = FMLPaths.CONFIGDIR.get().resolve(MODID);
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::common);
        modBus.addListener(this::conf);
        modBus.addListener(this::attributes);
        modBus.addGenericListener(GlobalLootModifierSerializer.class, this::registry);
        if (FMLEnvironment.dist == Dist.CLIENT)
            ClientEvents.register();

        registries();

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(this::addReloadListener);
        forgeBus.register(new EntityEvents());
        forgeBus.register(new WorldEvents());

        for (Map.Entry<ForgeConfigSpec, ConfigHolder<?>> confs : ConfigHolder.configs.entrySet()) {
            ConfigHolder<?> loader = confs.getValue();
            ModLoadingContext.get().registerConfig(loader.configType(), confs.getKey(), loader.configName());
        }

        SpawnConfig.spawnConfig = new SpawnConfig(confDir);
    }

    public static void registries() {
        ModBlocks.BLOCKS.registerContent();
        ModBlocks.TILES.registerContent();
        ModItems.ITEMS.registerContent();
        ModContainer.CONTAINERS.registerContent();
        ModEntities.ENTITIES.registerContent();
        ModAttributes.ATTRIBUTES.registerContent();
        ModEffects.EFFECTS.registerContent();
        ModCrafting.RECIPESERIALIZER.registerContent();
        ModLootModifier.SERIALZER.registerContent();
        ModFeatures.FEATURES.registerContent();
        ModSpells.SPELLS.registerContent();
        ModStructures.STRUCTURES.registerContent();
        ModParticles.PARTICLES.registerContent();
        ModActivities.ACTIVITIES.registerContent();
    }

    public void registry(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        ModLootCondition.LOOTFUNCTION.registerContent();
        ModLootCondition.LOOTCONDITIONS.registerContent();
        ModStructures.STRUCTURESPROCESSORS.registerContent();
        ModCrafting.RECIPETYPE.registerContent();
    }

    public void common(FMLCommonSetupEvent event) {
        PacketHandler.register();
        event.enqueueWork(() -> {
            SpawnPlacements.register(ModEntities.gate.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);
            ModFeatures.registerConfiguredMineralFeatures();
        });
        this.tweakVanillaAttribute(Attributes.MAX_HEALTH, Double.MAX_VALUE);
        this.tweakVanillaAttribute(Attributes.ATTACK_DAMAGE, Double.MAX_VALUE);
    }

    public void attributes(EntityAttributeCreationEvent event) {
        ModEntities.registerAttributes((type, builder) -> event.put(type, builder.build()));
    }

    public void conf(ModConfigEvent event) {
        ConfigHolder<?> holder = ConfigHolder.configs.get(event.getConfig().getSpec());
        if (holder != null)
            holder.reloadConfig();
    }

    public void addReloadListener(AddReloadListenerEvent event) {
        DataPackHandler.reloadItemStats(event::addListener);
        DataPackHandler.reloadCropManager(event::addListener);
        DataPackHandler.reloadFoodManager(event::addListener);
    }

    private void tweakVanillaAttribute(Attribute attribute, double value) {
        if (attribute instanceof RangedAttribute) {
            ((AttributeAccessor) attribute).setMaxValue(value);
        }
    }
}
