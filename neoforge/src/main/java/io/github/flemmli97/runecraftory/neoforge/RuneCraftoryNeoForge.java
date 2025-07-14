package io.github.flemmli97.runecraftory.neoforge;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.specs.ConfigHolder;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryActivities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryArmorMaterials;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttackActions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCrafting;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCreativeRuneCraftoryTabs;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryCriteria;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEffects;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFeatures;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFluids;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryLootRegistries;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMenuTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCBehaviour;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryNPCProfessions;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryParticles;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryPoiTypes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySpells;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryStructures;
import io.github.flemmli97.runecraftory.mixin.AttributeAccessor;
import io.github.flemmli97.runecraftory.neoforge.client.ClientEvents;
import io.github.flemmli97.runecraftory.neoforge.event.EntityEvents;
import io.github.flemmli97.runecraftory.neoforge.event.WorldEvents;
import io.github.flemmli97.runecraftory.neoforge.integration.top.TOP;
import io.github.flemmli97.runecraftory.neoforge.network.PacketHandler;
import io.github.flemmli97.runecraftory.neoforge.registry.ModAttachments;
import io.github.flemmli97.runecraftory.neoforge.registry.ModFluidTypes;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

import java.util.Map;

@Mod(value = RuneCraftory.MODID)
public class RuneCraftoryNeoForge {

    public RuneCraftoryNeoForge(IEventBus modBus, ModContainer container) {
        modBus.addListener(this::common);
        modBus.addListener(this::confLoad);
        modBus.addListener(this::confReload);
        modBus.addListener(this::attributes);
        modBus.addListener(this::attributesAdd);
        modBus.addListener(this::spawnPlacement);
        modBus.addListener(PacketHandler::register);
        modBus.addListener(TOP::enqueueIMC);
        if (FMLEnvironment.dist == Dist.CLIENT)
            ClientEvents.register(modBus);

        registries(modBus);

        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(this::addReloadListener);
        forgeBus.register(new EntityEvents());
        forgeBus.register(new WorldEvents());

        for (Map.Entry<IConfigSpec, ConfigHolder<?>> confs : ConfigHolder.CONFIGS.entrySet()) {
            ConfigHolder<?> loader = confs.getValue();
            container.registerConfig(loader.configType() == ConfigHolder.ConfigType.COMMON ? ModConfig.Type.COMMON : ModConfig.Type.CLIENT, confs.getKey(), loader.configName());
        }
        QuestHandler.register();
    }

    public static void registries(IEventBus modBus) {
        RuneCraftoryBlocks.BLOCKS.registerContent(modBus);
        RuneCraftoryBlocks.BLOCK_ENTITY_TYPES.registerContent(modBus);
        RuneCraftoryItems.ITEMS.registerContent(modBus);
        RuneCraftoryMenuTypes.CONTAINERS.registerContent(modBus);
        RuneCraftoryEntities.ENTITIES.registerContent(modBus);
        RuneCraftoryAttributes.ATTRIBUTES.registerContent(modBus);
        RuneCraftoryEffects.EFFECTS.registerContent(modBus);
        RuneCraftoryCrafting.RECIPESERIALIZER.registerContent(modBus);
        RuneCraftorySpells.SPELLS.register().registerContent(modBus);
        RuneCraftoryParticles.PARTICLES.registerContent(modBus);
        RuneCraftoryActivities.ACTIVITIES.registerContent(modBus);
        RuneCraftoryPoiTypes.POI.registerContent(modBus);
        RuneCraftoryArmorMaterials.MATERIALS.registerContent(modBus);
        RuneCraftoryMemoryTypes.MEMORYIES.registerContent(modBus);
        RuneCraftoryNPCBehaviour.BEHAVIOURS.register().registerContent(modBus);
        RuneCraftoryAttackActions.ATTACK_ACTIONS.register().registerContent(modBus);
        RuneCraftorySounds.SOUND_EVENTS.registerContent(modBus);
        RuneCraftoryArmorEffects.ARMOR_EFFECTS.register().registerContent(modBus);
        RuneCraftoryNPCLooks.NPC_FEATURES.register().registerContent(modBus);
        RuneCraftoryLootRegistries.LOOTFUNCTION.registerContent(modBus);
        RuneCraftoryLootRegistries.LOOTCONDITIONS.registerContent(modBus);
        RuneCraftoryLootRegistries.NUMBER_PROVIDERS.registerContent(modBus);
        RuneCraftoryCrafting.RECIPETYPE.registerContent(modBus);
        RuneCraftoryNPCProfessions.PROFESSIONS.register().registerContent(modBus);
        ModAttachments.ATTACHMENT_TYPES.register(modBus);
        RuneCraftoryDataComponentTypes.DATA_COMPONENTS.registerContent(modBus);
        RuneCraftoryCriteria.TRIGGERS.registerContent(modBus);
        RuneCraftoryCreativeRuneCraftoryTabs.CREATIVE_MODE_TABS.registerContent(modBus);
        RuneCraftoryStructures.STRUCTURE_PROCESSORS.registerContent(modBus);
        RuneCraftoryStructures.STRUCTURES.registerContent(modBus);
        RuneCraftoryFeatures.FEATURES.registerContent(modBus);
        RuneCraftoryFeatures.TRUNK_PLACER.registerContent(modBus);
        RuneCraftoryFeatures.TREE_DECORATORS.registerContent(modBus);
        RuneCraftoryFluids.FLUIDS.registerContent(modBus);
        ModFluidTypes.FLUID_TYPES.register(modBus);
    }

    public void common(FMLCommonSetupEvent event) {
        this.tweakVanillaAttribute(Attributes.MAX_HEALTH.value(), Double.MAX_VALUE);
        this.tweakVanillaAttribute(Attributes.ATTACK_DAMAGE.value(), Double.MAX_VALUE);
    }

    public void spawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(RuneCraftoryEntities.GATE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GateEntity::canSpawnAt, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public void attributes(EntityAttributeCreationEvent event) {
        RuneCraftoryEntities.registerAttributes((type, builder) -> event.put(type, builder.build()));
    }

    public void attributesAdd(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> t : event.getTypes()) {
            for (RegistryEntrySupplier<Attribute, ?> s : RuneCraftoryAttributes.ENTITY_ATTRIBUTES) {
                if (!event.has(t, s.asHolder()))
                    event.add(t, s.asHolder());
            }
        }
        for (RegistryEntrySupplier<Attribute, ?> s : RuneCraftoryAttributes.PLAYER_ATTRIBUTES) {
            event.add(EntityType.PLAYER, s.asHolder());
        }
    }

    public void confLoad(ModConfigEvent.Loading event) {
        ConfigHolder<?> holder = ConfigHolder.CONFIGS.get(event.getConfig().getSpec());
        if (holder != null)
            holder.reloadConfig();
    }

    public void confReload(ModConfigEvent.Reloading event) {
        ConfigHolder<?> holder = ConfigHolder.CONFIGS.get(event.getConfig().getSpec());
        if (holder != null)
            holder.reloadConfig();
    }

    public void addReloadListener(AddReloadListenerEvent event) {
        DataPackHandler.addListeners(ext -> {
            ext.insertRegistryAccess(event.getRegistryAccess());
            event.addListener(ext);
        });
    }

    private void tweakVanillaAttribute(Attribute attribute, double value) {
        if (attribute instanceof RangedAttribute) {
            ((AttributeAccessor) attribute).setMaxValue(value);
        }
    }
}
