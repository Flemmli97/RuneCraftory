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
