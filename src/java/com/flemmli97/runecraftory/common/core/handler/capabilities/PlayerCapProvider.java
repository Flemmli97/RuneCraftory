package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerCapProvider implements ICapabilitySerializable<NBTBase>{

	@CapabilityInject(IPlayer.class)
    public static final Capability<IPlayer> PlayerCap = null;
	@CapabilityInject(IPlayerAnim.class)
    public static final Capability<IPlayerAnim> PlayerAnim = null;

    private IPlayer instance = PlayerCap.getDefaultInstance();
    private IPlayerAnim instanceAnim = PlayerAnim.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == PlayerCap;
	}

	@Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
		if(capability == PlayerCap)
			return PlayerCap.<T> cast(this.instance);
		else if(capability==PlayerAnim)
			return PlayerAnim.<T> cast(this.instanceAnim);
		else
			return null;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return PlayerCap.getStorage().writeNBT(PlayerCap, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
    		PlayerCap.getStorage().readNBT(PlayerCap, this.instance, null, nbt);
    }

}
