package io.github.flemmli97.runecraftory.mixin;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RangedAttribute.class)
public interface AttributeAccessor {

    @Accessor("maxValue")
    @Mutable
    void setMaxValue(double value);
}
