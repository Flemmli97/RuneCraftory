package com.flemmli97.runecraftory.common.core.handler.crafting;

import com.flemmli97.runecraftory.RuneCraftory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeSextuple {
	
	private final ItemStack output;
	private final Object[] ingredients = new Object[6];
	
	public RecipeSextuple(ItemStack output, Object... ing)
	{
		this.output=output; 
		boolean flag = false;
		if(ing.length<=6)
			for(int i = 0; i < ing.length; i++)
			{
				if(ing[i].getClass().equals(String.class) || ing[i].getClass().equals(ItemStack.class))
				{
					this.ingredients[i]=ing[i];
					flag=true;
				}
			}
		if(!flag)
			RuneCraftory.logger.error("Invalid recipe: length: " + ing.length + ", params: " + ing.toString() + ", class: " + ing[0].getClass());
	}
	
	public ItemStack getRecipeOutput()
    {
        return this.output;
    }
	
	public ItemStack getCraftingResult()
    {
		return this.getRecipeOutput().copy();
    }
	
	public boolean doesRecipeMatch(ItemStack[] items)
	{
		int match = 0;
		System.out.println("some length, 1: " + this.ingredients.length + " 2 :"+items.length);
		for(Object ing : this.ingredients)
		{
			if(ing!=null)
			{
				System.out.println("object " + ing + " objectclss " +  ing.getClass());
				if(ing.getClass().equals(ItemStack.class) && ing !=ItemStack.EMPTY)
					for(ItemStack stack : items)
					{
						System.out.println("stack");
						if(stack!=ItemStack.EMPTY)
							if(stack.getItem() == ((ItemStack) ing).getItem())
								match++;
					}
				else if(ing.getClass().equals(String.class))
					for(ItemStack stack : items)
					{
						System.out.println("string" + ing);
						if(stack!=ItemStack.EMPTY)
						{
							boolean flag = false;
							for(ItemStack itemStack:OreDictionary.getOres((String) ing))
							{
								if(itemStack.getItem()==stack.getItem())
								System.out.println("iron " + stack);
								match++;
								flag = true;
								break;
							}
							if(flag)
								break;
						}
					}
			}
		}
		System.out.println(items.length + " match "+ match);
		return items.length==match;
	}

}
