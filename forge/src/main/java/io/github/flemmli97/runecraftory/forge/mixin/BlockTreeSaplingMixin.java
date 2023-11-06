package io.github.flemmli97.runecraftory.forge.mixin;

import io.github.flemmli97.runecraftory.common.blocks.BlockTreeSapling;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockTreeSapling.class)
public class BlockTreeSaplingMixin implements IPlantable {

    @Override
    public PlantType getPlantType(BlockGetter level, BlockPos pos) {
        return null;
    }

    @Override
    public BlockState getPlant(BlockGetter getter, BlockPos pos) {
        return getter.getBlockState(pos);
    }
}
