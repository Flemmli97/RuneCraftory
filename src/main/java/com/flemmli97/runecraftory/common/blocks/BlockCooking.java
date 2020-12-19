package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCooking;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockCooking extends BlockCrafting{

    public BlockCooking(Properties p_i48440_1_) {
        super(EnumCrafting.COOKING, p_i48440_1_);
    }

    @Override
    public TileEntity getTile(BlockState state, IBlockReader world) {
        return new TileCooking();
    }
}
