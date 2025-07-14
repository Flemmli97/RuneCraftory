package io.github.flemmli97.runecraftory.common.fluid;

import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryBlocks;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryFluids;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;

public abstract class HotSpringFluid extends WaterFluid {

    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        }
        if (random.nextInt(20) == 0) {
            level.addParticle(ParticleTypes.END_ROD,
                    pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble() * 1.2, pos.getZ() + random.nextDouble(),
                    0.0, 0.0, 0.0);
        }
        BlockState above;
        if (random.nextInt(48) == 0 && (above = level.getBlockState(pos.above())).getFluidState().isEmpty() && above.getCollisionShape(level, pos.above()).isEmpty()) {
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    pos.getX() + random.nextDouble(), pos.getY() + 0.3 + random.nextDouble(), pos.getZ() + random.nextDouble(),
                    0.0, 0.07, 0.0);
        }
    }

    @Override
    public Fluid getFlowing() {
        return RuneCraftoryFluids.FLOWING_HOT_SPRING_WATER.get();
    }

    @Override
    public Fluid getSource() {
        return RuneCraftoryFluids.HOT_SPRING_WATER.get();
    }

    @Override
    public Item getBucket() {
        return RuneCraftoryItems.HOT_SPRING_BUCKET.get();
    }

    @Override
    protected boolean canConvertToSource(Level level) {
        return false;
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return RuneCraftoryBlocks.HOT_SPRING_WATER.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public boolean isSame(Fluid fluid) {
        return fluid == this.getSource() || fluid == this.getFlowing();
    }

    public static class Flowing extends HotSpringFluid {

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }
    }

    public static class Source extends HotSpringFluid {

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }
    }
}
