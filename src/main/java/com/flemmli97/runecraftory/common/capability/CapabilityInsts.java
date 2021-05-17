package com.flemmli97.runecraftory.common.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CapabilityInsts {

    @CapabilityInject(IPlayerCap.class)
    public static final Capability<IPlayerCap> PlayerCap = null;
    @CapabilityInject(IStaffCap.class)
    public static final Capability<IStaffCap> StaffCap = null;
    @CapabilityInject(IEntityCap.class)
    public static final Capability<IEntityCap> EntityCap = null;

    public static class PlayerCapNetwork implements Capability.IStorage<IPlayerCap> {

        @Override
        public INBT writeNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side) {
            return instance.writeToNBT(new CompoundNBT(), null);
        }

        @Override
        public void readNBT(Capability<IPlayerCap> capability, IPlayerCap instance, Direction side, INBT nbt) {
            instance.readFromNBT((CompoundNBT) nbt, null);
        }
    }

    public static class StaffCapNetwork implements Capability.IStorage<IStaffCap> {

        @Override
        public INBT writeNBT(Capability<IStaffCap> capability, IStaffCap instance, Direction side) {
            return instance.writeToNBT(new CompoundNBT());
        }

        @Override
        public void readNBT(Capability<IStaffCap> capability, IStaffCap instance, Direction side, INBT nbt) {
            instance.readFromNBT((CompoundNBT) nbt);
        }
    }

    public static class EntityCapNetwork implements Capability.IStorage<IEntityCap> {

        @Override
        public INBT writeNBT(Capability<IEntityCap> capability, IEntityCap instance, Direction side) {
            return null;
        }

        @Override
        public void readNBT(Capability<IEntityCap> capability, IEntityCap instance, Direction side, INBT nbt) {

        }
    }

}
