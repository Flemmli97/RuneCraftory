package io.github.flemmli97.runecraftory.neoforge.registry;

import io.github.flemmli97.runecraftory.RuneCraftory;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RuneCraftoryFluidTypes {

    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, RuneCraftory.MODID);

    public static final DeferredHolder<FluidType, FluidType> HOT_SPRING_TYPE = FLUID_TYPES.register("hot_spring_water", () -> new FluidType(FluidType.Properties.create()
            .descriptionId("block.runecraftory.hot_spring_water")
            .fallDistanceModifier(0F)
            .canExtinguish(true)
            .supportsBoating(true)
            .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
            .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
            .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
            .canHydrate(true)
            .addDripstoneDripping(PointedDripstoneBlock.WATER_TRANSFER_PROBABILITY_PER_RANDOM_TICK, ParticleTypes.DRIPPING_DRIPSTONE_WATER, Blocks.WATER_CAULDRON, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON)));
}
