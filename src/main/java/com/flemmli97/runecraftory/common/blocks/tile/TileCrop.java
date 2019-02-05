package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCrop extends TileEntity{

	public TileCrop() {}
	private boolean isGiant=false;
	private float age;
	private long lastGrowth=0;
	private float level;
	private boolean withered;
	private BlockPos[] nearbyGiant;
	
	public int age()
	{
		return (int) this.age;
	}
	
	public void resetAge()
	{
		this.age=0;
		this.markDirty();
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
		return this.age>=block.properties().growth();
	}
	
	public boolean canGrow()
	{
		return Math.abs(this.world.getWorldTime()/24000 - this.lastGrowth/24000)>=1;
	}
	
    public void growCrop(World world, BlockPos pos, IBlockState state, float speed, float level, float seasonModifier)
    {
    	Block block = state.getBlock();
    	this.age+=speed*seasonModifier;

		if(block instanceof BlockCropBase)
		{
			this.age=Math.min(((BlockCropBase) block).properties().growth(), this.age);
		}
		this.world.notifyBlockUpdate(pos, state, state, 2);
    	this.markDirty();
    }
    
    public void setWithered()
    {
    	this.withered=true;
    	//this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withMirror(mirrorIn))
    }
    
    public boolean isWithered()
    {
    	return this.withered;
    }
    
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }
    
    public void postProcess()
    {
    	
    }
    
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
	}
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) 
	{
		this.readFromNBT(pkt.getNbtCompound());
		this.getWorld().notifyBlockUpdate(this.pos, this.getWorld().getBlockState(this.pos), this.getWorld().getBlockState(this.pos), 3);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.age=compound.getFloat("Age");
		this.isGiant=compound.getBoolean("Giant");
		this.level=compound.getFloat("Level");
		this.withered=compound.getBoolean("Withered");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setFloat("Age", this.age);
		compound.setBoolean("Giant", this.isGiant);
		compound.setFloat("Level", this.level);
		compound.setBoolean("Withered", this.withered);
		return compound;
	}
}
