package com.flemmli97.runecraftory.common.world.village;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class NPCVillageHandler extends WorldSavedData{

	private static final String identifier = "NPCVillages";

	private Map<UUID,NPCVillage> villages = Maps.newHashMap();
	
	public NPCVillageHandler(String identifier) {
		super(identifier);
	}
	
	public NPCVillageHandler()
	{
		this(identifier);
	}
	
	public static NPCVillageHandler get(World world)
	{
		MapStorage storage = world.getMapStorage();
		NPCVillageHandler data = (NPCVillageHandler)storage.getOrLoadData(NPCVillageHandler.class, identifier);
		if (data == null)
		{
			data = new NPCVillageHandler();
			storage.setData(identifier, data);
		}
		return data;
	}
	
	public void addVillage(NPCVillage village) {
		this.villages.put(village.getUUID(), village);
		this.markDirty();
	}
	
	public boolean removeVillage(NPCVillage village) {
		this.markDirty();
		return this.villages.remove(village.getUUID()) != null;
	}
	
	public NPCVillage getFromUUID(UUID uuid) {
		return this.villages.get(uuid);
	}
	
	public NPCVillage nearestFrom(BlockPos pos, double maxDist) {
		double d = -1;
		NPCVillage village = null;
		for(NPCVillage vill : this.villages.values()) {
			double dist = pos.distanceSq(vill.getTownHallPos());
			if((d==-1 || dist < d) && dist < maxDist*maxDist) {
				d = dist;
				village = vill;
			}
		}
		return village;
	}
	
	public void tick(World world) {
		for(NPCVillage vill : this.villages.values()) {
			vill.update(world);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		nbt.getTagList("villages", Constants.NBT.TAG_COMPOUND).forEach(tag->{
			NPCVillage village = new NPCVillage((NBTTagCompound) tag);
			this.villages.put(village.getUUID(), village);
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		this.villages.values().forEach(village->list.appendTag(village.writeToNBT(new NBTTagCompound())));
		compound.setTag("villages", list);
		return compound;
	}
}
