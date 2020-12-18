package com.flemmli97.runecraftory;

import com.flemmli97.runecraftory.client.ClientEvents;
import com.flemmli97.runecraftory.client.ClientRegister;
import com.flemmli97.runecraftory.common.MobModule;
import com.flemmli97.runecraftory.common.capability.IPlayerCap;
import com.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import com.flemmli97.runecraftory.common.capability.PlayerCapNetwork;
import com.flemmli97.runecraftory.common.datapack.DataPackHandler;
import com.flemmli97.runecraftory.common.events.PlayerEvents;
import com.flemmli97.runecraftory.common.events.WorldEvents;
import com.flemmli97.runecraftory.common.registry.ModAttributes;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.registry.ModCrafting;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.flemmli97.runecraftory.common.registry.ModLootModifier;
import com.flemmli97.runecraftory.common.registry.ModPotions;
import com.flemmli97.runecraftory.network.PacketHandler;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Path;

@Mod(value = RuneCraftory.MODID)
public class RuneCraftory {

    public static final String MODID = "runecraftory";
    public static final Logger logger = LogManager.getLogger(RuneCraftory.MODID);

    public static ModuleConf conf;
    public static Path defaultConfPath;

    public RuneCraftory() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::common);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEvents::register);

        registries(modBus);

        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
        forgeBus.addListener(DataPackHandler::registerDataPackHandler);
        forgeBus.register(new PlayerEvents());
        forgeBus.register(new WorldEvents());

        Path confDir = FMLPaths.CONFIGDIR.get().resolve(MODID);
        File def = confDir.resolve("default").toFile();
        if (!def.exists())
            def.mkdirs();
        conf = new ModuleConf(new File(confDir.toFile(), "module.json"));
        if (conf.mobModule)
            new MobModule();
        if (conf.combatModule)
            ;//new CombatModule();
    }

    public static void registries(IEventBus modBus) {
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.TILES.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModAttributes.ATTRIBUTES.register(modBus);
        ModPotions.EFFECTS.register(modBus);
        ModCrafting.RECIPESERIALIZER.register(modBus);
        modBus.addGenericListener(IRecipeSerializer.class, ModCrafting::register);
        ModLootModifier.SERIALZER.register(modBus);
        modBus.addGenericListener(GlobalLootModifierSerializer.class, ModLootModifier::register);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        ClientRegister.registerRender();
    }

    public void common(FMLCommonSetupEvent event) {
        PacketHandler.register();
        event.enqueueWork(() -> {
            ModEntities.registerAttributes();
        });
        CapabilityManager.INSTANCE.register(IPlayerCap.class, new PlayerCapNetwork(), PlayerCapImpl::new);
    }
}
