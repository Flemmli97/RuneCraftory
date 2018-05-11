package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCrop extends TileEntity{

	public TileCrop() {}
	private boolean isGiant=false;
	private int age;
	private long lastGrowth=0;
	private float level;
	public int age()
	{
		return this.age;
	}
	
	public int level()
	{
		return (int) this.level;
	}
	
	public boolean isGiant()
	{
		return this.isGiant;
	}
	
	public void increaseLevel(float amount)
	{
		this.level+=amount;
	}
	
	public boolean isFullyGrown(BlockCropBase block)
	{
		return this.age>=block.matureDays();
	}
	
	public boolean canGrow()
	{
		return Math.abs(this.world.getWorldTime()/24000 - this.lastGrowth/24000)>=1;
	}
	
    public void growCrop(World world, BlockPos pos, IBlockState state, float speed, float level, float seasonModifier)
    {
    	this.age+=speed*seasonModifier;
    	Block block = state.getBlock();
		if(block instanceof BlockCropBase)
			world.setBlockState(pos, state.withProperty(BlockCropBase.STATUS, (this.age*3)/((BlockCropBase)block).matureDays()));
    	this.markDirty();
    }
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.age=compound.getInteger("Age");
		this.isGiant=compound.getBoolean("Giant");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("Age", this.age);
		compound.setBoolean("Giant", this.isGiant);
		return compound;
	}
    
    
}
