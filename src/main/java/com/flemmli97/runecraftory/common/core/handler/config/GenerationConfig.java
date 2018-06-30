package com.flemmli97.runecraftory.common.core.handler.config;

import com.flemmli97.runecraftory.common.world.DungeonGenerator;
import com.flemmli97.runecraftory.common.world.Structure;

import net.minecraftforge.common.config.Configuration;

public class GenerationConfig
{
	public static int mineralRate = 25;
    protected static void init(Configuration config) {
        config.load();
        genData(config);
        config.save();
    }
    
    private static void genData(Configuration config) {
        for (Structure structure : DungeonGenerator.structures()) 
        {
            structure.setFrequency(ConfigHandler.getIntConfig(config, "Frequency", structure.structureName(), structure.frequency(), "Has a chance of 100/x percent of generating in a chunk. Set to 0 to disable generation"));
        }
    }
}
