package io.github.flemmli97.runecraftory.platform;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ContainerBlockEntity extends BlockEntity implements MenuProvider {

    public ContainerBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    public abstract Container getInventory();
}
