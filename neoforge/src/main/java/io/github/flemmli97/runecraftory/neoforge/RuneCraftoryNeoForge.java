package io.github.flemmli97.runecraftory.neoforge;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.specs.ConfigHolder;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.registry.ModActivities;
import io.github.flemmli97.runecraftory.common.registry.ModArmorEffects;
import io.github.flemmli97.runecraftory.common.registry.ModArmorMaterials;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.registry.ModCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModCreativeModTabs;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.registry.ModEffects;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.registry.ModLootRegistries;
import io.github.flemmli97.runecraftory.common.registry.ModMemoryTypes;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import io.github.flemmli97.runecraftory.common.registry.ModNPCBehaviour;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import io.github.flemmli97.runecraftory.common.registry.ModNPCLooks;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import io.github.flemmli97.runecraftory.common.registry.ModPoiTypes;
import io.github.flemmli97.runecraftory.common.registry.ModSounds;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import io.github.flemmli97.runecraftory.common.registry.ModStructures;
import io.github.flemmli97.runecraftory.mixin.AttributeAccessor;
import io.github.flemmli97.runecraftory.neoforge.client.ClientEvents;
import io.github.flemmli97.runecraftory.neoforge.event.EntityEvents;
import io.github.flemmli97.runecraftory.neoforge.event.WorldEvents;
import io.github.flemmli97.runecraftory.neoforge.integration.top.TOP;
import io.github.flemmli97.runecraftory.neoforge.network.PacketHandler;
import io.github.flemmli97.runecraftory.neoforge.registry.ModAttachments;
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
import net.neoforged.fml.ModList;
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
        RuneCraftory.iris = ModList.get().isLoaded("iris");

        modBus.addListener(this::common);
        modBus.addListener(this::conf);
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
        ModBlocks.BLOCKS.registerContent(modBus);
        ModBlocks.BLOCK_ENTITY_TYPES.registerContent(modBus);
        ModItems.ITEMS.registerContent(modBus);
        ModMenuTypes.CONTAINERS.registerContent(modBus);
        ModEntities.ENTITIES.registerContent(modBus);
        ModAttributes.ATTRIBUTES.registerContent(modBus);
        ModEffects.EFFECTS.registerContent(modBus);
        ModCrafting.RECIPESERIALIZER.registerContent(modBus);
        ModSpells.SPELLS.register().registerContent(modBus);
        ModParticles.PARTICLES.registerContent(modBus);
        ModActivities.ACTIVITIES.registerContent(modBus);
        ModPoiTypes.POI.registerContent(modBus);
        ModArmorMaterials.MATERIALS.registerContent(modBus);
        ModMemoryTypes.MEMORYIES.registerContent(modBus);
        ModNPCBehaviour.BEHAVIOURS.register().registerContent(modBus);
        ModAttackActions.ATTACK_ACTIONS.register().registerContent(modBus);
        ModSounds.SOUND_EVENTS.registerContent(modBus);
        ModArmorEffects.ARMOR_EFFECTS.register().registerContent(modBus);
        ModNPCLooks.NPC_FEATURES.register().registerContent(modBus);
        ModLootRegistries.LOOTFUNCTION.registerContent(modBus);
        ModLootRegistries.LOOTCONDITIONS.registerContent(modBus);
        ModLootRegistries.NUMBER_PROVIDERS.registerContent(modBus);
        ModCrafting.RECIPETYPE.registerContent(modBus);
        ModNPCJobs.JOBS.register().registerContent(modBus);
        ModAttachments.ATTACHMENT_TYPES.register(modBus);
        ModDataComponentTypes.DATA_COMPONENTS.registerContent(modBus);
        ModCriteria.TRIGGERS.registerContent(modBus);
        ModCreativeModTabs.CREATIVE_MODE_TABS.registerContent(modBus);
        ModStructures.STRUCTURE_PROCESSORS.registerContent(modBus);
        ModStructures.STRUCTURES.registerContent(modBus);
        ModFeatures.FEATURES.registerContent(modBus);
        ModFeatures.TRUNK_PLACER.registerContent(modBus);
        ModFeatures.TREE_DECORATORS.registerContent(modBus);
    }

    public void common(FMLCommonSetupEvent event) {
        this.tweakVanillaAttribute(Attributes.MAX_HEALTH.value(), Double.MAX_VALUE);
        this.tweakVanillaAttribute(Attributes.ATTACK_DAMAGE.value(), Double.MAX_VALUE);
    }

    public void spawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.GATE.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                GateEntity::canSpawnAt, RegisterSpawnPlacementsEvent.Operation.AND);
    }

    public void attributes(EntityAttributeCreationEvent event) {
        ModEntities.registerAttributes((type, builder) -> event.put(type, builder.build()));
    }

    public void attributesAdd(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> t : event.getTypes()) {
            for (RegistryEntrySupplier<Attribute, ?> s : ModAttributes.ENTITY_ATTRIBUTES) {
                if (!event.has(t, s.asHolder()))
                    event.add(t, s.asHolder());
            }
        }
        for (RegistryEntrySupplier<Attribute, ?> s : ModAttributes.PLAYER_ATTRIBUTES) {
            event.add(EntityType.PLAYER, s.asHolder());
        }
    }

    public void conf(ModConfigEvent event) {
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
