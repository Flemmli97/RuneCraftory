package com.flemmli97.runecraftory.common.core.handler.crafting;

import java.util.List;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.google.common.collect.Lists;

import net.minecraft.item.ItemStack;

public class CraftingHandler {
	
	private static final List<RecipeSextuple>  recipesForge =  Lists.<RecipeSextuple>newArrayList();
	private static final List<RecipeSextuple>  recipesArmor =  Lists.<RecipeSextuple>newArrayList();
	private static final List<RecipeSextuple>  recipesPharma =  Lists.<RecipeSextuple>newArrayList();
	private static final List<RecipeSextuple>  recipesCooking =  Lists.<RecipeSextuple>newArrayList();

	
	public static void addRecipe(EnumCrafting type, RecipeSextuple recipe)
	{
		switch(type)
		{
		case ARMOR:
			recipesArmor.add(recipe);
			break;
		case COOKING:
			recipesCooking.add(recipe);
			break;
		case FORGE:
			recipesForge.add(recipe);
			break;
		case PHARMA:
			recipesPharma.add(recipe);
			break;	
		}
	}
	
	public static ItemStack getRecipeItemFromItems(EnumCrafting type, ItemStack[] input)
	{		
		switch(type)
		{
		case ARMOR:
			 for (RecipeSextuple irecipe : recipesArmor)
		        {
		            if (irecipe.doesRecipeMatch(input))
		            {
		                return irecipe.getCraftingResult();
		            }
		        }
			break;
		case COOKING:
			 for (RecipeSextuple irecipe : recipesCooking)
		        {
		            if (irecipe.doesRecipeMatch(input))
		            {
		                return irecipe.getCraftingResult();
		            }
		        }
			break;
		case FORGE:
			 for (RecipeSextuple irecipe : recipesForge)
		        {
		            if (irecipe.doesRecipeMatch(input))
		            {
		                return irecipe.getCraftingResult();
		            }
		        }
			break;
		case PHARMA:
			 for (RecipeSextuple irecipe : recipesPharma)
		        {
		            if (irecipe.doesRecipeMatch(input))
		            {
		                return irecipe.getCraftingResult();
		            }
		        }
			break;
		
		}
		return null;
	}

}
