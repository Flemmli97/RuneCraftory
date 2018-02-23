package com.flemmli97.runecraftory.common.core.handler.crafting;

import java.util.Collection;

import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class CraftingHandler {
	
	//OLD
	//private static final Map<String, RecipeSextuple> recipesForge =  new LinkedHashMap<String, RecipeSextuple>();
	//private static final Map<String, RecipeSextuple> recipesArmor =  new LinkedHashMap<String, RecipeSextuple>();
	//private static final Map<String, RecipeSextuple> recipesPharma =  new LinkedHashMap<String, RecipeSextuple>();
	//private static final Map<String, RecipeSextuple> recipesCooking =  new LinkedHashMap<String, RecipeSextuple>();

	private static Multimap<String, RecipeSextuple> recipesForge = ArrayListMultimap.create();
	private static Multimap<String, RecipeSextuple> recipesArmor = ArrayListMultimap.create();
	private static Multimap<String, RecipeSextuple> recipesPharma = ArrayListMultimap.create();
	private static Multimap<String, RecipeSextuple> recipesCooking = ArrayListMultimap.create();

	public static void addRecipe(EnumCrafting type,String recipeID, RecipeSextuple recipe)
	{
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
}
