package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class CraftingRegistry {
	
	private static final void init()
	{
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
	}
	@SubscribeEvent
	public static final void registerRecipe(RegistryEvent.Register<IRecipe> event) {
		LibReference.logger.info("Registering crafting");
		init();
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "research_table"), new ItemStack(ModBlocks.research),
				new Object[] {"WPW", "IBI", "WPW", 
				'W', "plankWood",
				'I', "ingotIron",
				'B', new ItemStack(Items.BREAD),
				'P', "paper"}).setRegistryName(new ResourceLocation(LibReference.MODID, "research_table")));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "forge"), new ItemStack(ModItems.itemBlockForge),
				new Object[] {"BBB", "SLS", "WWW", 
				'W', "plankWood",
				'L', new ItemStack(Items.LAVA_BUCKET),
				'B', "blockIron",
				'S', "stone"}).setRegistryName(new ResourceLocation(LibReference.MODID, "forge")));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "cooking"), new ItemStack(ModItems.itemBlockCooking),
				new Object[] {"CCC", "BFB", "BBB", 
				'C', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 9),
				'B', new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, 15),
				'F', new ItemStack(Blocks.FURNACE)}).setRegistryName(new ResourceLocation(LibReference.MODID, "cooking")));
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "accessory"), new ItemStack(ModItems.itemBlockAccess),
				new Object[] {"CCC", "WBW", "WWW", 
				'C', new ItemStack(ModItems.cloth, 1, OreDictionary.WILDCARD_VALUE),
				'B', "workbench",
				'W', "plankWood"}).setRegistryName(new ResourceLocation(LibReference.MODID, "accessory")));
		/*event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "pharmacy"), new ItemStack(ModItems.itemBlockPharm),
				new Object[] {"CCC", "WBW", "WWW", 
				'C', new ItemStack(ModItems.cloth, 1, OreDictionary.WILDCARD_VALUE),
				'B', "workbench",
				'F', "plankWood"}).setRegistryName(new ResourceLocation(LibReference.MODID, "pharmacy")));*/
		LibReference.logger.info("Finished crafting-registry");
	}
}
