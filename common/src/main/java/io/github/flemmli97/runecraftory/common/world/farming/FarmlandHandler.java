package io.github.flemmli97.runecraftory.common.world.farming;

import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * WIP
 */
public class FarmlandHandler extends SavedData {

    public static final boolean ENABLED = false;

    private static final String ID = "FarmlandData";

    private final Map<ResourceKey<Level>, Long2ObjectMap<FarmlandData>> farmland = new HashMap<>();
    private final Map<ResourceKey<Level>, Long2ObjectMap<Set<FarmlandData>>> farmlandChunks = new HashMap<>();

    public FarmlandHandler() {
    }

    private FarmlandHandler(CompoundTag tag) {
        this.load(tag);
    }

    public static FarmlandHandler get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(FarmlandHandler::new, FarmlandHandler::new, ID);
    }

    public static boolean isFarmBlock(BlockState state) {
        return state.is(ModTags.FARMLAND) && state.hasProperty(FarmBlock.MOISTURE);
    }

    public static void waterLand(ServerLevel level, BlockPos pos, BlockState state) {
        level.sendParticles(ParticleTypes.FISHING, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 4, 0.0, 0.01, 0.0, 0.1D);
        level.setBlock(pos, state.setValue(FarmBlock.MOISTURE, 7), 3);
        level.playSound(null, pos, SoundEvents.BOAT_PADDLE_WATER, SoundSource.BLOCKS, 1.0f, 1.1f);
        BlockPos up = pos.above();
        BlockState crop = level.getBlockState(up);
        if (crop.getBlock() instanceof BlockCrop && crop.getValue(BlockCrop.WILTED))
            level.setBlock(up, crop.setValue(BlockCrop.WILTED, false), Block.UPDATE_ALL);
    }

    public void onFarmlandPlace(ServerLevel level, BlockPos pos) {
        if (!ENABLED)
            return;
        FarmlandData data = this.farmland.computeIfAbsent(level.dimension(), old -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(pos.asLong(), old -> new FarmlandData(pos));
        this.farmlandChunks.computeIfAbsent(level.dimension(), old -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(new ChunkPos(pos).toLong(), old -> new HashSet<>())
                .add(data);
        data.updateFarmBlock(true);
        this.setDirty();
    }

    public void onFarmlandRemove(ServerLevel level, BlockPos pos) {
        if (!ENABLED)
            return;
        Long2ObjectMap<FarmlandData> farms = this.farmland.get(level.dimension());
        if (farms != null) {
            FarmlandData data = farms.get(pos.asLong());
            if (data != null) {
                data.updateFarmBlock(false);
                this.setDirty();
            }
        }
    }

    public void onChunkLoad(ServerLevel level, ChunkPos pos) {
        if (!ENABLED)
            return;
        Long2ObjectMap<Set<FarmlandData>> chunkFarms = this.farmlandChunks.get(level.dimension());
        if (chunkFarms != null) {
            Set<FarmlandData> farms = chunkFarms.get(pos.toLong());
            if (farms != null) {
                farms.forEach(d -> d.onLoad(level));
                this.setDirty();
            }
        }
    }

    public void onChunkUnLoad(ServerLevel level, ChunkPos pos) {
        if (!ENABLED)
            return;
        Long2ObjectMap<Set<FarmlandData>> chunkFarms = this.farmlandChunks.get(level.dimension());
        if (chunkFarms != null) {
            Set<FarmlandData> farms = chunkFarms.get(pos.toLong());
            if (farms != null) {
                farms.forEach(d -> d.onUnload(level));
                this.setDirty();
            }
        }
    }

    public Optional<FarmlandData> getData(ServerLevel level, BlockPos pos) {
        Long2ObjectMap<FarmlandData> farms = this.farmland.get(level.dimension());
        if (farms != null) {
            return Optional.ofNullable(farms.get(pos.asLong()));
        }
        return Optional.empty();
    }

    public void tick(ServerLevel level) {
        if (!ENABLED)
            return;
        this.farmland.forEach((dim, m) -> m.values().removeIf(d -> {
            d.tick(level, false);
            return d.shouldBeRemoved();
        }));
        this.setDirty();
    }

    public void load(CompoundTag compoundNBT) {
        compoundNBT.getAllKeys().forEach(levelKey -> {
            CompoundTag levelTag = compoundNBT.getCompound(levelKey);
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelKey));
            levelTag.getAllKeys().forEach(l -> {
                long packedPos = Long.parseLong(l);
                FarmlandData data = FarmlandData.fromTag(levelTag.getCompound(l), BlockPos.of(packedPos));
                this.addDataOnRead(key, data);
            });
        });
    }

    private void addDataOnRead(ResourceKey<Level> key, FarmlandData data) {
        this.farmland.computeIfAbsent(key, old -> new Long2ObjectOpenHashMap<>())
                .put(data.pos.asLong(), data);
        this.farmlandChunks.computeIfAbsent(key, old -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(new ChunkPos(data.pos).toLong(), old -> new HashSet<>())
                .add(data);
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        this.farmland.forEach((key, map) -> {
            CompoundTag levelTag = new CompoundTag();
            map.forEach((l, data) -> levelTag.put(l.toString(), data.save()));
            compoundTag.put(key.location().toString(), levelTag);
        });
        return compoundTag;
    }
}
