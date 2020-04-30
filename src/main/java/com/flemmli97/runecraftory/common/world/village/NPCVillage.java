package com.flemmli97.runecraftory.common.world.village;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import com.google.common.collect.Lists;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class NPCVillage {

	private UUID uuid;
	
	private BlockPos townHall;
	
	private Map<UUID,INPCHouse> houses;
	
	private Map<UUID, BlockPos> addedBeds;
	private int radius;
	
	private int updateDelay;
	
	public NPCVillage(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}
	
	public NPCVillage(World world, BlockPos center) {
		this.townHall=center;
		this.uuid = MathHelper.getRandomUUID(world.rand);
	}
	
	public BlockPos getTownHallPos() {
		return this.townHall;
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	public void addHouse(UUID uuid, INPCHouse house) {
		this.houses.put(uuid, house);
	}
	
	public void update(World world) {
		if(this.updateDelay--<0) {
			this.updateDelay=100;
			//Check center
			
			//Check beds
			
			
			int rad = (this.radius+16)>>4;
			int chunkX = this.townHall.getX()>>4;
			int chunkZ = this.townHall.getZ()>>4;
			List<BlockPos> beds = Lists.newArrayList();
			for(int x = -rad; x < rad; x++)
				for(int z = -rad; z < rad; z++) {
					if(world.getChunkProvider().getLoadedChunk(x, z)==null)
						continue;
					Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
					chunk.getTileEntityMap().forEach((pos, tile)->{
						if(tile instanceof TileEntityBed && pos.distanceSq(this.townHall)<(this.radius+16) && !this.addedBeds.values().contains(pos))
							beds.add(pos);
					});
				}
			
			beds.forEach(pos->{
				UUID nearest = null;
				double dist = -1;
				for(Entry<UUID, BlockPos> ent : this.addedBeds.entrySet()) {
					double newD;
					if((newD = ent.getValue().distanceSq(pos))<dist || dist == -1) {
						nearest = ent.getKey();
						dist = newD;
					}
				}
				if(nearest!=null) {
					this.houses.get(nearest).getBeds().add(pos);
					this.addedBeds.put(nearest, pos);
				}
			});
		}
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		// TODO Auto-generated method stub
	}

	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub
		return null;
	}
}
