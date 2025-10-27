package io.github.flemmli97.runecraftory.neoforge;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.specs.ConfigHolder;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryAttributes;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryEntities;
import io.github.flemmli97.runecraftory.neoforge.client.ClientEvents;
import io.github.flemmli97.runecraftory.neoforge.event.EntityEvents;
import io.github.flemmli97.runecraftory.neoforge.event.WorldEvents;
import io.github.flemmli97.runecraftory.neoforge.integration.top.TOP;
import io.github.flemmli97.runecraftory.neoforge.network.PacketHandler;
import io.github.flemmli97.runecraftory.neoforge.registry.RuneCraftoryFluidTypes;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
        RuneCraftory.commonInit();
        registries(modBus);
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

        IEventBus forgeBus = NeoForge.EVENT_BUS;
        forgeBus.addListener(this::addReloadListener);
        forgeBus.register(new EntityEvents());
        forgeBus.register(new WorldEvents());

        for (Map.Entry<IConfigSpec, ConfigHolder<?>> confs : ConfigHolder.CONFIGS.entrySet()) {
            ConfigHolder<?> loader = confs.getValue();
            container.registerConfig(loader.configType() == ConfigHolder.ConfigType.COMMON ? ModConfig.Type.COMMON : ModConfig.Type.CLIENT, confs.getKey(), loader.configName());
        }
    }

    public static void registries(IEventBus modBus) {
        RuneCraftoryFluidTypes.FLUID_TYPES.register(modBus);
    }

    public void common(FMLCommonSetupEvent event) {
        RuneCraftory.updateAttributeLimits();
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
            ext.insertRegistryAccess(event.getServerResources().getRegistryLookup());
            event.addListener(ext);
        });
    }
}
