package com.flemmli97.runecraftory.common.world.forestBeginning;

import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DIMRegistry {

    public static final DimensionType FORESTOFBEGINNING = DimensionType.register("FORESTOFBEGINNING", "_forestBeginning", LibReference.dimID, ForestWorldProvider.class, false);

    public static void init() {
    	DimensionManager.registerDimension(LibReference.dimID, FORESTOFBEGINNING);
    }
    
}