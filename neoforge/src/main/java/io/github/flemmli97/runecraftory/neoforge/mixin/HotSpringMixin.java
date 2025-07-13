package io.github.flemmli97.runecraftory.neoforge.mixin;

import io.github.flemmli97.runecraftory.common.fluid.HotSpringFluid;
import io.github.flemmli97.runecraftory.neoforge.registry.ModFluidTypes;
import net.neoforged.neoforge.common.extensions.IFluidExtension;
import net.neoforged.neoforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HotSpringFluid.class)
public class HotSpringMixin implements IFluidExtension {

    @Override
    public FluidType getFluidType() {
        return ModFluidTypes.HOT_SPRING_TYPE.get();
    }
}
