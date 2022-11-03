package io.github.flemmli97.runecraftory.common.utils;

import io.github.flemmli97.runecraftory.api.enums.EnumSeason;
import io.github.flemmli97.runecraftory.common.registry.ModBlocks;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.tenshilib.platform.registry.RegistryEntrySupplier;
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

    private static final EnumMap<EnumSeason, List<HerbEntry>> seasonHerbGrowMap = getSeasonHerbGrowMap();

    public static void tryGrowHerb(ServerLevel level, BlockPos pos) {
        EnumSeason currentSeason = WorldHandler.get(level.getServer()).currentSeason();
        List<HerbEntry> l = seasonHerbGrowMap.get(currentSeason);
        BlockState state = WeightedRandom.getRandomItem(level.random, l).map(e -> e.sup.get().defaultBlockState()).orElse(Blocks.AIR.defaultBlockState());
        if (state.getBlock() != Blocks.AIR)
            level.setBlock(pos, state, Block.UPDATE_ALL);
    }

    private static EnumMap<EnumSeason, List<HerbEntry>> getSeasonHerbGrowMap() {
        EnumMap<EnumSeason, List<HerbEntry>> map = new EnumMap<>(EnumSeason.class);
        map.put(EnumSeason.SPRING, List.of(new HerbEntry(100, ModBlocks.weeds),
                new HerbEntry(30, ModBlocks.greenGrass), new HerbEntry(30, ModBlocks.orangeGrass),
                new HerbEntry(50, ModBlocks.antidoteGrass), new HerbEntry(50, ModBlocks.medicinalHerb),
                new HerbEntry(15, ModBlocks.bambooSprout)));
        map.put(EnumSeason.SUMMER, List.of(new HerbEntry(100, ModBlocks.weeds),
                new HerbEntry(30, ModBlocks.greenGrass), new HerbEntry(30, ModBlocks.yellowGrass),
                new HerbEntry(30, ModBlocks.blueGrass), new HerbEntry(30, ModBlocks.purpleGrass),
                new HerbEntry(50, ModBlocks.antidoteGrass), new HerbEntry(50, ModBlocks.medicinalHerb),
                new HerbEntry(15, ModBlocks.bambooSprout)));
        map.put(EnumSeason.FALL, List.of(new HerbEntry(100, ModBlocks.weeds),
                new HerbEntry(30, ModBlocks.yellowGrass), new HerbEntry(20, ModBlocks.redGrass),
                new HerbEntry(20, ModBlocks.orangeGrass), new HerbEntry(50, ModBlocks.antidoteGrass),
                new HerbEntry(50, ModBlocks.medicinalHerb), new HerbEntry(15, ModBlocks.bambooSprout)));
        map.put(EnumSeason.WINTER, List.of(new HerbEntry(100, ModBlocks.weeds),
                new HerbEntry(20, ModBlocks.whiteGrass), new HerbEntry(20, ModBlocks.blackGrass),
                new HerbEntry(30, ModBlocks.indigoGrass), new HerbEntry(50, ModBlocks.antidoteGrass),
                new HerbEntry(50, ModBlocks.medicinalHerb), new HerbEntry(15, ModBlocks.bambooSprout)));
        return map;
    }

    private static class HerbEntry implements WeightedEntry {

        private final Weight weight;
        public final RegistryEntrySupplier<Block> sup;

        public HerbEntry(int weight, RegistryEntrySupplier<Block> sup) {
            this.weight = Weight.of(weight);
            this.sup = sup;
        }

        @Override
        public Weight getWeight() {
            return this.weight;
        }
    }
}
