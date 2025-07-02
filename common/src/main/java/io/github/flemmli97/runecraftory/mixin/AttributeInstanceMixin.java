package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.flemmli97.runecraftory.mixinhelper.AttributeInstanceExtension;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements AttributeInstanceExtension {

    @Unique
    private Predicate<AttributeModifier> runecraftory$modifierFilter;

    @Shadow
    protected abstract void setDirty();

    @Override
    public void runecraftory$setAttributeModifierFilter(@Nullable Predicate<AttributeModifier> filter) {
        this.runecraftory$modifierFilter = filter;
        this.setDirty();
    }

    @ModifyReturnValue(method = "getModifiersOrEmpty", at = @At("RETURN"))
    private Collection<AttributeModifier> filterModifiers(Collection<AttributeModifier> original) {
        if (this.runecraftory$modifierFilter == null)
            return original;
        return original.stream().filter(this.runecraftory$modifierFilter)
                .collect(Collectors.toSet());
    }
}
