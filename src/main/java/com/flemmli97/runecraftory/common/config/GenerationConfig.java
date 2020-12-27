package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.config.values.GenConfig;
import com.flemmli97.tenshilib.common.config.Configuration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerationConfig {

    public static final List<GenConfig> mineralGen = Lists.newArrayList();

    public static void load(GenerationConfigSpec spec) {

    }

    public static List<GenConfig> mineralGenFrom(Set<BiomeDictionary.Type> types){
        return mineralGen.stream().filter(c->
            (c.blackList().isEmpty() || Sets.intersection(types, c.blackList()).isEmpty())
                    &&(c.whiteList().isEmpty() || !Sets.intersection(types, c.whiteList()).isEmpty())
        ).collect(Collectors.toList());
    }

    public static class GenerationConfigSpec {

        public static final Configuration<GenerationConfigSpec> config = new Configuration<>(GenerationConfigSpec::new, (p) -> p.resolve(RuneCraftory.MODID).resolve("generation.toml"), GenerationConfig::load, RuneCraftory.MODID);

        private GenerationConfigSpec(ForgeConfigSpec.Builder builder) {

        }
    }
}
