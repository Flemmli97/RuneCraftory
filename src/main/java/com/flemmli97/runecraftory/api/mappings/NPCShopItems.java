package com.flemmli97.runecraftory.api.mappings;

import java.util.List;

import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.enums.EnumShop;
import com.google.common.collect.Lists;

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
    
    public static List<ItemStack> allItems(Item[] items) 
    {
        NonNullList<ItemStack> list = NonNullList.create();
        for (Item item : items) 
            list.add(new ItemStack(item));
        return list;
    }
    
    public static void addItem(ItemStack stack, EnumShop shop) {
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
    
    static {
        NPCShopItems.GENERAL.addAll(allItems(ModItems.CROPSEEDS));
        NPCShopItems.GENERAL.addAll(allItems(ModItems.CROPS));
        
        NPCShopItems.WEAPON.addAll(allItems(ModItems.TOOLS));
        NPCShopItems.WEAPON.addAll(allItems(ModItems.WEAPONS));
        

        NPCShopItems.RANDOM.addAll(allItems(ModItems.MATERIALS));
    }
}
