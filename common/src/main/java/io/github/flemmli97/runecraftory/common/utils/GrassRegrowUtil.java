package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.calendar.Season;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
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
        map.put(Season.SPRING, List.of(new HerbEntry(100, ModBlocks.WEEDS),
                new HerbEntry(30, ModBlocks.GREEN_GRASS), new HerbEntry(30, ModBlocks.ORANGE_GRASS),
                new HerbEntry(50, ModBlocks.ANTIDOTE_GRASS), new HerbEntry(50, ModBlocks.MEDICINAL_HERB),
                new HerbEntry(15, ModBlocks.BAMBOO_SPROUT)));
        map.put(Season.SUMMER, List.of(new HerbEntry(100, ModBlocks.WEEDS),
                new HerbEntry(30, ModBlocks.GREEN_GRASS), new HerbEntry(30, ModBlocks.YELLOW_GRASS),
                new HerbEntry(30, ModBlocks.BLUE_GRASS), new HerbEntry(30, ModBlocks.PURPLE_GRASS),
                new HerbEntry(50, ModBlocks.ANTIDOTE_GRASS), new HerbEntry(50, ModBlocks.MEDICINAL_HERB),
                new HerbEntry(15, ModBlocks.BAMBOO_SPROUT)));
        map.put(Season.AUTUMN, List.of(new HerbEntry(100, ModBlocks.WEEDS),
                new HerbEntry(30, ModBlocks.YELLOW_GRASS), new HerbEntry(20, ModBlocks.RED_GRASS),
                new HerbEntry(20, ModBlocks.ORANGE_GRASS), new HerbEntry(50, ModBlocks.ANTIDOTE_GRASS),
                new HerbEntry(50, ModBlocks.MEDICINAL_HERB), new HerbEntry(15, ModBlocks.BAMBOO_SPROUT)));
        map.put(Season.WINTER, List.of(new HerbEntry(100, ModBlocks.WEEDS),
                new HerbEntry(20, ModBlocks.WHITE_GRASS), new HerbEntry(20, ModBlocks.BLACK_GRASS),
                new HerbEntry(30, ModBlocks.INDIGO_GRASS), new HerbEntry(50, ModBlocks.ANTIDOTE_GRASS),
                new HerbEntry(50, ModBlocks.MEDICINAL_HERB), new HerbEntry(15, ModBlocks.BAMBOO_SPROUT)));
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
