package io.github.flemmli97.runecraftory.common.items.consumables;

import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
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
