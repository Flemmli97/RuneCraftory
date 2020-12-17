package com.flemmli97.runecraftory.common.loot;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;

public class LuckLootFunction extends LootFunction {

    protected LuckLootFunction(ILootCondition[] p_i51231_1_) {
        super(p_i51231_1_);
    }

    @Override
    protected ItemStack doApply(ItemStack p_215859_1_, LootContext p_215859_2_) {
        return null;
    }

    @Override
    public LootFunctionType getType() {
        return null;
    }
}
