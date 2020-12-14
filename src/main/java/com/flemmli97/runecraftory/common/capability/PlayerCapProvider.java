package com.flemmli97.runecraftory.common.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapProvider implements ICapabilitySerializable<INBT> {

    @CapabilityInject(IPlayerCap.class)
    public static final Capability<IPlayerCap> PlayerCap = null;

    private final LazyOptional<IPlayerCap> instance = LazyOptional.of(PlayerCap::getDefaultInstance);

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return PlayerCap.orEmpty(cap, this.instance);
    }

    @Override
    public INBT serializeNBT() {
        return PlayerCap.getStorage().writeNBT(PlayerCap, this.instance.orElseThrow(() -> new NullPointerException("Something went wrong serializing capabilities")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        PlayerCap.getStorage().readNBT(PlayerCap, this.instance.orElseThrow(() -> new NullPointerException("Something went wrong deserializing capabilities")), null, nbt);
    }
}
