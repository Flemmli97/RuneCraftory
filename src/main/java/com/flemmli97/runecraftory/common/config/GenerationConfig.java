package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfigSpec;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.tenshilib.common.config.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerationConfig {

    private static final List<MineralGenConfig> mineralGen = Lists.newArrayList();

    static {
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralIron.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(20).setAmount(2, 5).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralBronze.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(30).setAmount(2, 4).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralSilver.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(55).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralGold.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralPlatinum.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(200).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralOrichalcum.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(250).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralDiamond.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(250).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralDragonic.getId())
                .addWhiteListType(BiomeDictionary.Type.END).withChance(30).setAmount(1, 2).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralAquamarine.getId())
                .addWhiteListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET).withChance(50).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralAmethyst.getId())
                .addWhiteListType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.DEAD).withChance(45).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralRuby.getId())
                .addWhiteListType(BiomeDictionary.Type.HOT, BiomeDictionary.Type.NETHER).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralEmerald.getId())
                .addWhiteListType(BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.SPARSE).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralSapphire.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.SNOWY).withChance(75).setAmount(2, 3).build());
    }

    public static void load(GenerationConfigSpec spec) {
        for (MineralGenConfig conf : mineralGen)
            conf.read(spec.mineralSpecs.get(conf.blockRes().toString()));
    }

    public static List<MineralGenConfig> mineralGenFrom(Set<BiomeDictionary.Type> types) {
        return mineralGen.stream().filter(c ->
                (c.blackList().isEmpty() || Sets.intersection(types, c.blackList()).isEmpty())
                        && (c.whiteList().isEmpty() || !Sets.intersection(types, c.whiteList()).isEmpty())
        ).collect(Collectors.toList());
    }

    public static class GenerationConfigSpec {

        public static final Configuration<GenerationConfigSpec> config = new Configuration<>(GenerationConfigSpec::new, (p) -> p.resolve(RuneCraftory.MODID).resolve("generation.toml"), GenerationConfig::load, RuneCraftory.MODID);

        private final Map<String, MineralGenConfigSpec> mineralSpecs = Maps.newHashMap();

        private GenerationConfigSpec(ForgeConfigSpec.Builder builder) {
            builder.push("mineral");
            for (MineralGenConfig conf : mineralGen) {
                builder.push(conf.blockRes().toString());
                mineralSpecs.put(conf.blockRes().toString(), new MineralGenConfigSpec(builder, conf));
                builder.pop();
            }
            builder.pop();
        }
    }
}
