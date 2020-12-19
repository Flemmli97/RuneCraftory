package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

public class BlockForge extends BlockCrafting{

    public BlockForge(Properties p_i48440_1_) {
        super(EnumCrafting.FORGE, p_i48440_1_);
    }

    @Override
    public TileEntity getTile(BlockState state, IBlockReader world) {
        return new TileForge();
    }
}
