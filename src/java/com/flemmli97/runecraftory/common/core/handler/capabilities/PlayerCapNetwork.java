package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerCapNetwork implements IStorage<IPlayer>{
	
	@Override
	public NBTBase writeNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		instance.writeToNBT(compound, false);
		return compound;
	}

	@Override
	public void readNBT(Capability<IPlayer> capability, IPlayer instance, EnumFacing side, NBTBase nbt) {
		instance.readFromNBT((NBTTagCompound) nbt);
	}
}
