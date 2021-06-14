package io.github.flemmli97.runecraftory.mixin;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(DimensionStructuresSettings.class)
public interface DimStrucSetAccess {

    @Accessor("field_236191_b_")
    static void setDEFAULT_STRUCTURES(ImmutableMap<Structure<?>, StructureSeparationSettings> map) {
        throw new IllegalStateException();
    }

    @Accessor("field_236193_d_")
    void setStructures(Map<Structure<?>, StructureSeparationSettings> map);
}
