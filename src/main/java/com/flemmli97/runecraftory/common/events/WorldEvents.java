package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.capability.EntityCapImpl;
import com.flemmli97.runecraftory.common.capability.PlayerCapImpl;
import com.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import com.flemmli97.runecraftory.common.config.GenerationConfig;
import com.flemmli97.runecraftory.common.config.values.HerbGenConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import com.flemmli97.runecraftory.common.registry.ModEntities;
import com.flemmli97.runecraftory.common.registry.ModFeatures;
import com.flemmli97.runecraftory.common.registry.ModStructures;
import com.flemmli97.runecraftory.common.world.GateSpawning;
import com.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.WeightedBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorldEvents {

    public static final ResourceLocation PlayerCap = new ResourceLocation(RuneCraftory.MODID, "player_cap");
    public static final ResourceLocation StaffCap = new ResourceLocation(RuneCraftory.MODID, "staff_cap");
    public static final ResourceLocation EntityCap = new ResourceLocation(RuneCraftory.MODID, "entity_cap");

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(PlayerCap, new PlayerCapImpl());
        }
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(EntityCap, new EntityCapImpl());
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
        for (MineralGenConfig conf : mineralConf) {
            if (types.contains(BiomeDictionary.Type.NETHER))
                event.getGeneration().feature(GenerationStage.Decoration.VEGETAL_DECORATION, conf.configuredFeature());
            else
                event.getGeneration().feature(GenerationStage.Decoration.VEGETAL_DECORATION, conf.configuredFeatureNether());
        }
        List<HerbGenConfig> herbConf = GenerationConfig.herbGenFrom(types);
        if (!herbConf.isEmpty()) {
            WeightedBlockStateProvider provider = new WeightedBlockStateProvider();
            for (HerbGenConfig conf : herbConf)
                provider.addState(conf.getBlock().getDefaultState(), conf.weight());
            if (types.contains(BiomeDictionary.Type.NETHER))
                event.getGeneration().feature(GenerationStage.Decoration.VEGETAL_DECORATION,
                        Feature.FLOWER.configure((new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.INSTANCE)).tries(GenerationConfig.netherHerbTries).build()).applyChance(GenerationConfig.netherHerbChance).decorate(Placement.COUNT_MULTILAYER.configure(new FeatureSpreadConfig(6))));
            else if (types.contains(BiomeDictionary.Type.END))
                event.getGeneration().feature(GenerationStage.Decoration.VEGETAL_DECORATION,
                        Feature.FLOWER.configure((new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.INSTANCE)).tries(GenerationConfig.endHerbTries).build()).applyChance(GenerationConfig.endHerbChance).decorate(Features.Placements.SQUARE_HEIGHTMAP));
            else
                event.getGeneration().feature(GenerationStage.Decoration.VEGETAL_DECORATION,
                        Feature.FLOWER.configure((new BlockClusterFeatureConfig.Builder(provider, SimpleBlockPlacer.INSTANCE)).tries(GenerationConfig.overworldHerbTries).build()).applyChance(GenerationConfig.overworldHerbChance).decorate(Features.Placements.SQUARE_HEIGHTMAP));
        }
        if (types.contains(BiomeDictionary.Type.FOREST) && types.contains(BiomeDictionary.Type.OVERWORLD))
            event.getGeneration().getStructures().add(() -> ModFeatures.AMBROSIA_FEATURE);
        if (types.contains(BiomeDictionary.Type.OCEAN) && types.contains(BiomeDictionary.Type.OVERWORLD))
            event.getGeneration().getStructures().add(() -> ModFeatures.THUNDERBOLT_FEATURE);
    }

    @SubscribeEvent
    public void worldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.getRegistryKey().equals(World.OVERWORLD)) {
                return;
            }
            Map<Structure<?>, StructureSeparationSettings> map = serverWorld.getChunkProvider().generator.getStructuresConfig().getStructures();
            map.put(ModStructures.AMBROSIA_FOREST.get(), new StructureSeparationSettings(25, 15, 34645653));
            map.put(ModStructures.THUNDERBOLT_RUINS.get(), new StructureSeparationSettings(40, 32, 34645653));
        }
    }

    @SubscribeEvent
    public void structureSpawns(StructureSpawnListGatherEvent event) {
        if (GateSpawning.structureShouldSpawn(event.getStructure()))
            event.addEntitySpawn(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(ModEntities.gate.get(), 100, 1, 1));
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
