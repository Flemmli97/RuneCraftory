package com.flemmli97.runecraftory.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerCapNetwork implements Capability.IStorage<IPlayerCap> {

    @Override
    public INBT writeNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side) {
        return instance.writeToNBT(new CompoundNBT(), null);
    }

    @Override
    public void readNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side, INBT nbt) {
        instance.readFromNBT((CompoundNBT) nbt, null);
    }
}
