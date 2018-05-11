package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.blocks.IDailyTickable;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.DailyBlockTickHandler;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class TileFarmland extends TileEntity implements IDailyTickable{

	private float growthMultiplier=1;
	private int health=255;
	private float level;
	public TileFarmland()
	{
	}
	
	public TileFarmland(World world)
	{
		this.world=world;
	}
	
	@Override
	public void setPos(BlockPos pos) {
		super.setPos(pos);
		if(this.world instanceof WorldServer)
			DailyBlockTickHandler.instance((WorldServer) this.world).add(pos);
	}
	
	//Sets the world early before nbt are read
	@Override
	protected void setWorldCreate(World world)
	{
		this.setWorld(world);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.health=compound.getInteger("Health");
        this.growthMultiplier=compound.getFloat("Growth");
        this.level=compound.getFloat("Level");
		if(this.world instanceof WorldServer)
			DailyBlockTickHandler.instance((WorldServer) this.world).add(this.pos);
    }
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		super.writeToNBT(compound);
		compound.setInteger("Health", this.health);
		compound.setFloat("Level", this.level);
		compound.setFloat("Growth", this.growthMultiplier);
        return compound;
    }
	
	public int health()
	{
		return this.health;
	}
	
	public float level()
	{
		return this.level;
	}
	
	public float growth()
	{
		return this.growthMultiplier;
	}
	
	public void applyGrowthFertilizer(float amount)
	{
		this.growthMultiplier=Math.min(5, this.growthMultiplier+amount);
		this.markDirty();
	}
	
	public void applyLevelFertilizer(float amount)
	{
		this.level=Math.min(2, (this.level+amount));
		this.markDirty();
	}
	
	@Override
	public void dailyUpdate(World world, BlockPos pos, IBlockState state) {
		boolean isWet = world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)>0;
		BlockPos cropPos = pos.up();
		IBlockState cropState = world.getBlockState(cropPos);
		if(isWet)
		{
			world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0));
			if(cropState.getBlock() instanceof BlockCrops)
			{
				BlockCrops crop = (BlockCrops) cropState.getBlock();
				int age = cropState.getValue(BlockCrops.AGE);
	            world.setBlockState(cropPos, crop.withAge(Math.min(age + 1, crop.getMaxAge())), 2);
			}
			else if(cropState.getBlock() instanceof BlockCropBase)
			{
				BlockCropBase crop = (BlockCropBase) cropState.getBlock();
				TileCrop tile = (TileCrop) world.getTileEntity(cropPos);
				float season = CalendarHandler.get(world).currentSeason() == crop.bestSeason() ? 1.5F:1;
				if(!tile.isFullyGrown(crop))
					tile.growCrop(world, cropPos, cropState, this.growthMultiplier, this.level, season);
			}
            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, cropPos, state, world.getBlockState(cropPos));
		}
        if(world.rand.nextInt(2)==0)
        {
        	this.growthMultiplier=Math.max(this.growthMultiplier-0.1F, 0.1F);
        }
        this.health--;
        this.level-=0.01F;
		this.markDirty();
	}
	
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

}
