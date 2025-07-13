package io.github.flemmli97.runecraftory.neoforge.data.tags;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FluidTagGen extends IntrinsicHolderTagsProvider<Fluid> {

    public FluidTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> future, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.FLUID, future, fluid -> BuiltInRegistries.FLUID.getResourceKey(fluid).orElseThrow(), RuneCraftory.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(RunecraftoryTags.Fluids.HOT_SPRING_FLUID)
                .add(ModFluids.FLOWING_HOT_SPRING_WATER.get())
                .add(ModFluids.HOT_SPRING_WATER.get());
        this.tag(FluidTags.WATER)
                .add(ModFluids.FLOWING_HOT_SPRING_WATER.get())
                .add(ModFluids.HOT_SPRING_WATER.get());
    }
}
