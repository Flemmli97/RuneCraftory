package io.github.flemmli97.runecraftory.fabric.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CropGrowEvent {

    public static final Event<GrowEvent> EVENT = EventFactory.createArrayBacked(GrowEvent.class, callbacks -> ((level, state, pos) -> {
        for (GrowEvent callback : callbacks) {
            if (!callback.canGrow(level, state, pos)) {
                return false;
            }
        }
        return true;
    }));

    public interface GrowEvent {

        boolean canGrow(Level level, BlockState state, BlockPos pos);
    }
}
