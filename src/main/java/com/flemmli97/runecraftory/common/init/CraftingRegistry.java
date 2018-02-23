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
		CraftingHandler.addRecipe(EnumCrafting.FORGE, "broadSword", new RecipeSextuple(2,new ItemStack(ModItems.broadSword), "ingotIron"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, "broadSword", new RecipeSextuple(2,new ItemStack(ModItems.broadSword), "ingotGold"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, "broadSword", new RecipeSextuple(2,new ItemStack(ModItems.broadSword), new ItemStack(ModItems.mineral, 1, OreDictionary.WILDCARD_VALUE)));
	}

}
