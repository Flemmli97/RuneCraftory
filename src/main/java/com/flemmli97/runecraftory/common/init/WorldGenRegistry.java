package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.core.handler.config.GenerationConfig;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.world.DungeonGenerator;
import com.flemmli97.runecraftory.common.world.MineralGenerator;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenRegistry
{
    public static final void init() {
        LibReference.logger.info("Register worldgen");
        GameRegistry.registerWorldGenerator(new MineralGenerator(GenerationConfig.mineralRate), 3);
        GameRegistry.registerWorldGenerator(new DungeonGenerator(), 15);
        LibReference.logger.info("Finished registering worldgen");
    }
}
