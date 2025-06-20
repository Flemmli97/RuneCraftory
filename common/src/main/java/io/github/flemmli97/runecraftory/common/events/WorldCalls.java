package io.github.flemmli97.runecraftory.common.events;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WorldCalls {

//    public static void addFeatures(BiConsumer<GenerationStep.Decoration, Holder<PlacedFeature>> cons, Biome.BiomeCategory category) {
//        switch (category) {
//            case NETHER -> {
//                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDNETHERHERBFEATURE);
//                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDNETHERMINERALFEATURES)
//                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
//            }
//            case THEEND -> {
//                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDENDHERBFEATURE);
//                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDMINERALFEATURES)
//                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
//            }
//            default -> {
//                cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, ModFeatures.PLACEDHERBFEATURE);
//                for (Holder<PlacedFeature> holder : ModFeatures.PLACEDMINERALFEATURES)
//                    cons.accept(GenerationStep.Decoration.VEGETAL_DECORATION, holder);
//            }
//        }
//    }

    public static void daily(Level level) {
        if (level instanceof ServerLevel serverLevel && level.dimension().equals(Level.OVERWORLD)) {
            WorldHandler.get(serverLevel.getServer()).update(serverLevel);
            FarmlandHandler.get(serverLevel.getServer()).tick(serverLevel);
        }
    }

    public static boolean disableVanillaCrop(LevelAccessor level, BlockState state, BlockPos pos) {
        if (state.getBlock() instanceof CropBlock crop) {
            CropProperties prop = DataPackHandler.INSTANCE.cropManager().get(crop.getCloneItemStack(level, pos, state).getItem());
            return prop != null;
        }
        return false;
    }
}
