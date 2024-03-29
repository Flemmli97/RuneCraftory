package io.github.flemmli97.runecraftory.forge.capability;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class PlayerCap extends PlayerData implements ICapabilitySerializable<CompoundTag> {

    private final LazyOptional<PlayerData> holder = LazyOptional.of(() -> this);

    @Override
    public CompoundTag serializeNBT() {
        return this.writeToNBTPlain(new CompoundTag());
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.readFromNBT(nbt, null);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        return CapabilityInsts.PLAYER_CAP.orEmpty(cap, this.holder);
    }
}