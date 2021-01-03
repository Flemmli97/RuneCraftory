package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;

public class ItemMedicine extends Item {

    public final boolean levelAffectStats;

    public ItemMedicine(boolean levelAffectStat, Item.Properties props) {
        super(props);
        this.levelAffectStats = levelAffectStat;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 3;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    public int healthRegen(ItemStack stack, FoodProperties prop) {
        if (this.levelAffectStats)
            return prop.getHPGain() + (int) (prop.getHPGain() * 0.1 * (ItemNBT.itemLevel(stack) - 1));
        return prop.getHPGain();
    }

    public int healthRegenPercent(ItemStack stack, FoodProperties prop) {
        if (prop != null) {
            if (this.levelAffectStats)
                return prop.getHpPercentGain() + (int) (prop.getHpPercentGain() * 0.1 * (ItemNBT.itemLevel(stack) - 1));
            return prop.getHpPercentGain();
        }
        return 0;
    }
}
