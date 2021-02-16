package com.flemmli97.runecraftory.mixin;

import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(FlatGenerationSettings.class)
public interface FlatGenSettingAccessor {

    @Accessor
    static Map<Structure<?>, StructureFeature<?, ?>> getSTRUCTURES() {
        throw new IllegalStateException();
    }
}
