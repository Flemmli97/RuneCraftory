package com.flemmli97.runecraftory.common.world.village;

import java.util.List;
import java.util.UUID;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;

import net.minecraft.util.math.BlockPos;

public class BasicNPCHouse implements INPCHouse{

	@Override
	public List<BlockPos> getBeds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockPos getWorkPos() {
		return null;
	}

	@Override
	public List<UUID> residents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addResident(EntityNPCBase npc) {
		// TODO Auto-generated method stub
		return false;
	}

}
