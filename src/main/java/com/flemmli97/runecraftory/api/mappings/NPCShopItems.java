package com.flemmli97.runecraftory.api.mappings;

import java.util.List;
import java.util.Set;

import com.flemmli97.runecraftory.common.lib.enums.EnumShop;
import com.flemmli97.tenshilib.api.config.ExtendedItemStackWrapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class NPCShopItems
{
    private static final List<ItemStack> FLOWERS = Lists.newArrayList();
    private static final List<ItemStack> GENERAL = Lists.newArrayList();
    private static final List<ItemStack> WEAPON = Lists.newArrayList();
    private static final List<ItemStack> CLINIC = Lists.newArrayList();
    private static final List<ItemStack> MAGIC = Lists.newArrayList();
    private static final List<ItemStack> RUNES = Lists.newArrayList();
    private static final List<ItemStack> FOOD = Lists.newArrayList();
    private static final List<ItemStack> RANDOM = Lists.newArrayList();
    private static final Set<ExtendedItemStackWrapper> starterItems = Sets.newHashSet();
    private static final Set<ExtendedItemStackWrapper> leveledItems = Sets.newHashSet();

    public static List<ItemStack> allItems(Item[] items) 
    {
        NonNullList<ItemStack> list = NonNullList.create();
        for (Item item : items) 
            list.add(new ItemStack(item));
        return list;
    }
    
    /**
     * Adds items to appropriate shops.
     * @param stack
     * @param shop
     * @param starter true if a shop can sell it despite the player not sold at least one of that item
     */
    public static void addItem(ItemStack stack, EnumShop shop, boolean starter, boolean leveled) {
        switch (shop) 
        {
            case CLINIC:
                NPCShopItems.CLINIC.add(stack);
                break;
            case FLOWER:
                NPCShopItems.FLOWERS.add(stack);
                break;
            case FOOD:
                NPCShopItems.FOOD.add(stack);
                break;
            case GENERAL:
                NPCShopItems.GENERAL.add(stack);
                break;
            case MAGIC:
                NPCShopItems.MAGIC.add(stack);
                break;
            case RUNESKILL:
                NPCShopItems.RUNES.add(stack);
                break;
            case WEAPON:
                NPCShopItems.WEAPON.add(stack);
                break;
            case RANDOM:
                NPCShopItems.RANDOM.add(stack);
                break;
        }
        if(starter)
        	starterItems.add(new ExtendedItemStackWrapper(stack));
        if(leveled)
            leveledItems.add(new ExtendedItemStackWrapper(stack));
    }
    
    public static Set<ExtendedItemStackWrapper> starterItems()
    {
    	return Sets.newHashSet(starterItems);
    }
    
    public static Set<ExtendedItemStackWrapper> leveledItems()
    {
        return Sets.newHashSet(leveledItems);
    }
    
    public static List<ItemStack> getShopList(EnumShop profession) 
    {
        switch (profession) 
        {
            case CLINIC: return Lists.newArrayList(NPCShopItems.CLINIC);
            case FLOWER: return Lists.newArrayList(NPCShopItems.FLOWERS);
            case FOOD: return Lists.newArrayList(NPCShopItems.FOOD);
            case GENERAL: return Lists.newArrayList(NPCShopItems.GENERAL);
            case MAGIC: return Lists.newArrayList(NPCShopItems.MAGIC);
            case RANDOM: return Lists.newArrayList(NPCShopItems.RANDOM);
            case RUNESKILL: return Lists.newArrayList(NPCShopItems.RUNES);
            case WEAPON: return Lists.newArrayList(NPCShopItems.WEAPON);
            default: return Lists.newArrayList(NPCShopItems.GENERAL);
        }
    }
}
