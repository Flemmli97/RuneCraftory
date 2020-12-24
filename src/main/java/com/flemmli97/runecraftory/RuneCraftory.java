package com.flemmli97.runecraftory;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.client.ClientEvents;
import com.flemmli97.runecraftory.client.ClientRegister;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.IStaffCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import com.flemmli97.runecraftory.common.capability.StaffCapImpl;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.config.GeneralConfigSpec;
import com.flemmli97.runecraftory.common.config.MobConfig;
import com.flemmli97.runecraftory.common.config.SpawnConfig;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.entities.GateEntity;
import com.flemmli97.runecraftory.common.events.MobEvents;
import com.flemmli97.runecraftory.common.events.PlayerEvents;
import com.flemmli97.runecraftory.common.events.WorldEvents;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.runecraftory.common.registry.ModPotions;
import com.flemmli97.runecraftory.common.registry.ModSpells;
import com.flemmli97.runecraftory.network.PacketHandler;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Mod(value = RuneCraftory.MODID)
public class RuneCraftory {

    public static final String MODID = "runecraftory";
    public static final Logger logger = LogManager.getLogger(RuneCraftory.MODID);

    public static Path defaultConfPath;

    public static SpawnConfig spawnConfig;

    public static IForgeRegistry<Spell> spellRegistry;

    public RuneCraftory() {
        Path confDir = FMLPaths.CONFIGDIR.get().resolve(MODID);
        File def = confDir.resolve("default").toFile();
        if (!def.exists())
            def.mkdirs();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfigSpec.generalSpec, RuneCraftory.MODID+"/general.toml");
        spawnConfig = new SpawnConfig(FMLPaths.CONFIGDIR.get().resolve(RuneCraftory.MODID));
        MobConfig.MobConfigSpec.config.loadConfig();

        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::common);
        modBus.addListener(this::newReg);
        modBus.addListener(this::conf);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);

        registries(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(DataPackHandler::registerDataPackHandler);
        forgeBus.register(new MobEvents());
        forgeBus.register(new PlayerEvents());
        forgeBus.register(new WorldEvents());
    }

    public static void registries(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.TILES.register(modBus);
        ModContainer.CONTAINERS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModAttributes.ATTRIBUTES.register(modBus);
        ModPotions.EFFECTS.register(modBus);
        ModCrafting.RECIPESERIALIZER.register(modBus);
        modBus.addGenericListener(IRecipeSerializer.class, ModCrafting::register);
        ModLootModifier.SERIALZER.register(modBus);
        modBus.addGenericListener(GlobalLootModifierSerializer.class, ModLootModifier::register);
        ModSpells.SPELLS.register(modBus);
    }

    public void newReg(RegistryEvent.NewRegistry event) {
        spellRegistry = new RegistryBuilder<Spell>().setName(new ResourceLocation(RuneCraftory.MODID, "spell_registry"))
                .setType(Spell.class).setDefaultKey(new ResourceLocation(RuneCraftory.MODID, "empty_spell")).create();
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.registerRender(event);
    }

    public void common(FMLCommonSetupEvent event) {
        PacketHandler.register();
        event.enqueueWork(() -> {
            ModEntities.registerAttributes();
            EntitySpawnPlacementRegistry.register(ModEntities.gate.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GateEntity::canSpawnAt);
        });
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new CapabilityInsts.PlayerCapNetwork(), PlayerCapImpl::new);
        CapabilityManager.INSTANCE.register(IStaffCap.class, new CapabilityInsts.StaffCapNetwork(), StaffCapImpl::new);
    }

    public void conf(ModConfig.ModConfigEvent event) {
        if (event.getConfig().getSpec() == GeneralConfigSpec.generalSpec)
            GeneralConfig.load();
    }
}
