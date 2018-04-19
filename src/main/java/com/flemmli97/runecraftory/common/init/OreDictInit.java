package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.RuneCraftory;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class OreDictInit {
	
	public static final void init()
	{
		RuneCraftory.logger.info("Adding Oredicts");
		OreDictionary.registerOre("ingotIron", new ItemStack(ModItems.mineral, 1, 0));
		OreDictionary.registerOre("ingotBronze", new ItemStack(ModItems.mineral, 1, 1));
		OreDictionary.registerOre("ingotSilver", new ItemStack(ModItems.mineral, 1, 2));
		OreDictionary.registerOre("ingotGold", new ItemStack(ModItems.mineral, 1, 3));
		OreDictionary.registerOre("ingotPlatinum", new ItemStack(ModItems.mineral, 1, 4));
		OreDictionary.registerOre("gemAmethyst", new ItemStack(ModItems.jewel, 1, 0));
		OreDictionary.registerOre("gemAquamarine", new ItemStack(ModItems.jewel, 1, 1));
		OreDictionary.registerOre("gemEmerald", new ItemStack(ModItems.jewel, 1, 2));
		OreDictionary.registerOre("gemRuby", new ItemStack(ModItems.jewel, 1, 3));
		OreDictionary.registerOre("gemSapphire", new ItemStack(ModItems.jewel, 1, 4));
		OreDictionary.registerOre("gemDiamond", new ItemStack(ModItems.jewel, 1, 5));
		OreDictionary.registerOre("stickWood", new ItemStack(ModItems.sticks, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("feather", new ItemStack(ModItems.feathers, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("bone", new ItemStack(ModItems.bones, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("stone", new ItemStack(ModItems.stones, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("string", new ItemStack(ModItems.strings, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("gunpowder", new ItemStack(ModItems.powders, 1, 5));

	}

}
