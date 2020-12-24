package com.flemmli97.runecraftory.common.capability;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.Spell;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class StaffCapImpl implements IStaffCap, ICapabilitySerializable<CompoundNBT> {

    private final LazyOptional<IStaffCap> holder = LazyOptional.of(() -> this);

    private Spell tier1, tier2, tier3;

    @Override
    public Spell getTier1Spell() {
        return tier1;
    }

    @Override
    public Spell getTier2Spell() {
        return tier2;
    }

    @Override
    public Spell getTier3Spell() {
        return tier3;
    }

    @Override
    public void setTier1Spell(Spell spell) {
        tier1 = spell;
    }

    @Override
    public void setTier2Spell(Spell spell) {
        tier2 = spell;
    }

    @Override
    public void setTier3Spell(Spell spell) {
        tier3 = spell;
    }

    @Override
    public void readFromNBT(CompoundNBT nbt) {
        if(nbt.contains("Tier_1"))
            this.tier1 = RuneCraftory.spellRegistry.getValue(new ResourceLocation(nbt.getString("Tier_1")));
        if(nbt.contains("Tier_2"))
            this.tier2 = RuneCraftory.spellRegistry.getValue(new ResourceLocation(nbt.getString("Tier_2")));
        if(nbt.contains("Tier_3"))
            this.tier3 = RuneCraftory.spellRegistry.getValue(new ResourceLocation(nbt.getString("Tier_3")));
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        if(this.tier1 != null)
            nbt.putString("Tier_1", this.tier1.getRegistryName().toString());
        if(this.tier2 != null)
            nbt.putString("Tier_2", this.tier2.getRegistryName().toString());
        if(this.tier3 != null)
            nbt.putString("Tier_3", this.tier3.getRegistryName().toString());
        return nbt;
    }

    @Override
    public CompoundNBT serializeNBT() {
        return this.writeToNBT(new CompoundNBT());
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.readFromNBT(nbt);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return CapabilityInsts.StaffCap.orEmpty(cap, holder);
    }
}
