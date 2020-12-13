package com.flemmli97.runecraftory.common.config;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.tenshilib.common.config.Configuration;
import net.minecraftforge.common.ForgeConfigSpec;

public class ItemConfig {


    public static void load(ItemConfigSpec spec) {

    }

    public static class ItemConfigSpec {

        public static final Configuration<ItemConfigSpec> config = new Configuration<ItemConfigSpec>(ItemConfigSpec::new, (p) -> p.resolve(RuneCraftory.MODID).resolve("mobs.toml"), ItemConfig::load, RuneCraftory.MODID);

        private ItemConfigSpec(ForgeConfigSpec.Builder builder) {

        }
    }
}
