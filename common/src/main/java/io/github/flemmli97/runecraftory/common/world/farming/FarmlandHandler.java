package io.github.flemmli97.runecraftory.common.world.farming;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumWeather;
import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import io.github.flemmli97.runecraftory.common.blocks.BlockGiantCrop;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.config.MobConfig;
import io.github.flemmli97.runecraftory.common.network.S2CFarmlandRemovePacket;
import io.github.flemmli97.runecraftory.common.network.S2CFarmlandUpdatePacket;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Handler for farmland.
 * Makes "farmland" not block bound and dependent on if the chunk is loaded or not
 */
public class FarmlandHandler extends SavedData {

    private static final String ID = "FarmlandData";
    private static final int CHUNK_SECTION_SIZE = 16 * 16 * 16;

    private final Map<ResourceKey<Level>, Long2ObjectMap<FarmlandData>> farmland = new HashMap<>();
    private final Map<ResourceKey<Level>, Long2ObjectMap<Set<FarmlandData>>> farmlandChunks = new HashMap<>();

    private final Map<ResourceKey<Level>, Long2ObjectMap<Set<FarmlandData>>> scheduledUpdates = new HashMap<>();
    private final Map<ResourceKey<Level>, Long2ObjectMap<Set<BlockPos>>> scheduledRemoveUpdates = new HashMap<>();
    private final Map<ResourceKey<Level>, Set<PendingGiantCrops>> pendingGiantGrowth = new HashMap<>();

    private final Map<ResourceKey<Level>, Map<UUID, IrrigationPOI>> irrigationPOI = new HashMap<>();

