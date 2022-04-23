package io.github.flemmli97.runecraftory.forge.capability;

import io.github.flemmli97.runecraftory.common.attachment.StaffData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class StaffCap extends StaffData implements ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<StaffData> holder = LazyOptional.of(() -> this);

    @Override
    public CompoundTag serializeNBT() {
        return this.writeToNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return CapabilityInsts.ITEMSTACKCAP.orEmpty(cap, this.holder);
    }
}
