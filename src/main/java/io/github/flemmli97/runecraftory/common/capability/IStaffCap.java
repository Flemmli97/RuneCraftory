package io.github.flemmli97.runecraftory.common.capability;

import io.github.flemmli97.runecraftory.api.Spell;
import net.minecraft.nbt.CompoundNBT;

public interface IStaffCap {

    Spell getTier1Spell();

    Spell getTier2Spell();

    Spell getTier3Spell();

    void setTier1Spell(Spell spell);

    void setTier2Spell(Spell spell);

    void setTier3Spell(Spell spell);

    void readFromNBT(CompoundNBT nbt);

    CompoundNBT writeToNBT(CompoundNBT nbt);
}
