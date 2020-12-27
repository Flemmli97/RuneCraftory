package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import com.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import com.flemmli97.runecraftory.common.config.GenerationConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModFeatures;
import com.flemmli97.runecraftory.common.world.GateSpawning;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import com.flemmli97.runecraftory.common.world.features.ChancedBlockCluster;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Set;

public class WorldEvents {

    public static final ResourceLocation PlayerCap = new ResourceLocation(RuneCraftory.MODID, "player_cap");
    public static final ResourceLocation StaffCap = new ResourceLocation(RuneCraftory.MODID, "staff_cap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(PlayerCap, new PlayerCapImpl());
        }
    }

    @SubscribeEvent
    public void command(RegisterCommandsEvent event) {
        RunecraftoryCommand.reg(event.getDispatcher());
    }

    @SubscribeEvent
    public void biomeLoad(BiomeLoadingEvent event) {
        RuneCraftory.spawnConfig.getEntityFromBiome(event.getName()).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(RegistryKey.of(Registry.BIOME_KEY, event.getName()));
        for (BiomeDictionary.Type type : types)
            RuneCraftory.spawnConfig.getEntityFromBiomeType(type).forEach(entity -> GateSpawning.addSpawn(event.getName(), entity));
        event.getSpawns().spawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.gate.get(), 100, 1, 1));

        List<MineralGenConfig> mineralConf = GenerationConfig.mineralGenFrom(types);
        if (!mineralConf.isEmpty()) {
            for (MineralGenConfig conf : mineralConf)
                event.getGeneration().feature(GenerationStage.Decoration.SURFACE_STRUCTURES, ModFeatures.MINERALFEATURE.get()
                        .configure(new ChancedBlockCluster(new SimpleBlockStateProvider(conf.getBlock().getDefaultState()), conf.minAmount(), conf.maxAmount(), conf.xSpread(), conf.ySpread(), conf.zSpread(), conf.chance())).decorate(Features.Placements.SQUARE_TOP_SOLID_HEIGHTMAP));
        }
    }

    @SubscribeEvent
    public void daily(TickEvent.WorldTickEvent e) {
        if (e.phase == TickEvent.Phase.END && !e.world.isRemote && e.world.getRegistryKey().equals(World.OVERWORLD)) {
            WorldHandler.get((ServerWorld) e.world).update((ServerWorld) e.world);
        }
    }

    @SubscribeEvent
    public void disableVanillaCrop(BlockEvent.CropGrowEvent.Pre event) {
        if (event.getState().getBlock() instanceof IGrowable && event.getState().getBlock() instanceof IPlantable) {
            IPlantable growable = (IPlantable) event.getState().getBlock();
            if (growable.getPlantType(event.getWorld(), event.getPos()) == PlantType.CROP) {
                event.setResult(Event.Result.DENY);
            }
        }
    }
}
