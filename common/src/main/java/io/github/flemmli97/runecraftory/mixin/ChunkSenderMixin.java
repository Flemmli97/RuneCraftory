package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerChunkSender.class)
public abstract class ChunkSenderMixin {

    /**
     * When player starts tracking a chunk
     */
    @Inject(method = "sendChunk", at = @At("RETURN"))
    private static void onPlayerLoadChunk(ServerGamePacketListenerImpl packetListener, ServerLevel level, LevelChunk chunk, CallbackInfo ci) {
        FarmlandHandler.get(level.getServer()).sendChangesTo(packetListener.getPlayer(), chunk.getPos());
    }
}
