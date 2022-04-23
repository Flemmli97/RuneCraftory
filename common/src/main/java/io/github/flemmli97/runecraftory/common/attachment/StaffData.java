package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.Spell;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class StaffData {

    private Spell tier1, tier2, tier3;

    public Spell getTier1Spell() {
        return this.tier1;
    }

    public Spell getTier2Spell() {
        return this.tier2;
    }

    public Spell getTier3Spell() {
        return this.tier3;
    }

    public void setTier1Spell(Spell spell) {
        this.tier1 = spell;
    }

    public void setTier2Spell(Spell spell) {
        this.tier2 = spell;
    }

    public void setTier3Spell(Spell spell) {
        this.tier3 = spell;
    }

    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains("Tier_1"))
            this.tier1 = ModSpells.SPELLREGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_1")));
        if (nbt.contains("Tier_2"))
            this.tier2 = ModSpells.SPELLREGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_2")));
        if (nbt.contains("Tier_3"))
            this.tier3 = ModSpells.SPELLREGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_3")));
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (this.tier1 != null)
            nbt.putString("Tier_1", this.tier1.getRegistryName().toString());
        if (this.tier2 != null)
            nbt.putString("Tier_2", this.tier2.getRegistryName().toString());
        if (this.tier3 != null)
            nbt.putString("Tier_3", this.tier3.getRegistryName().toString());
        return nbt;
    }
}
