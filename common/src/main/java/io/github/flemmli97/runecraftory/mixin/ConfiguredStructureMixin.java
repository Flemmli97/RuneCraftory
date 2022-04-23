package io.github.flemmli97.runecraftory.mixin;

import com.google.common.collect.ImmutableMap;
import io.github.flemmli97.runecraftory.mixinhelper.StructureModification;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ConfiguredStructureFeature.class)
public abstract class ConfiguredStructureMixin<FC extends FeatureConfiguration, F extends StructureFeature<FC>> implements StructureModification {

    @Shadow
    @Mutable
    public Map<MobCategory, StructureSpawnOverride> spawnOverrides;

    @Override
    public void addSpawn(MobCategory category, StructureSpawnOverride override) {
        ImmutableMap.Builder<MobCategory, StructureSpawnOverride> builder = new ImmutableMap.Builder<>();
        builder.putAll(this.spawnOverrides);
        builder.put(category, override);
        this.spawnOverrides = builder.build();
    }
}
