package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public interface AttributeInstanceExtension {

    void runecraftory$setAttributeModifierFilter(@Nullable Predicate<AttributeModifier> filter);
}
