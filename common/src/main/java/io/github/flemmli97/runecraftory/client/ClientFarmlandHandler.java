package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.common.world.farming.FarmlandDataContainer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientFarmlandHandler {

    public static final ClientFarmlandHandler INSTANCE = new ClientFarmlandHandler();

    private final Long2ObjectMap<FarmlandDataContainer> farmland = new Long2ObjectOpenHashMap<>();
    private final Long2ObjectMap<Set<FarmlandDataContainer>> farmlandChunks = new Long2ObjectOpenHashMap<>();

    private ClientFarmlandHandler() {
    }

    @Nullable
    public FarmlandDataContainer getData(BlockPos pos) {
        return this.farmland.get(pos.asLong());
    }

    public void updateChunk(long packedChunk, List<FarmlandDataContainer> list) {
        Set<FarmlandDataContainer> chunkMap = this.farmlandChunks.computeIfAbsent(packedChunk, key -> new HashSet<>());
        list.forEach(d -> {
            this.farmland.put(d.pos().asLong(), d);
            chunkMap.add(d);
        });
    }

    public void onChunkUnLoad(ChunkPos pos) {
        Set<FarmlandDataContainer> farms = this.farmlandChunks.get(pos.toLong());
        if (farms != null) {
            farms.forEach(d -> this.farmland.remove(d.pos().asLong()));
            this.farmlandChunks.remove(pos.toLong());
        }
    }

    public void onFarmBlockRemove(BlockPos pos) {
        FarmlandDataContainer cont = this.farmland.remove(pos.asLong());
        if (cont != null) {
            long packedChunk = new ChunkPos(pos).toLong();
            Set<FarmlandDataContainer> farms = this.farmlandChunks.get(packedChunk);
            if (farms != null) {
                farms.remove(cont);
                if (farms.isEmpty())
                    this.farmlandChunks.remove(packedChunk);
            }
        }
    }

    public void onDisconnect() {
        this.farmland.clear();
        this.farmlandChunks.clear();
    }
}
