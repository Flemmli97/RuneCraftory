package io.github.flemmli97.runecraftory.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.flemmli97.runecraftory.neoforge.registry.RuneCraftoryFluidTypes;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.extensions.IEntityExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityExtension {

    /**
     * Path Neoforge patch of updateFluidHeightAndDoFluidPushing that only allows vanilla water to be treated as water
     */
    @ModifyReturnValue(method = "updateFluidHeightAndDoFluidPushing(Lnet/minecraft/tags/TagKey;D)Z", at = @At(value = "RETURN", ordinal = 0), remap = false)
    private boolean allowTagReturn(boolean orig) {
        return orig || this.isInFluidType(RuneCraftoryFluidTypes.HOT_SPRING_TYPE.get());
    }
}
