package com.flemmli97.runecraftory.common.blocks.crops;

import java.util.Random;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTreeSapling extends BlockBush implements IGrowable{
	
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    private Block tree;
    
	public BlockTreeSapling(String name, Block tree)
	{
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.tree=tree;
		this.setCreativeTab(RuneCraftory.crops);
	}
	
    @Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }

    @Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            super.updateTick(worldIn, pos, state, rand);

            if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
            if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0)
            {
                this.grow(worldIn, rand, pos, state);
            }
        }
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }

    @Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return worldIn.rand.nextFloat() < 0.45D;
    }

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {		
	}
}
