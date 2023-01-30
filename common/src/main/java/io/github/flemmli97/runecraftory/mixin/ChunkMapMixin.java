package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
public abstract class ChunkMapMixin {

    /**
     * When player starts tracking a chunk
     */
    @Inject(method = "playerLoadedChunk", at = @At("HEAD"))
    private void onPlayerLoadChunk(ServerPlayer player, MutableObject<ClientboundLevelChunkWithLightPacket> packetCache, LevelChunk chunk, CallbackInfo info) {
        FarmlandHandler.get(player.getLevel().getServer()).sendChangesTo(player, chunk.getPos());
    }
}
