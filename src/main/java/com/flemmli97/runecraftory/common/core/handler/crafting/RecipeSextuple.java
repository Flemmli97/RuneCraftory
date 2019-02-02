package com.flemmli97.runecraftory.common.core.handler.crafting;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeSextuple
{
    private ItemStack result;
    private final NonNullList<Object> ingredients;
    private int level;
    
    public RecipeSextuple(int level, ItemStack result, Object... ing) 
    {
        this.level = level;
        this.result = result;
        if (ing == null) 
        {
            throw new IllegalArgumentException("Ingredients can't be null");
        }
        this.ingredients = NonNullList.withSize(ing.length, ItemStack.EMPTY);
        if (ing.length <= 6) 
        {
            for (int i = 0; i < ing.length; ++i) 
            {
                if (!ing[i].getClass().equals(String.class) && !ing[i].getClass().equals(ItemStack.class)) 
                {
                    throw new IllegalArgumentException("Invalid recipe: parameter " + ing[i]);
                }
                this.ingredients.set(i, ing[i]);
            }
        }
        else 
        {
            LibReference.logger.error("Invalid recipe: length {}", ing.length);
        }
    }
    
    public ItemStack getCraftingOutput() 
    {
        ItemStack stack = this.result.copy();
        ItemNBT.initNBT(stack);
        return stack;
    }
    
    public int getCraftingLevel() {
        return this.level;
    }
    
    public NonNullList<Object> getRecipeItems() 
    {
        return this.ingredients;
    }
    
    public boolean doesRecipeMatch(List<ItemStack> items) 
    {
        int match = 0;
        List<ItemStack> nonEmptyItems = new ArrayList<ItemStack>();
        for (ItemStack item : items) 
        {
            if (!item.isEmpty()) 
            {
                nonEmptyItems.add(item);
            }
        }
        if (nonEmptyItems.size() != this.ingredients.size()) 
        {
            return false;
        }
        for (Object ing : this.ingredients) 
        {
            if (ing.getClass().equals(ItemStack.class)) 
            {
                for (ItemStack stack : items) {
                    if (!stack.isEmpty() && OreDictionary.itemMatches((ItemStack)ing, stack, false)) 
                    {
                        ++match;
                        break;
                    }
                }
            }
            else if (ing.getClass().equals(String.class)) 
            {
                for (ItemStack stack : items) 
                {
                    boolean flag = false;
                    if (stack != ItemStack.EMPTY) 
                    {
                        for (ItemStack itemStack : OreDictionary.getOres((String)ing)) 
                        {
                            if (OreDictionary.itemMatches(itemStack, stack, false)) {
                                ++match;
                                flag = true;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            }
        }
        return this.ingredients.size() == match;
    }
    
    public static class Sort implements Comparator<RecipeSextuple>
    {
        @Override
        public int compare(RecipeSextuple o1, RecipeSextuple o2) {
            return Integer.compare(o1.level, o2.level);
        }
    }
}
