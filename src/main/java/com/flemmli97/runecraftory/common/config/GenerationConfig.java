package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.values.HerbGenCofigSpecs;
import com.flemmli97.runecraftory.common.config.values.HerbGenConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import com.flemmli97.runecraftory.common.config.values.MineralGenConfigSpec;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.tenshilib.common.config.Configuration;
import com.google.common.collect.Sets;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerationConfig {

    private static final List<MineralGenConfig> mineralGen = new ArrayList<>();

    public static int overworldHerbChance;
    public static int overworldHerbTries;
    public static int netherHerbChance;
    public static int netherHerbTries;
    public static int endHerbChance;
    public static int endHerbTries;

    private static final List<HerbGenConfig> herbGen = new ArrayList<>();

    static {
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralIron.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(15).setAmount(2, 5).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralBronze.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(30).setAmount(2, 4).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralSilver.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(60).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralGold.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralPlatinum.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(200).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralOrichalcum.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(250).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralDiamond.getId())
                .addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(220).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralDragonic.getId())
                .addWhiteListType(BiomeDictionary.Type.END, BiomeDictionary.Type.VOID).withChance(25).setAmount(1, 2).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralAquamarine.getId())
                .addWhiteListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.WET).addBlackListType(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(17).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralAmethyst.getId())
                .addWhiteListType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MOUNTAIN, BiomeDictionary.Type.DEAD).addBlackListType(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralRuby.getId())
                .addWhiteListType(BiomeDictionary.Type.HOT, BiomeDictionary.Type.NETHER).withChance(75).setAmount(2, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralEmerald.getId())
                .addWhiteListType(BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.WASTELAND, BiomeDictionary.Type.SPARSE).addBlackListType(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(75).setAmount(1, 3).build());
        mineralGen.add(new MineralGenConfig.Builder(ModBlocks.mineralSapphire.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.SNOWY).addBlackListType(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withChance(75).setAmount(2, 3).build());

        herbGen.add(new HerbGenConfig.Builder(ModBlocks.weeds.getId()).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(100).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.mushroom.getId())
                .addWhiteListType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.MAGICAL).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(40).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.monarchMushroom.getId())
                .addWhiteListType(BiomeDictionary.Type.FOREST, BiomeDictionary.Type.MUSHROOM, BiomeDictionary.Type.MAGICAL).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(10).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.witheredGrass.getId()).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(45).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.whiteGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.COLD, BiomeDictionary.Type.SNOWY).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.indigoGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.WET, BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.LUSH).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.purpleGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.WET, BiomeDictionary.Type.MAGICAL).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.greenGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.FOREST, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.HILLS).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.blueGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.BEACH, BiomeDictionary.Type.RIVER, BiomeDictionary.Type.SWAMP).addBlackListType(BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.yellowGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.DRY, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.NETHER).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.redGrass.getId()).addWhiteListType(BiomeDictionary.Type.NETHER).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.orangeGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.HOT, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.SAVANNA).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.END).withWeight(50).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.blackGrass.getId()).addWhiteListType(BiomeDictionary.Type.END, BiomeDictionary.Type.VOID).withWeight(75).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.elliLeaves.getId()).addWhiteListType(BiomeDictionary.Type.END, BiomeDictionary.Type.VOID).withWeight(10).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.antidoteGrass.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(75).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.medicinalHerb.getId())
                .addWhiteListType(BiomeDictionary.Type.MAGICAL, BiomeDictionary.Type.DENSE, BiomeDictionary.Type.LUSH, BiomeDictionary.Type.HILLS, BiomeDictionary.Type.PLAINS, BiomeDictionary.Type.FOREST).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(75).build());
        herbGen.add(new HerbGenConfig.Builder(ModBlocks.bambooSprout.getId())
                .addWhiteListType(BiomeDictionary.Type.LUSH, BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.DENSE).addBlackListType(BiomeDictionary.Type.WATER, BiomeDictionary.Type.NETHER, BiomeDictionary.Type.END).withWeight(66).build());
    }

    public static void load(GenerationConfigSpec spec) {
        for (MineralGenConfig conf : mineralGen)
            conf.read(spec.mineralSpecs.get(conf.blockRes().toString()));
        overworldHerbChance = spec.overworldHerbChance.get();
        overworldHerbTries = spec.overworldHerbTries.get();
        netherHerbChance = spec.netherHerbChance.get();
        netherHerbTries = spec.netherHerbTries.get();
        endHerbChance = spec.endHerbChance.get();
        endHerbTries = spec.endHerbTries.get();
        for (HerbGenConfig conf : herbGen)
            conf.read(spec.herbSpecs.get(conf.blockRes().toString()));
    }

    public static List<MineralGenConfig> mineralGenFrom(Set<BiomeDictionary.Type> types) {
        return mineralGen.stream().filter(c ->
                (c.blackList().isEmpty() || Sets.intersection(types, c.blackList()).isEmpty())
                        && (c.whiteList().isEmpty() || !Sets.intersection(types, c.whiteList()).isEmpty())
        ).collect(Collectors.toList());
    }

    public static List<MineralGenConfig> allMineralConfs() {
        return mineralGen;
    }

    public static List<HerbGenConfig> herbGenFrom(Set<BiomeDictionary.Type> types) {
        return herbGen.stream().filter(c ->
                (c.blackList().isEmpty() || Sets.intersection(types, c.blackList()).isEmpty())
                        && (c.whiteList().isEmpty() || !Sets.intersection(types, c.whiteList()).isEmpty())
        ).collect(Collectors.toList());
    }

    public static class GenerationConfigSpec {

        public static final Configuration<GenerationConfigSpec> config = new Configuration<>(GenerationConfigSpec::new, (p) -> p.resolve(RuneCraftory.MODID).resolve("generation.toml"), GenerationConfig::load, RuneCraftory.MODID);

        private final Map<String, MineralGenConfigSpec> mineralSpecs = new HashMap<>();
        private final Map<String, HerbGenCofigSpecs> herbSpecs = new HashMap<>();
        private final ForgeConfigSpec.ConfigValue<Integer> overworldHerbChance;
        public ForgeConfigSpec.ConfigValue<Integer> overworldHerbTries;
        private final ForgeConfigSpec.ConfigValue<Integer> netherHerbChance;
        public ForgeConfigSpec.ConfigValue<Integer> netherHerbTries;
        public ForgeConfigSpec.ConfigValue<Integer> endHerbChance;
        public ForgeConfigSpec.ConfigValue<Integer> endHerbTries;

        private GenerationConfigSpec(ForgeConfigSpec.Builder builder) {
            builder.push("mineral");
            for (MineralGenConfig conf : mineralGen) {
                builder.push(conf.blockRes().toString());
                this.mineralSpecs.put(conf.blockRes().toString(), new MineralGenConfigSpec(builder, conf));
                builder.pop();
            }
            builder.pop();

            builder.push("herbs");
            this.overworldHerbChance = builder.comment("1/x Chance for herb to generate in overworld").define("Herb Chance", 10);
            this.overworldHerbTries = builder.comment("Generation tries for overworld").define("Herb Tries", 64);
            this.netherHerbChance = builder.comment("1/x Chance for herb to generate in the nether").define("Nether Herb Chance", 35);
            this.netherHerbTries = builder.comment("Generation tries for the nether").define("Nether Herb Tries", 64);
            this.endHerbChance = builder.comment("1/x Chance for herb to generate in the end").define("End Herb Chance", 7);
            this.endHerbTries = builder.comment("Generation tries for the end").define("End Herb Tries", 32);
            for (HerbGenConfig conf : herbGen) {
                builder.push(conf.blockRes().toString());
                this.herbSpecs.put(conf.blockRes().toString(), new HerbGenCofigSpecs(builder, conf));
                builder.pop();
            }
            builder.pop();
        }
    }
}
