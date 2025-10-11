package io.github.flemmli97.runecraftory.common.events;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public class WorldCalls {

    public static void tick(Level level) {
        if (level instanceof ServerLevel serverLevel && level.dimension().equals(Level.OVERWORLD)) {
            RunecraftorySavedData.get(serverLevel.getServer()).tick(serverLevel);
            FarmlandHandler.get(serverLevel.getServer()).tick(serverLevel);
        }
    }

    public static boolean disableVanillaCrop(LevelAccessor level, BlockState state, BlockPos pos) {
        CropProperties prop = DataPackHandler.INSTANCE.cropManager().get(state.getBlock());
        return prop != null;
    }
}