    private int lastUpdateDay;

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
        if (crop.getBlock() instanceof BlockCrop blockCrop && crop.getValue(BlockCrop.WILTED))
            blockCrop.onWiltedWatering(level, up, crop);
    }

    public static boolean canRainingAt(Level level, BlockPos position) {
        if (!level.canSeeSky(position)) {
            return false;
        }
        if (level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, position).getY() > position.getY()) {
            return false;
        }
        Biome biome = level.getBiome(position).value();
        return biome.getPrecipitation() == Biome.Precipitation.RAIN && biome.warmEnoughToRain(position);
    }

    public static void sendChangesTo(ServerLevel level, ChunkPos pos, List<FarmlandData> data) {
        Platform.INSTANCE.sendToTracking(new S2CFarmlandUpdatePacket(pos.toLong(), data), level, pos);
    }

    public static void onFarmRemoveChange(ServerLevel level, ChunkPos pos, List<BlockPos> data) {
        Platform.INSTANCE.sendToTracking(new S2CFarmlandRemovePacket(pos.toLong(), data), level, pos);
    }

    public static void unloadChunk(ServerLevel level, ChunkPos pos) {
        Platform.INSTANCE.sendToTracking(new S2CFarmlandRemovePacket(pos.toLong()), level, pos);
    }

    /**
     * {@link FarmBlock#isNearWater}
     */
    public static boolean isNearWater(LevelReader level, BlockPos pos) {
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
            if (!level.getFluidState(blockPos).is(FluidTags.WATER)) continue;
            return true;
        }
        return false;
    }

    public void onFarmlandPlace(ServerLevel level, BlockPos pos) {
        FarmlandData data = this.farmland.computeIfAbsent(level.dimension(), old -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(pos.asLong(), old -> new FarmlandData(pos));
        this.farmlandChunks.computeIfAbsent(level.dimension(), old -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(new ChunkPos(pos).toLong(), old -> new HashSet<>())
                .add(data);
        data.updateFarmBlock(true);
        data.onLoad(level, false);
        this.scheduleUpdate(level, data);
        this.setDirty();
    }

    public void onFarmlandRemove(ServerLevel level, BlockPos pos) {
        Long2ObjectMap<FarmlandData> farms = this.farmland.get(level.dimension());
        if (farms != null) {
            FarmlandData data = farms.get(pos.asLong());
            if (data != null) {
                data.updateFarmBlock(false);
                this.scheduleRemoveUpdate(level, data);
                if (data.shouldBeRemoved()) {
                    farms.remove(pos.asLong());
                    this.farmlandChunks.get(level.dimension())
                            .get(new ChunkPos(pos).toLong())
                            .remove(data);
                }
                this.setDirty();
            }
        }
    }

    public void onChunkLoad(ServerLevel level, ChunkPos pos) {
        Long2ObjectMap<Set<FarmlandData>> chunkFarms = this.farmlandChunks.get(level.dimension());
        if (chunkFarms != null) {
            Set<FarmlandData> farms = chunkFarms.get(pos.toLong());
            if (farms != null) {
                farms.forEach(d -> {
                    d.onLoad(level, true);
                    this.scheduleUpdate(level, d);
                });
                this.setDirty();
            }
        }
    }

    public void onChunkUnLoad(ServerLevel level, ChunkPos pos) {
        Long2ObjectMap<Set<FarmlandData>> chunkFarms = this.farmlandChunks.get(level.dimension());
        if (chunkFarms != null) {
            Set<FarmlandData> farms = chunkFarms.get(pos.toLong());
            if (farms != null) {
                farms.forEach(d -> d.onUnload(level));
                this.setDirty();
            }
            unloadChunk(level, pos);
        }
    }

    public Optional<FarmlandData> getData(ServerLevel level, BlockPos pos) {
        Long2ObjectMap<FarmlandData> farms = this.farmland.get(level.dimension());
        if (farms != null) {
            return Optional.ofNullable(farms.get(pos.asLong()));
        }
        return Optional.empty();
    }

    public void scheduleUpdate(ServerLevel level, FarmlandData data) {
        this.scheduledUpdates.computeIfAbsent(level.dimension(), key -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(new ChunkPos(data.pos).toLong(), key -> new HashSet<>())
                .add(data);
    }

    public void scheduleRemoveUpdate(ServerLevel level, FarmlandData data) {
        this.scheduledRemoveUpdates.computeIfAbsent(level.dimension(), key -> new Long2ObjectOpenHashMap<>())
                .computeIfAbsent(new ChunkPos(data.pos).toLong(), key -> new HashSet<>())
                .add(data.pos);
    }

    public void scheduleGiantCropMerge(ServerLevel level, BlockPos pos, BlockState state) {
        Set<PendingGiantCrops> set = this.pendingGiantGrowth.computeIfAbsent(level.dimension(), key -> new HashSet<>());
        List<PendingGiantCrops> overlap = new ArrayList<>();
        for (PendingGiantCrops pending : set) {
            if (pending.contains(pos)) {
                overlap.add(pending);
            }
            if (overlap.size() > 1) {
                overlap.get(0).add(pos, state);
                overlap.get(0).combine(overlap.get(1));
                break;
            }
        }
        if (overlap.size() > 0) {
            if (overlap.size() == 1)
                overlap.get(0).add(pos, state);
            else
                set.remove(overlap.get(1));
            return;
        }
        PendingGiantCrops newData = new PendingGiantCrops();
        newData.add(pos, state);
        set.add(newData);
    }

    public void sendChangesTo(ServerPlayer player, ChunkPos pos) {
        Long2ObjectMap<Set<FarmlandData>> chunkFarms = this.farmlandChunks.get(player.getLevel().dimension());
        if (chunkFarms != null) {
            Set<FarmlandData> farms = chunkFarms.get(pos.toLong());
            if (farms != null) {
                Platform.INSTANCE.sendToTracking(new S2CFarmlandUpdatePacket(pos.toLong(), new ArrayList<>(farms)), player.getLevel(), pos);
            }
        }
    }

    public void addIrrigationPOI(ServerLevel level, UUID id, BlockPos pos) {
        IrrigationPOI poi = new MonsterCropIrrigation(level.getGameTime(), pos);
        this.irrigationPOI.computeIfAbsent(level.dimension(), old -> new HashMap<>())
                .put(id, poi);
        this.setDirty();
    }

    public void removeIrrigationPOI(ServerLevel level, UUID id) {
        Map<UUID, IrrigationPOI> map = this.irrigationPOI.get(level.dimension());
        if (map != null) {
            map.remove(id);
            this.setDirty();
        }
    }

    public boolean hasWater(ServerLevel level, BlockPos pos) {
        if (!GeneralConfig.unloadedFarmlandCheckWater || (!GeneralConfig.disableFarmlandRandomtick && FarmlandHandler.isNearWater(level, pos)))
            return true;
        Map<UUID, IrrigationPOI> map = this.irrigationPOI.get(level.dimension());
        if (map != null) {
            for (IrrigationPOI poi : map.values()) {
                if (level.getGameTime() - poi.getStartTime() > 1200 && poi.isInside(pos))
                    return true;
            }
        }
        return false;
    }

    public void tick(ServerLevel level) {
        if (WorldUtils.canUpdateDaily(level, this.lastUpdateDay)) {
            ArrayList<ResourceKey<Level>> empty = new ArrayList<>();
            this.farmlandChunks.forEach((dim, m) -> {
                ServerLevel actualLevel = level.dimension().equals(dim) ? level : level.getServer().getLevel(dim);
                ArrayList<FarmlandData> removed = new ArrayList<>();
                m.values().removeIf(set -> {
                    if (set == null)
                        return true;
                    set.removeIf(d -> {
                        d.tick(actualLevel, false);
                        boolean remove = d.shouldBeRemoved();
                        if (remove)
                            removed.add(d);
                        return remove;
                    });
                    return set.isEmpty();
                });
                if (m.isEmpty())
                    empty.add(dim);
                else
                    removed.forEach(d -> {
                        Long2ObjectMap<FarmlandData> land = this.farmland.get(dim);
                        if (land != null)
                            land.remove(d.pos.asLong());
                    });
            });
            empty.forEach(dim -> {
                this.farmland.remove(dim);
                this.farmlandChunks.remove(dim);
            });
            this.lastUpdateDay = WorldUtils.day(level);
        }
        this.farmlandChunks.forEach((dim, m) -> {
            ServerLevel actualLevel = level.dimension().equals(dim) ? level : level.getServer().getLevel(dim);
            if (actualLevel != null)
                this.randomTick(actualLevel, m);
        });
        this.scheduledUpdates.forEach((dim, m) -> {
            ServerLevel actualLevel = level.dimension().equals(dim) ? level : level.getServer().getLevel(dim);
            if (actualLevel != null)
                m.forEach((l, data) -> sendChangesTo(actualLevel, new ChunkPos(l), new ArrayList<>(data)));
        });
        this.scheduledUpdates.clear();
        this.scheduledRemoveUpdates.forEach((dim, m) -> {
            ServerLevel actualLevel = level.dimension().equals(dim) ? level : level.getServer().getLevel(dim);
            if (actualLevel != null)
                m.forEach((l, data) -> onFarmRemoveChange(actualLevel, new ChunkPos(l), new ArrayList<>(data)));
        });
        this.scheduledRemoveUpdates.clear();
        this.pendingGiantGrowth.forEach((dim, m) -> {
            ServerLevel actualLevel = level.dimension().equals(dim) ? level : level.getServer().getLevel(dim);
            if (actualLevel != null)
                m.forEach(data -> data.tryMerge(actualLevel));
        });
        this.pendingGiantGrowth.clear();
        this.setDirty();
    }

    private void randomTick(ServerLevel level, Long2ObjectMap<Set<FarmlandData>> m) {
        EnumWeather weather = WorldHandler.get(level.getServer()).currentWeather();
        Consumer<FarmlandData> cons = null;
        int day = WorldUtils.day(level);
        if (weather == EnumWeather.STORM)
            cons = d -> d.onStorming(level, day);
        else if (level.isRaining())
            cons = d -> d.onWatering(level, day);
        if (cons == null)
            return;
        int randomTickSpeed = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        Consumer<FarmlandData> finalCons = cons;
        m.forEach((packedChunk, data) -> {
            if (data != null) {
                List<FarmlandData> list = new ArrayList<>(data);
                if (!list.isEmpty()) {
                    int size = list.size();
                    int chance = Mth.ceil(CHUNK_SECTION_SIZE / (float) size);
                    for (int l = 0; l < randomTickSpeed; ++l) {
                        if (level.random.nextInt(chance) == 0)
                            finalCons.accept(list.get(level.random.nextInt(size)));
                    }
                }
            }
        });
    }

    public void load(CompoundTag compoundTag) {
        this.lastUpdateDay = compoundTag.getInt("LastUpdateDay");
        CompoundTag farmTag = compoundTag.getCompound("Farms");
        farmTag.getAllKeys().forEach(levelKey -> {
            CompoundTag levelTag = farmTag.getCompound(levelKey);
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelKey));
            levelTag.getAllKeys().forEach(l -> {
                long packedPos = Long.parseLong(l);
                FarmlandData data = FarmlandData.fromTag(levelTag.getCompound(l), BlockPos.of(packedPos));
                this.addDataOnRead(key, data);
            });
        });

        CompoundTag irrigationTag = compoundTag.getCompound("Irrigation");
        irrigationTag.getAllKeys().forEach(levelKey -> {
            CompoundTag t = irrigationTag.getCompound(levelKey);
            ResourceKey<Level> key = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelKey));
            t.getAllKeys().forEach(uuidKey -> {
                UUID uuid = UUID.fromString(uuidKey);
                this.irrigationPOI.computeIfAbsent(key, old -> new HashMap<>())
                        .put(uuid, IrrigationPOI.load(t.getCompound(uuidKey)));
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
        compoundTag.putInt("LastUpdateDay", this.lastUpdateDay);
        CompoundTag farmTag = new CompoundTag();
        this.farmland.forEach((key, map) -> {
            CompoundTag levelTag = new CompoundTag();
            map.forEach((l, data) -> levelTag.put(l.toString(), data.save()));
            farmTag.put(key.location().toString(), levelTag);
        });
        compoundTag.put("Farms", farmTag);
        CompoundTag irrigationTag = new CompoundTag();
        this.irrigationPOI.forEach((key, map) -> {
            CompoundTag t = new CompoundTag();
            map.forEach((uuid, poi) -> t.put(uuid.toString(), poi.save()));
            farmTag.put(key.location().toString(), t);
        });
        compoundTag.put("Irrigation", irrigationTag);
        return compoundTag;
    }

    public interface IrrigationPOI {

        /**
         * Something like this is fine
         */
        static IrrigationPOI load(CompoundTag tag) {
            String id = tag.getString("ID");
            if (id.equals(MonsterCropIrrigation.ID))
                return new MonsterCropIrrigation(tag);
            throw new IllegalStateException("Couldn't parse!");
        }

        long getStartTime();

        boolean isInside(BlockPos pos);

        CompoundTag save();
    }

    public record MonsterCropIrrigation(long startTime, BlockPos pos) implements IrrigationPOI {

        public static final String ID = "MonsterIrrigation";

        public MonsterCropIrrigation(CompoundTag tag) {
            this(tag.getLong("Start"), BlockPos.CODEC.parse(NbtOps.INSTANCE, tag.getCompound("Pos")).getOrThrow(false, RuneCraftory.logger::error));
        }

        @Override
        public long getStartTime() {
            return this.startTime;
        }

        @Override
        public boolean isInside(BlockPos pos) {
            int radius = MobConfig.farmRadius;
            int dX = this.pos.getX() - pos.getX();
            int dY = this.pos.getY() - pos.getY();
            int dZ = this.pos.getZ() - pos.getZ();
            return Math.abs(dY) < 2 && Math.abs(dX) < radius && Math.abs(dZ) < radius;
        }

        @Override
        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.putString("ID", ID);
            tag.putLong("Start", this.getStartTime());
            tag.put("Pos", BlockPos.CODEC.encodeStart(NbtOps.INSTANCE, this.pos).getOrThrow(false, RuneCraftory.logger::error));
            return tag;
        }
    }

    /**
     * This handles merging crops into giant crops.
     * Ensures consistency
     */
    public static class PendingGiantCrops {

        private static final PositionDirection[] OFFSETS = new PositionDirection[]{
                new PositionDirection(new BlockPos(1, 0, 0), Direction.WEST),
                new PositionDirection(new BlockPos(1, 0, 1), Direction.NORTH),
                new PositionDirection(new BlockPos(0, 0, 1), Direction.EAST)
        };

        private AABB inner, outer;
        private final Long2ObjectMap<BlockState> crops = new Long2ObjectOpenHashMap<>();

        public boolean contains(BlockPos pos) {
            return this.outer != null && this.outer.contains(pos.getX(), pos.getY(), pos.getZ());
        }

        public void add(BlockPos pos, BlockState state) {
            this.crops.put(pos.asLong(), state);
            if (this.inner == null) {
                this.inner = new AABB(pos);
            } else {
                this.inner = this.inner.minmax(new AABB(pos));
            }
            this.outer = this.inner.inflate(1, 0, 1);
        }

        public void combine(PendingGiantCrops other) {
            this.inner = this.inner.minmax(other.inner);
            this.outer = this.inner.inflate(1, 0, 1);
            this.crops.putAll(other.crops);
        }

        public void tryMerge(ServerLevel level) {
            int minX = Mth.floor(this.inner.minX);
            int minZ = Mth.floor(this.inner.minZ);
            int y = Mth.floor(this.inner.minY);
            int maxX = Mth.floor(this.inner.maxX - 1);
            int maxZ = Mth.floor(this.inner.maxZ - 1);
            for (int z = minZ; z <= maxZ; z++) {
                for (int x = minX; x <= maxX; x++) {
                    BlockState s = this.crops.get(BlockPos.asLong(x, y, z));
                    if (s != null) {
                        BlockPos p = new BlockPos(x, y, z);
                        List<Pair<BlockPos, BlockState>> list = new ArrayList<>();
                        if (s.getBlock() instanceof BlockGiantCrop)
                            s = s.setValue(BlockGiantCrop.DIRECTION, Direction.SOUTH);
                        for (PositionDirection offset : OFFSETS) {
                            BlockPos newPos = p.offset(offset.pos());
                            BlockState s2 = this.crops.get(newPos.asLong());
                            if (s2 != null && s2.is(s.getBlock())) {
                                if (s2.getBlock() instanceof BlockGiantCrop)
                                    s2 = s2.setValue(BlockGiantCrop.DIRECTION, offset.direction());
                                list.add(Pair.of(newPos, s2));
                            }
                        }
                        list.add(Pair.of(p, s));
                        if (list.size() == 4)
                            for (Pair<BlockPos, BlockState> pair : list) {
                                this.crops.remove(pair.getFirst().asLong());
                                level.setBlock(pair.getFirst(), pair.getSecond(), Block.UPDATE_ALL);
                            }
                    }
                }
            }
        }
    }

    record PositionDirection(BlockPos pos, Direction direction) {
    }
}
