package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingRegistry {
	
	public static void init()
	{
		RuneCraftory.logger.info("Registering crafting");
		//Forge recipes
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.dagger), "ingotIron", "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.dagger), "ingotGold", "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.dagger), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.leatherGlove), "leather"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.leatherGlove), new ItemStack(ModItems.furs, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(5,new ItemStack(ModItems.hoeScrap), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(15,new ItemStack(ModItems.hoeIron), "ingotBronze"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(30,new ItemStack(ModItems.hoeSilver), "ingotSilver"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(50,new ItemStack(ModItems.hoeGold), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(80,new ItemStack(ModItems.hoePlatinum), "ingotPlatinum"));
		RuneCraftory.logger.info("Finished crafting-registry");

	}

}
