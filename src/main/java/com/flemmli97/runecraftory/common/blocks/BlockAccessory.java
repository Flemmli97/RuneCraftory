package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileAccessory;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockAccessory extends BlockCrafting {

    public BlockAccessory(Properties p_i48440_1_) {
        super(EnumCrafting.ARMOR, p_i48440_1_);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileAccessory();
    }
}
