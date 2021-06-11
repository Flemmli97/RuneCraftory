package com.flemmli97.runecraftory.common.fluid;

import net.minecraft.fluid.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class FluidHotSpring extends ForgeFlowingFluid {

    protected FluidHotSpring(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    @Override
    public int getLevel(FluidState state) {
        return 0;
    }
}
