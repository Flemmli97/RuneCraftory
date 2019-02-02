package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.world.HerbGenerator;
import com.flemmli97.runecraftory.common.world.MineralGenerator;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenRegistry
{
    public static final void init() {
        LibReference.logger.info("Registering worldgen");
        GameRegistry.registerWorldGenerator(new MineralGenerator(), 3);
        GameRegistry.registerWorldGenerator(new HerbGenerator(), 3);

    }
}
