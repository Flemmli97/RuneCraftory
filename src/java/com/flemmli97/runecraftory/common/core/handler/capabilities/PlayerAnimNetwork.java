package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerAnimNetwork implements IStorage<IPlayerAnim>{
	
	@Override
	public NBTBase writeNBT(Capability<IPlayerAnim> capability, IPlayerAnim instance, EnumFacing side) {
		NBTTagCompound compound = new NBTTagCompound();
		return compound;
	}

	@Override
	public void readNBT(Capability<IPlayerAnim> capability, IPlayerAnim instance, EnumFacing side, NBTBase nbt) {
	}
}
