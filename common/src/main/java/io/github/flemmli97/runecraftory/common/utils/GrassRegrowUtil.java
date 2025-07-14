package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.world.data.Calendar;
import io.github.flemmli97.tenshilib.loader.registry.RegistryEntrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.List;

public class GrassRegrowUtil {

    private static final EnumMap<Season, List<HerbEntry>> SEASON_HERB_GROW_MAP = getSeasonHerbGrowMap();

    public static void tryGrowHerb(ServerLevel level, BlockPos pos) {
        Season currentSeason = Calendar.get(level).currentSeason();
        List<HerbEntry> l = SEASON_HERB_GROW_MAP.get(currentSeason);
        BlockState state = WeightedRandom.getRandomItem(level.random, l).map(e -> e.sup.get().defaultBlockState()).orElse(Blocks.AIR.defaultBlockState());
        if (state.getBlock() != Blocks.AIR)
            level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    private static EnumMap<Season, List<HerbEntry>> getSeasonHerbGrowMap() {
        EnumMap<Season, List<HerbEntry>> map = new EnumMap<>(Season.class);
        map.put(Season.SPRING, List.of(new HerbEntry(100, RuneCraftoryBlocks.WEEDS),
                new HerbEntry(30, RuneCraftoryBlocks.GREEN_GRASS), new HerbEntry(30, RuneCraftoryBlocks.ORANGE_GRASS),
                new HerbEntry(50, RuneCraftoryBlocks.ANTIDOTE_GRASS), new HerbEntry(50, RuneCraftoryBlocks.MEDICINAL_HERB),
                new HerbEntry(15, RuneCraftoryBlocks.BAMBOO_SPROUT)));
        map.put(Season.SUMMER, List.of(new HerbEntry(100, RuneCraftoryBlocks.WEEDS),
                new HerbEntry(30, RuneCraftoryBlocks.GREEN_GRASS), new HerbEntry(30, RuneCraftoryBlocks.YELLOW_GRASS),
                new HerbEntry(30, RuneCraftoryBlocks.BLUE_GRASS), new HerbEntry(30, RuneCraftoryBlocks.PURPLE_GRASS),
                new HerbEntry(50, RuneCraftoryBlocks.ANTIDOTE_GRASS), new HerbEntry(50, RuneCraftoryBlocks.MEDICINAL_HERB),
                new HerbEntry(15, RuneCraftoryBlocks.BAMBOO_SPROUT)));
        map.put(Season.AUTUMN, List.of(new HerbEntry(100, RuneCraftoryBlocks.WEEDS),
                new HerbEntry(30, RuneCraftoryBlocks.YELLOW_GRASS), new HerbEntry(20, RuneCraftoryBlocks.RED_GRASS),
                new HerbEntry(20, RuneCraftoryBlocks.ORANGE_GRASS), new HerbEntry(50, RuneCraftoryBlocks.ANTIDOTE_GRASS),
                new HerbEntry(50, RuneCraftoryBlocks.MEDICINAL_HERB), new HerbEntry(15, RuneCraftoryBlocks.BAMBOO_SPROUT)));
        map.put(Season.WINTER, List.of(new HerbEntry(100, RuneCraftoryBlocks.WEEDS),
                new HerbEntry(20, RuneCraftoryBlocks.WHITE_GRASS), new HerbEntry(20, RuneCraftoryBlocks.BLACK_GRASS),
                new HerbEntry(30, RuneCraftoryBlocks.INDIGO_GRASS), new HerbEntry(50, RuneCraftoryBlocks.ANTIDOTE_GRASS),
                new HerbEntry(50, RuneCraftoryBlocks.MEDICINAL_HERB), new HerbEntry(15, RuneCraftoryBlocks.BAMBOO_SPROUT)));
        return map;
    }

    private static class HerbEntry implements WeightedEntry {

        private final Weight weight;
        public final RegistryEntrySupplier<Block, ?> sup;

        public HerbEntry(int weight, RegistryEntrySupplier<Block, ?> sup) {
            this.weight = Weight.of(weight);
            this.sup = sup;
        }

        @Override
        public Weight getWeight() {
            return this.weight;
        }
    }
}
