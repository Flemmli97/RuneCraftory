package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;

import net.minecraft.item.ItemStack;

public class CraftingRegistry {
	
	public static void init()
	{
		RuneCraftory.logger.info("Registering crafting");
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(new ItemStack(ModItems.broadSword), "ingotIron","ingotIron"));
	}

}
