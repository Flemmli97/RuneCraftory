package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileChemistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockChemistry extends BlockCrafting {

    public BlockChemistry(AbstractBlock.Properties props) {
        super(EnumCrafting.CHEM, props);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileChemistry();
    }
}
