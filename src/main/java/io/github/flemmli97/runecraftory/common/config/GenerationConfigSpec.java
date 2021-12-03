package io.github.flemmli97.runecraftory.common.config;

import io.github.flemmli97.runecraftory.common.config.values.HerbGenCofigSpecs;
import io.github.flemmli97.runecraftory.common.config.values.HerbGenConfig;
import io.github.flemmli97.runecraftory.common.config.values.MineralGenConfig;
import io.github.flemmli97.runecraftory.common.config.values.MineralGenConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class GenerationConfigSpec {

    public static final Pair<GenerationConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(GenerationConfigSpec::new);

    public final Map<String, MineralGenConfigSpec> mineralSpecs = new HashMap<>();
    public final Map<String, HerbGenCofigSpecs> herbSpecs = new HashMap<>();
    public final ForgeConfigSpec.ConfigValue<Integer> overworldHerbChance;
    public final ForgeConfigSpec.ConfigValue<Integer> overworldHerbTries;
    public final ForgeConfigSpec.ConfigValue<Integer> netherHerbChance;
    public final ForgeConfigSpec.ConfigValue<Integer> netherHerbTries;
    public final ForgeConfigSpec.ConfigValue<Integer> endHerbChance;
    public final ForgeConfigSpec.ConfigValue<Integer> endHerbTries;

    private GenerationConfigSpec(ForgeConfigSpec.Builder builder) {
        builder.push("mineral");
        for (MineralGenConfig conf : GenerationConfig.allMineralConfs()) {
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
        for (HerbGenConfig conf : GenerationConfig.allHerbConfs()) {
            builder.push(conf.blockRes().toString());
            this.herbSpecs.put(conf.blockRes().toString(), new HerbGenCofigSpecs(builder, conf));
            builder.pop();
        }
        builder.pop();
    }
}
