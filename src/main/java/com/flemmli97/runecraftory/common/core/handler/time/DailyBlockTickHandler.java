package com.flemmli97.runecraftory.common.core.handler.time;

import java.util.Set;

import com.flemmli97.runecraftory.api.blocks.IDailyTickable;
import com.google.common.collect.Sets;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

/**
 * Does not get saved to disk
 */
public class DailyBlockTickHandler extends WorldSavedData{

	private static final String id = "RCDailyTicker";
	private Set<BlockPos> dailyTickBlocks = Sets.newHashSet();

	public DailyBlockTickHandler()
	{
		this(id);
	}
	public DailyBlockTickHandler(String name) {
		super(name);
	}
	
	public static DailyBlockTickHandler instance(WorldServer world)
	{
		MapStorage storage = world.getMapStorage();
		DailyBlockTickHandler data = (DailyBlockTickHandler)storage.getOrLoadData(DailyBlockTickHandler.class, id);
		if (data == null)
		{
			data = new DailyBlockTickHandler();
			storage.setData(id, data);
		}
		return data;
	}
	
	public boolean add(BlockPos pos)
	{
		return this.dailyTickBlocks.add(pos);
	}
	
	public void update(World world)
	{
		for(BlockPos pos : dailyTickBlocks)
		{
			if(world.isBlockLoaded(pos))
				if(world.getTileEntity(pos)!=null && world.getTileEntity(pos) instanceof IDailyTickable)
				{
					((IDailyTickable)world.getTileEntity(pos)).dailyUpdate(world, pos, world.getBlockState(pos));
				}
		}
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {		
	}
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return compound;
	}
}
