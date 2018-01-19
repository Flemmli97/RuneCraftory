package com.flemmli97.runecraftory.common.lib.world.forestBeginning;

import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class DIMRegistry {

    public static final DimensionType FORESTOFBEGINNING = DimensionType.register("FORESTOFBEGINNING", "_forestBeginning", RFReference.dimID, ForestWorldProvider.class, false);

    public static void init() {
    	DimensionManager.registerDimension(RFReference.dimID, FORESTOFBEGINNING);
    }
    
}