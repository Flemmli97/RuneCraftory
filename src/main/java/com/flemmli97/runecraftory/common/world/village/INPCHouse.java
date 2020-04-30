package com.flemmli97.runecraftory.common.world.village;

import java.util.List;
import java.util.UUID;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;

import net.minecraft.util.math.BlockPos;

public interface INPCHouse {

	public List<BlockPos> getBeds();
	
	public BlockPos getWorkPos();
		
	public List<UUID> residents();
	
	public boolean addResident(EntityNPCBase npc);
}
