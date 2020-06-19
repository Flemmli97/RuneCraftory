package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class BlockRFFarmland extends BlockFarmland implements ITileEntityProvider{
	
	public BlockRFFarmland()
	{
		super();
        this.setTickRandomly(false);
        this.setHardness(0.6F);
        this.blockSoundType = SoundType.GROUND;
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "farmland"));
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable)
    {
        EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
        return plantType==EnumPlantType.Crop;
    }
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
		entityIn.fall(fallDistance, 1.0F);
    }
	
	//Reset farmland growth age for normal crops
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
		IBlockState up = world.getBlockState(pos.up());
		TileFarmland tile = (TileFarmland) world.getTileEntity(pos);
		if(tile!=null)
		{
			if(!(up.getBlock() instanceof BlockCrops))
				tile.resetGrowth();
			else
			{
				if(!up.getPropertyKeys().contains(BlockCrops.AGE))
					;
				else if(up.getValue(BlockCrops.AGE)==0 && tile.age()!=0)
					tile.resetGrowth();
			}
		}
		super.neighborChanged(state, world, pos, blockIn, fromPos);
    }
	
	@Override
	public void fillWithRain(World world, BlockPos pos)
    {
		world.setBlockState(pos, this.getDefaultState().withProperty(BlockFarmland.MOISTURE, 7));
    }

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileFarmland(world);
	}
}
