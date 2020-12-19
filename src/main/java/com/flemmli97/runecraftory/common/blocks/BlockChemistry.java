package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileChemistry;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockChemistry extends BlockCrafting {

    public BlockChemistry(Properties p_i48440_1_) {
        super(EnumCrafting.CHEM, p_i48440_1_);
    }

    @Override
    public TileEntity getTile(BlockState state, IBlockReader world) {
        return new TileChemistry();
    }
}
