package io.github.flemmli97.runecraftory.common.attachment;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.api.registry.Spell;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModAttributes;
import io.github.flemmli97.runecraftory.common.registry.ModSpells;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class StaffData {

    private Spell tier1, tier2, tier3;
    private int chargeTime;

    public Spell getTier1Spell(ItemStack stack) {
        return this.tier1 != null ? this.tier1 : DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getTier1Spell).orElse(null);
    }

    public void setTier1Spell(Spell spell) {
        this.tier1 = spell;
        if (spell != null)
            this.chargeTime = spell.coolDown();
    }

    public Spell getTier2Spell(ItemStack stack) {
        return this.tier2 != null ? this.tier2 : DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getTier2Spell).orElse(null);
    }

    public void setTier2Spell(Spell spell) {
        this.tier2 = spell;
        if (spell != null && this.tier1 == null)
            this.chargeTime = spell.coolDown();
    }

    public Spell getTier3Spell(ItemStack stack) {
        return this.tier3 != null ? this.tier3 : DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getTier3Spell).orElse(null);
    }

    public void setTier3Spell(Spell spell) {
        this.tier3 = spell;
        if (spell != null && this.tier1 == null && this.tier2 == null)
            this.chargeTime = spell.coolDown();
    }

    public int getChargeTime() {
        return this.chargeTime == 0 ? (int) ModAttributes.CHARGE_TIME.get().getDefaultValue() : this.chargeTime;
    }

    public void readFromNBT(CompoundTag nbt) {
        if (nbt.contains("Tier_1"))
            this.tier1 = ModSpells.SPELL_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_1")));
        if (nbt.contains("Tier_2"))
            this.tier2 = ModSpells.SPELL_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_2")));
        if (nbt.contains("Tier_3"))
            this.tier3 = ModSpells.SPELL_REGISTRY.get().getFromId(new ResourceLocation(nbt.getString("Tier_3")));
        this.chargeTime = nbt.getInt("ChargeTime");
    }

    public CompoundTag writeToNBT(CompoundTag nbt) {
        if (this.tier1 != null)
            nbt.putString("Tier_1", this.tier1.getRegistryName().toString());
        if (this.tier2 != null)
            nbt.putString("Tier_2", this.tier2.getRegistryName().toString());
        if (this.tier3 != null)
            nbt.putString("Tier_3", this.tier3.getRegistryName().toString());
        nbt.putInt("ChargeTime", this.chargeTime);
        return nbt;
    }
}
