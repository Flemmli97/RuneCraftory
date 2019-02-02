package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class PlayerCapProvider implements ICapabilitySerializable<NBTBase>
{
    @CapabilityInject(IPlayer.class)
    public static final Capability<IPlayer> PlayerCap = null;
    
    private IPlayer instancePlayer = PlayerCapProvider.PlayerCap.getDefaultInstance();
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == PlayerCapProvider.PlayerCap;
    }
    
    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == PlayerCapProvider.PlayerCap) {
            return PlayerCapProvider.PlayerCap.cast(this.instancePlayer);
        }
        return null;
    }
    
    @Override
    public NBTBase serializeNBT() {
        return PlayerCapProvider.PlayerCap.getStorage().writeNBT(PlayerCapProvider.PlayerCap, this.instancePlayer, null);
    }
    
    @Override
    public void deserializeNBT(NBTBase nbt) {
        PlayerCapProvider.PlayerCap.getStorage().readNBT(PlayerCapProvider.PlayerCap, this.instancePlayer, null, nbt);
    }
}

