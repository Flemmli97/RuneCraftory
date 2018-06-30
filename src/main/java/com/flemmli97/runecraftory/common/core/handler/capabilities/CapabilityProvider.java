package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class CapabilityProvider
{
    public static class PlayerCapProvider implements ICapabilitySerializable<NBTBase>
    {
        @CapabilityInject(IPlayer.class)
        public static final Capability<IPlayer> PlayerCap = null;
        @CapabilityInject(IPlayerAnim.class)
        public static final Capability<IPlayerAnim> PlayerAnim = null;
        
        private IPlayer instancePlayer = PlayerCapProvider.PlayerCap.getDefaultInstance();
        private IPlayerAnim instanceAnim = PlayerCapProvider.PlayerAnim.getDefaultInstance();
        
        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == PlayerCapProvider.PlayerCap || capability == PlayerCapProvider.PlayerAnim;
        }
        
        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            if (capability == PlayerCapProvider.PlayerCap) {
                return PlayerCapProvider.PlayerCap.cast(this.instancePlayer);
            }
            if (capability == PlayerCapProvider.PlayerAnim) {
                return PlayerCapProvider.PlayerAnim.cast(this.instanceAnim);
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
}
