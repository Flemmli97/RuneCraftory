package io.github.flemmli97.runecraftory.common.items.consumables;

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
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 5;
    }
}
