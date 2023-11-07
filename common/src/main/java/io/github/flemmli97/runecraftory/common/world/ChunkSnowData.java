package io.github.flemmli97.runecraftory.common.world;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.blocks.BlockMeltableSnow;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes it possible for snow to be the same over the world.
 * Aka even unloaded chunks will have snow.
 * This only applies {@link BlockMeltableSnow} though and NOT vanilla snow
 * in regards to seasonal snow.
 */
public class ChunkSnowData extends SavedData {

    private static final String id = "RCSnowData";

    //The saved rain values for the chunk when the chunk unloaded
    private final Long2ObjectMap<ChunkWeatherData> unloadedRainValues = new Long2ObjectOpenHashMap<>();
    //Keep track of the amount of snowblocks in the chunk -> chunksection -> blockpos as long
    private final Long2ObjectMap<Int2ObjectMap<LongSet>> hasSnow = new Long2ObjectOpenHashMap<>();
    //The global rain value
    private long globalRainValue, lastMonthRainValue;
    private long monthTime, lastMonthTime;

    public ChunkSnowData() {
    }

    private ChunkSnowData(CompoundTag tag) {
        this.load(tag);
    }

    public static ChunkSnowData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(ChunkSnowData::new, ChunkSnowData::new, id);
    }

    public void onChunkLoad(ServerLevel level, ChunkAccess chunk) {
        if (GeneralConfig.seasonedSnow)
            return;
        this.chunkLoadHandler(level, chunk);
    }

    private void chunkLoadHandler(ServerLevel level, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        ChunkWeatherData data = this.unloadedRainValues.get(chunkPos.toLong());
        long lastMonthRain = 0;
        long rainDiff = this.globalRainValue;
        long lastMonthTicks = 0;
        long thisMonthTicks = level.getDayTime() - this.monthTime;
        if (data.serverTime <= this.lastMonthTime) {
            //Save time was older than last month
            lastMonthRain = this.lastMonthRainValue;
            lastMonthTicks = this.monthTime - this.lastMonthTime;
        } else if (data.serverTime <= this.monthTime) {
            //Chunk was loaded in the last month
            lastMonthRain = this.lastMonthRainValue - data.rainValue;
            lastMonthTicks = data.serverTime - this.lastMonthTime;
        } else if (data.serverTime > this.monthTime) {
            //Chunk was loaded this month
            rainDiff = this.globalRainValue - data.rainValue;
            thisMonthTicks = data.serverTime - this.monthTime;
        }
        lastMonthRain = Math.min(24000, lastMonthRain);
        rainDiff = Math.min(24000, rainDiff);
        lastMonthTicks = Math.min(24000, lastMonthTicks);
        thisMonthTicks = Math.min(24000, thisMonthTicks);
        EnumSeason season = WorldHandler.get(level.getServer()).currentSeason();
        EnumSeason lastSeason = EnumSeason.values()[(season.ordinal() - 1) % 4];

        int minX = chunk.getPos().getMinBlockX();
        int minZ = chunk.getPos().getMinBlockZ();
        //Precheck if chunk contains applicable biomes
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(minX, 0, minZ);
        List<LevelChunkSection> meltableNow = new ArrayList<>();
        List<LevelChunkSection> meltablePast = new ArrayList<>();
        int coldEnoughNow = -1;
        boolean coldEnoughPast = false;
        Int2ObjectMap<LongSet> snowBlockSectionMap = this.hasSnow.get(chunkPos.toLong());
        for (int i = chunk.getSections().length - 1; i >= 0; --i) {
            LevelChunkSection section = chunk.getSection(i);
            //Ignore empty sections or sections without snowblocks
            if (section.hasOnlyAir() || (coldEnoughNow != -1 && !snowBlockSectionMap.containsKey(i)))
                continue;
            BiomeCounter counter = new BiomeCounter(pos, lastSeason, season, coldEnoughNow == -1);
            section.getBiomes().count(counter);
            if (coldEnoughNow == -1) {
                coldEnoughNow = counter.coldNow ? 1 : 0;
                coldEnoughPast = counter.coldPast;
            }
            if (!snowBlockSectionMap.containsKey(i)) {
                if (counter.tooWarmNow)
                    meltableNow.add(section);
                if (counter.tooWarmPast)
                    meltablePast.add(section);
            }
        }

        int randomTickSpeed = level.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
        Map<BlockPos, Pair<BlockPos, Boolean>> matches = new HashMap<>();
        Map<BlockPos, BlockState> snowBlocks = new HashMap<>();
        boolean melt = randomTickSpeed > 0 && this.hasSnow.containsKey(chunk.getPos().toLong());
        if (melt) {
            //Simulate last months random snow ticks
            if (!meltablePast.isEmpty()) {
                while (lastMonthTicks > 0) {
                    lastMonthTicks--;
                    this.handleMeltSnow(chunkPos, meltablePast, level, minX, minZ, randomTickSpeed, snowBlocks, lastSeason);
                }
            }
        }
        //Simulate last months rain
        if (coldEnoughPast) {
            while (lastMonthRain > 0) {
                lastMonthRain--;
                this.handlePlaceSnow(level, minX, minZ, matches, lastSeason);
            }
        }
        if(melt) {
            if (!meltableNow.isEmpty()) {
                while (thisMonthTicks > 0) {
                    thisMonthTicks--;
                    this.handleMeltSnow(chunkPos, meltablePast, level, minX, minZ, randomTickSpeed, snowBlocks, season);
                }
            }
        }
        //Simulate this months rain
        if (coldEnoughNow == 1) {
            while (rainDiff > 0) {
                rainDiff--;
                this.handlePlaceSnow(level, minX, minZ, matches, season);
            }
        }
    }

    private void handlePlaceSnow(ServerLevel level, int minX, int minZ, Map<BlockPos, Pair<BlockPos, Boolean>> cache, EnumSeason season) {
        BlockPos blockPos;
        if (level.random.nextInt(16) == 0) {
            blockPos = level.getBlockRandomPos(minX, 0, minZ, 15);
            Pair<BlockPos, Boolean> val = cache.get(blockPos);
            if (val == null) {
                //Cache biomes
                BlockPos heightmapPos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, blockPos);
                Biome biome = level.getBiome(heightmapPos).value();
                boolean cold = WorldUtils.coldEnoughForSnow(heightmapPos, biome, () -> season) && biome.getPrecipitation() != Biome.Precipitation.NONE;
                cache.put(blockPos, Pair.of(heightmapPos, cold));
                if (cold && WorldUtils.canPlaceSnowAt(level, heightmapPos)) {
                    level.setBlockAndUpdate(heightmapPos, ModBlocks.snow.get().defaultBlockState());
                }
            } else if (val.getSecond() && WorldUtils.canPlaceSnowAt(level, val.getFirst())) {
                level.setBlockAndUpdate(val.getFirst(), ModBlocks.snow.get().defaultBlockState());
            }
        }
    }

    private void handleMeltSnow(ChunkPos chunkPos, List<LevelChunkSection> sections, ServerLevel level, int minX, int minZ, int randomTickSpeed, Map<BlockPos, BlockState> cache, EnumSeason season) {
        BlockPos pos;
        for (LevelChunkSection levelChunkSection : sections) {
            if (!levelChunkSection.isRandomlyTickingBlocks())
                continue;
            int k = levelChunkSection.bottomBlockY();
            for (int i = 0; i < randomTickSpeed; i++) {
                pos = level.getBlockRandomPos(minX, k, minZ, 15);
                BlockState state = cache.get(pos);
                if (state == null) {
                    state = levelChunkSection.getBlockState(pos.getX() - minX, pos.getY() - k, pos.getZ() - minZ);
                    cache.put(pos, state);
                }
                if (state.getBlock() == ModBlocks.snow.get()) {
                    if (BlockMeltableSnow.melt(state, level, pos, level.getRandom(), () -> season)) {
                        SnowLayerBlock.dropResources(state, level, pos);
                        level.removeBlock(pos, false);
                    }
                    if (!this.hasSnow.containsKey(chunkPos.toLong()))
                        return;
                    if (!levelChunkSection.isRandomlyTickingBlocks())
                        break;
                }
            }
        }
    }

    public void onSeasonChange(ServerLevel level) {
        this.lastMonthTime = this.monthTime;
        this.monthTime = level.getDayTime();
        this.lastMonthRainValue = this.globalRainValue;
        this.globalRainValue = 0;
        this.setDirty();
    }

    public void onChunkUnload(ServerLevel level, ChunkAccess chunk) {
        this.unloadedRainValues.put(chunk.getPos().toLong(), new ChunkWeatherData(level.getDayTime(), this.globalRainValue));
        this.setDirty();
    }

    public void tickWeather(ServerLevel level) {
        if (level.isRaining())
            this.globalRainValue++;
        else
            this.globalRainValue = Math.max(0, --this.globalRainValue);
        this.setDirty();
    }

    public void incrementSnow(BlockPos pos) {
        this.hasSnow.putIfAbsent(ChunkPos.asLong(pos), new Int2ObjectOpenHashMap<>())
                .putIfAbsent(SectionPos.blockToSectionCoord(pos.getY()), new LongOpenHashSet())
                .add(pos.asLong());
        this.setDirty();
    }

    public void decrementSnow(BlockPos pos) {
        long chunk = ChunkPos.asLong(pos);
        Int2ObjectMap<LongSet> snowBlocks = this.hasSnow.get(chunk);
        if (snowBlocks != null) {
            int sectionId = SectionPos.blockToSectionCoord(pos.getY());
            LongSet sectionBlocks = snowBlocks.get(sectionId);
            sectionBlocks.remove(pos.asLong());
            if (snowBlocks.isEmpty()) {
                sectionBlocks.remove(sectionId);
                if (sectionBlocks.isEmpty())
                    this.hasSnow.remove(chunk);
            }
            this.setDirty();
        }
    }

    public void load(CompoundTag compoundTag) {
        this.globalRainValue = compoundTag.getLong("GlobalRain");
        this.lastMonthRainValue = compoundTag.getLong("LastGlobalRain");
        this.monthTime = compoundTag.getLong("MonthStart");
        this.lastMonthTime = compoundTag.getLong("LastMonthStart");

        ListTag tag = compoundTag.getList("ChunkValues", Tag.TAG_COMPOUND);
        tag.forEach(t -> {
            CompoundTag valTag = (CompoundTag) t;
            this.unloadedRainValues.put(valTag.getLong("Pos"), new ChunkWeatherData(valTag.getLong("ServerTime"), valTag.getLong("RainValue")));
        });
        ListTag snowTag = compoundTag.getList("SnowBlocks", Tag.TAG_COMPOUND);
        snowTag.forEach(t -> {
            CompoundTag valTag = (CompoundTag) t;
            CompoundTag sectionVal = valTag.getCompound("Value");
            Int2ObjectMap<LongSet> sectionMap = new Int2ObjectOpenHashMap<>();
            sectionVal.getAllKeys().forEach(sectionID -> {
                LongSet blocks = new LongOpenHashSet();
                ListTag positions = sectionVal.getList(sectionID, Tag.TAG_COMPOUND);
                positions.forEach(b -> blocks.add(((LongTag) b).getAsLong()));
                sectionMap.put(Integer.valueOf(sectionID).intValue(), blocks);
            });
            this.hasSnow.put(valTag.getLong("Pos"), sectionMap);
        });
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putLong("GlobalRain", this.globalRainValue);
        compoundTag.putLong("LastGlobalRain", this.lastMonthRainValue);
        compoundTag.putLong("MonthStart", this.monthTime);
        compoundTag.putLong("LastMonthStart", this.lastMonthTime);

        ListTag tag = new ListTag();
        this.unloadedRainValues.forEach((pos, val) -> {
            CompoundTag tagVal = new CompoundTag();
            tagVal.putLong("Pos", pos);
            tagVal.putLong("ServerTime", val.serverTime);
            tagVal.putLong("RainValue", val.rainValue);
            tag.add(tagVal);
        });
        compoundTag.put("ChunkValues", tag);

        ListTag snowTag = new ListTag();
        this.hasSnow.forEach((pos, sectionData) -> {
            CompoundTag tagVal = new CompoundTag();
            tagVal.putLong("Pos", pos);
            CompoundTag sectionVal = new CompoundTag();
            sectionData.forEach((sectionID, set) -> {
                ListTag positions = new ListTag();
                set.forEach(p -> positions.add(LongTag.valueOf(p)));
                sectionVal.put("" + sectionID, positions);
            });
            tagVal.put("Value", sectionVal);
            snowTag.add(tagVal);
        });
        compoundTag.put("SnowBlocks", snowTag);

        return compoundTag;
    }

    record ChunkWeatherData(long serverTime, long rainValue) {
    }

    private static class BiomeCounter implements PalettedContainer.CountConsumer<Holder<Biome>> {

        private final BlockPos pos, coldPos;
        private final EnumSeason last;
        private final EnumSeason current;
        private final boolean checkCold;

        protected boolean tooWarmNow, tooWarmPast;
        protected boolean coldNow, coldPast;

        private BiomeCounter(BlockPos pos, EnumSeason last, EnumSeason current, boolean checkCold) {
            this.pos = pos;
            this.coldPos = pos.immutable().above(15);
            this.last = last;
            this.current = current;
            this.checkCold = checkCold;
        }

        @Override
        public void accept(Holder<Biome> biome, int i) {
            this.tooWarmPast = !WorldUtils.coldEnoughForSnow(this.pos, biome.value(), () -> this.last);
            this.tooWarmNow = !WorldUtils.coldEnoughForSnow(this.pos, biome.value(), () -> this.current);
            if (this.checkCold) {
                this.tooWarmPast = WorldUtils.coldEnoughForSnow(this.coldPos, biome.value(), () -> this.last);
                this.tooWarmNow = WorldUtils.coldEnoughForSnow(this.coldPos, biome.value(), () -> this.current);
            }
        }
    }
}
