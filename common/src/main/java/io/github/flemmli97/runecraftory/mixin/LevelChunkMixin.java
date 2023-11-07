package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.mixinhelper.LevelChunkTick;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelChunk.class)
public class LevelChunkMixin implements LevelChunkTick {

    @Unique
    private boolean runecraftory_doneFirstTick;


    @Override
    public boolean getAndSetFirstTick() {
        boolean first = this.runecraftory_doneFirstTick;
        this.runecraftory_doneFirstTick = true;
        return !first;
    }
}
