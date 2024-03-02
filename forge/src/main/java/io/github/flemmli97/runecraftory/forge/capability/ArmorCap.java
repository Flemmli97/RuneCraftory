package io.github.flemmli97.runecraftory.forge.capability;

import io.github.flemmli97.runecraftory.common.attachment.ArmorEffectData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArmorCap extends ArmorEffectData implements ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<ArmorEffectData> holder = LazyOptional.of(() -> this);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CapabilityInsts.ARMOR_ITEM_CAP.orEmpty(cap, this.holder);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.writeToNBT(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.readFromNBT(nbt);
    }
}
