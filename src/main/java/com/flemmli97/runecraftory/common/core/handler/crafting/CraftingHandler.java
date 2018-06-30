package com.flemmli97.runecraftory.common.core.handler.crafting;

import com.flemmli97.runecraftory.common.lib.enums.*;
import java.util.*;
import com.google.common.collect.*;

public class CraftingHandler
{
    private static final Multimap<String, RecipeSextuple> recipesForge = ArrayListMultimap.create();
    private static final Multimap<String, RecipeSextuple> recipesArmor = ArrayListMultimap.create();
    private static final Multimap<String, RecipeSextuple> recipesPharma = ArrayListMultimap.create();
    private static final Multimap<String, RecipeSextuple> recipesCooking = ArrayListMultimap.create();
    
    public static void addRecipe(EnumCrafting type, RecipeSextuple recipe)
	{
		String recipeID = recipe.getCraftingOutput().getItem().getRegistryName().toString();
		switch(type)
		{
		case ARMOR:	
			recipesArmor.put(recipeID, recipe);
			break;
		case COOKING:
			recipesCooking.put(recipeID, recipe);
			break;
		case FORGE:
			recipesForge.put(recipeID, recipe);
			break;
		case PHARMA:
			recipesPharma.put(recipeID, recipe);
			break;	
		}
	}
	
	public static void removeRecipe(EnumCrafting type, String recipeName)
	{
		switch(type)
		{
		case ARMOR:	
			recipesArmor.removeAll(recipeName);
			break;
		case COOKING:
			recipesCooking.removeAll(recipeName);
			break;
		case FORGE:
			recipesForge.removeAll(recipeName);
			break;
		case PHARMA:
			recipesPharma.removeAll(recipeName);
			break;	
		}
	}
    
	public static Collection<RecipeSextuple> getRecipeFromID(EnumCrafting type, String recipeID)
	{		
		switch(type)
		{
		case ARMOR:
			return recipesArmor.get(recipeID);
		case COOKING:
			return recipesCooking.get(recipeID);
		case FORGE:
			return recipesForge.get(recipeID);
		case PHARMA:
			return recipesPharma.get(recipeID);
		}
		return null;
	}
    
	private static String randomRecipe(EnumCrafting type)
	{
		switch(type)
		{
		case ARMOR:
			return recipesArmor.keySet().toArray(new String[] {})[new Random().nextInt(recipesArmor.keySet().size())];
		case COOKING:
			return recipesCooking.keySet().toArray(new String[] {})[new Random().nextInt(recipesCooking.keySet().size())];
		case FORGE:
			return recipesForge.keySet().toArray(new String[] {})[new Random().nextInt(recipesForge.keySet().size())];
		case PHARMA:
			return recipesPharma.keySet().toArray(new String[] {})[new Random().nextInt(recipesPharma.keySet().size())];
		}
		return "";
	}
	
	public static String randomRecipeToExclude(EnumCrafting type, int levelMin, int levelMax)
	{
		String s = randomRecipe(type);
		boolean flag=false;
		switch(type)
		{
		case ARMOR:
			for(RecipeSextuple r : recipesArmor.get(s))
				if(r.getCraftingLevel()>= levelMin && r.getCraftingLevel()<=levelMax)
				{
					flag=true;
					break;
				}				
		case COOKING:
			for(RecipeSextuple r : recipesCooking.get(s))
				if(r.getCraftingLevel()>= levelMin && r.getCraftingLevel()<=levelMax)
				{
					flag=true;
					break;
				}
		case FORGE:
			for(RecipeSextuple r : recipesForge.get(s))
				if(r.getCraftingLevel()>= levelMin && r.getCraftingLevel()<=levelMax)
				{
					flag=true;
					break;
				}
		case PHARMA:
			for(RecipeSextuple r : recipesPharma.get(s))
				if(r.getCraftingLevel()>= levelMin && r.getCraftingLevel()<=levelMax)
				{
					flag=true;
					break;
				}
		}
		if(flag)
			return s;
		return randomRecipeToExclude(type, levelMin, levelMax);
	}
}
