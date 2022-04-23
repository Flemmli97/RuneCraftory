package io.github.flemmli97.runecraftory.common.events;

import com.mojang.brigadier.CommandDispatcher;
import io.github.flemmli97.runecraftory.common.commands.RunecraftoryCommand;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModFeatures;
import io.github.flemmli97.runecraftory.common.world.GateSpawning;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiConsumer;

public class WorldCalls {

    public static void command(CommandDispatcher<CommandSourceStack> dispatcher) {
        RunecraftoryCommand.reg(dispatcher);
    }

    public static void addFeatures(BiConsumer<GenerationStep.Decoration, Holder<PlacedFeature>> cons, Biome.BiomeCategory category) {
        switch (category) {
            case NETHER -> {
                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDNETHERHERBFEATURE);
                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDNETHERMINERALFEATURES)
                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
            }
            case THEEND -> {
                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDENDHERBFEATURE);
                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDMINERALFEATURES)
                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
            }
            default -> {
                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDHERBFEATURE);
                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDMINERALFEATURES)
                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
            }
        }
    }

    public static void structureSpawns(StructureFeature<?> structure, BiConsumer<MobCategory, MobSpawnSettings.SpawnerData> consumer) {
        if (GateSpawning.structureShouldSpawn(structure))
            consumer.accept(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.gate.get(), 100, 1, 1));
    }

    public static void daily(Level level) {
        if (level instanceof ServerLevel serverLevel && level.dimension().equals(Level.OVERWORLD)) {
            WorldHandler.get(serverLevel.getServer()).update(serverLevel);
        }
    }

    public static boolean disableVanillaCrop(BlockState state, BlockPos pos) {
        return state.getBlock() instanceof CropBlock;
    }
}
