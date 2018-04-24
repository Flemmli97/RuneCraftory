package com.flemmli97.runecraftory.common.world;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.flemmli97.runecraftory.common.utils.Position;
import com.flemmli97.runecraftory.common.world.Structure.PlacementProperties;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

public class StructureData extends WorldSavedData{

	private static final String id = "BossStructures";
	private Map<Position, PlacementProperties> structureMap = new HashMap<Position, PlacementProperties>();

	public StructureData(String id) {
		super(id);
	}
	
	public StructureData() {
		this(id);
	}
	
	public static StructureData get(World world)
	{
		MapStorage storage = world.getMapStorage();
		StructureData data = (StructureData)storage.getOrLoadData(StructureData.class, id);
		if (data == null)
		{
			data = new StructureData();
			storage.setData(id, data);
		}
		return data;
	}

	public boolean hasPosition(Position pos)
	{
		return this.structureMap.containsKey(pos);
	}
	
	public void put(Position pos, PlacementProperties props)
	{
		this.structureMap.put(pos, props);
		this.markDirty();
	}
	
	public Set<Position> positions()
	{
		return this.structureMap.keySet();
	}
	
	public PlacementProperties read(Position pos)
	{
		return this.structureMap.get(pos);
	}
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		NBTTagList list = nbt.getTagList("Structures", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound tag = list.getCompoundTagAt(i);
			Position chunk = new Position(tag.getInteger("ChunkX"), 0, tag.getInteger("ChunkZ"));
			PlacementProperties props = new PlacementProperties(tag.getCompoundTag("Placement"));
			this.structureMap.put(chunk, props);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		for(Position pos : this.structureMap.keySet())
		{
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("ChunkX", pos.getX());
			tag.setInteger("ChunkZ", pos.getZ());
			tag.setTag("Placement", this.structureMap.get(pos).writeToNBT());
			list.appendTag(tag);
		}
		compound.setTag("Structures", list);
		return compound;
	}
}
