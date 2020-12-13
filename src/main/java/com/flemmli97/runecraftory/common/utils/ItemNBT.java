package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.ItemStat;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.Map;

public class ItemNBT {

    public static boolean initNBT(ItemStack stack) {
        return initNBT(stack, false);
    }

    public static boolean initNBT(ItemStack stack, boolean forced) {
        ItemStat stat = null;//ItemStatMap.get(stack);
        if (stat != null || forced)
        {
            CompoundNBT stackTag = stack.getOrCreateTag();
            CompoundNBT compound = new CompoundNBT();
            compound.putInt("ItemLevel", 1);
            if (stack.getItem() instanceof IItemWearable)
            {
                compound.put("Upgrades", new ListNBT());
                if(stat!=null)
                {
                    CompoundNBT stats = new CompoundNBT();
                    for (Map.Entry<Attribute, Integer> entry : stat.itemStats().entrySet())
                    {
                        stats.putInt(entry.getKey().getRegistryName().toString(), entry.getValue());
                    }
                    compound.put("ItemStats", stats);
                    if (stack.getItem() instanceof IItemUsable)
                    {
                        compound.putString("Element", stat.element().getName());
                    }
                }
            }
            stackTag.put(RuneCraftory.MODID, compound);
            stack.setTag(stackTag);
            return true;
        }
        return false;
    }
}
