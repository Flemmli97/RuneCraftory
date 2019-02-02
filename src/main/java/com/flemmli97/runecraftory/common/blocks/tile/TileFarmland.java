package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.blocks.IDailyTickable;
import com.flemmli97.runecraftory.api.items.CropProperties;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.core.handler.time.CalendarHandler;
import com.flemmli97.runecraftory.common.core.handler.time.WeatherData;
import com.flemmli97.runecraftory.common.core.handler.time.WeatherData.EnumWeather;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *	Saves data also for the crop of the farmland
 */
public class TileFarmland extends TileEntity implements IDailyTickable, ITickable{

	private float growthMultiplier=1;
	private boolean growGiant;
	private int health=255;
	private float level;

	//Used for other crops not from this mod
	private float age;
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
		//if(this.world instanceof WorldServer)
		//	DailyBlockTickHandler.instance((WorldServer) this.world).add(pos);
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
        this.age=compound.getFloat("Age");
        this.growGiant=compound.getBoolean("Giant");
        //Looks up the last time this tile was saved.
        long saveTime = compound.getLong("SaveTime");
        if(this.world!=null && !this.world.isRemote)
        {
    		long diff = this.world.getWorldTime()-saveTime;
    		while(diff>24000)
    		{
    			this.dailyUpdate(this.world, this.pos, this.world.getBlockState(this.pos));
    			diff-=24000;
    		}
        }
    }
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
		super.writeToNBT(compound);
		compound.setInteger("Health", this.health);
		compound.setFloat("Level", this.level);
		compound.setFloat("Growth", this.growthMultiplier);
		compound.setFloat("Age", this.age);
		compound.setBoolean("Giant", this.growGiant);
		compound.setLong("SaveTime", this.world.getWorldTime());
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
	
	public boolean growGiant()
	{
		return this.growGiant;
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
	
	public void applyHealth(int amount)
	{
		this.health=Math.min(255, this.health+amount);
		this.markDirty();
	}
	
	public void applySizeFertilizer(boolean growGiant)
	{
		this.growGiant=growGiant;
		this.markDirty();
	}
	
	@Override
	public void dailyUpdate(World world, BlockPos pos, IBlockState state) {
		boolean isWet = world.getBlockState(pos).getValue(BlockFarmland.MOISTURE)>0;
		BlockPos cropPos = pos.up();
		IBlockState cropState = world.getBlockState(cropPos);
		boolean didCropGrow = false;
		if(isWet)
		{
			world.setBlockState(pos, state.withProperty(BlockFarmland.MOISTURE, 0));
			if(cropState.getBlock() instanceof BlockCropBase)
			{
				//Let the crop tile entity handle growth
				BlockCropBase crop = (BlockCropBase) cropState.getBlock();
				TileCrop tile = (TileCrop) world.getTileEntity(cropPos);
				float season = crop.properties().seasonMultiplier(CalendarHandler.get(world).currentSeason());
				float runeyBonus = WeatherData.get(world).currentWeather()==EnumWeather.RUNEY?7:1;
				if(!tile.isFullyGrown(crop))
					tile.growCrop(world, cropPos, cropState, this.growthMultiplier*runeyBonus, this.level, season);
				didCropGrow = true;
	            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, cropPos, state, world.getBlockState(cropPos));
			}
			else if(cropState.getBlock() instanceof BlockCrops)
			{
				BlockCrops crop = (BlockCrops) cropState.getBlock();
				CropProperties props=CropMap.getProperties(crop.getItem(this.world, cropPos, cropState));
				if(props!=null)
				{
					float season = props.seasonMultiplier(CalendarHandler.get(world).currentSeason());
					float runeyBonus = WeatherData.get(world).currentWeather()==EnumWeather.RUNEY?7:1;
					float speed = this.growthMultiplier*season*runeyBonus;
			    	this.age+=Math.min(props.growth(),speed);
			    	//The blockstates maxAge
			    	int maxAge = crop.getMaxAge();
			    	
			    	int stage = Math.round(this.age*maxAge)/props.growth();
			    	//Update the blockstate according to the growth age from this tile
			    	world.setBlockState(cropPos, crop.withAge(Math.min(stage, maxAge)), 2);
			    }
				else
				{
					//if not increase block state age by one per day with reduced bonus from growth multiplier
					int age = cropState.getValue(BlockCrops.AGE);
		            world.setBlockState(cropPos, crop.withAge(Math.min((int)(age + Math.max(1, this.growthMultiplier/2.4)), crop.getMaxAge())), 2);
				}
				didCropGrow = true;
	            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, cropPos, state, world.getBlockState(cropPos));
			}
		}
		
		if(didCropGrow)
		{
	        if(world.rand.nextInt(2)==0)
	        {
	        	this.growthMultiplier=Math.max(this.growthMultiplier-0.1F, 0.1F);
	        }
	        this.health=Math.max(0, --this.health);
	        this.level=Math.max(0, this.level-0.01F);
		}
		else
		{
			if(world.rand.nextInt(4)==0)
	        {
	        	this.growthMultiplier=Math.min(this.growthMultiplier+0.2F, 1F);
	        }
	        this.health=Math.min(++this.health, 100);
		}
		this.markDirty();
	}
	
	public float age()
	{
		return this.age;
	}
	
	public void resetGrowth()
	{
		this.age=0;
		this.markDirty();
	}
	
    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

	@Override
	public void update() {
		
		if(RFCalculations.canUpdateDaily(this.world))
			this.dailyUpdate(this.world, this.pos, this.world.getBlockState(this.pos));
	}

}
