package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.world.DungeonGenerator;
import com.flemmli97.runecraftory.common.world.MineralGenerator;
import com.flemmli97.runecraftory.common.world.Structure;

import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenRegistry {

	public static final void init()
	{
		RuneCraftory.logger.info("Register worldgen");
        GameRegistry.registerWorldGenerator(new MineralGenerator(25), 3);
        GameRegistry.registerWorldGenerator(new DungeonGenerator(), 15);
        DungeonGenerator.addStructureGen(new Structure("AmbrosiaForest", 2, false, 0,-4, Type.FOREST, Type.LUSH));
		RuneCraftory.logger.info("Finished registering worldgen");

	}
}
