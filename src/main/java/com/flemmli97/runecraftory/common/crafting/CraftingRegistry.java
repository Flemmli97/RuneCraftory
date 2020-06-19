package com.flemmli97.runecraftory.common.crafting;

import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
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
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class CraftingRegistry {
	
	private static final void init()
	{
		//Forge recipes
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), LibOreDictionary.IRON));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.broadSword), LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), LibOreDictionary.IRON));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.claymore), LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), LibOreDictionary.IRON));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.battleHammer), LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), LibOreDictionary.IRON));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.spear), LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.shortDagger), LibOreDictionary.IRON, LibOreDictionary.IRON));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.shortDagger), LibOreDictionary.GOLD, LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.shortDagger), LibOreDictionary.MINERALS, LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.leatherGlove), "leather"));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(3,new ItemStack(ModItems.leatherGlove), LibOreDictionary.FURS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(5,new ItemStack(ModItems.hoeScrap), LibOreDictionary.MINERALS));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(15,new ItemStack(ModItems.hoeIron), LibOreDictionary.BRONZE));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(30,new ItemStack(ModItems.hoeSilver), LibOreDictionary.SILVER));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(50,new ItemStack(ModItems.hoeGold), LibOreDictionary.GOLD));
		CraftingHandler.addRecipe(EnumCrafting.FORGE, new RecipeSextuple(80,new ItemStack(ModItems.hoePlatinum), LibOreDictionary.PLATINUM));
	}
	@SubscribeEvent
	public static final void registerRecipe(RegistryEvent.Register<IRecipe> event) {
		LibReference.logger.info("Registering crafting");
		init();
		event.getRegistry().register(new ShapedOreRecipe(new ResourceLocation(LibReference.MODID, "research_table"), new ItemStack(ModBlocks.research),
				new Object[] {"WPW", "IBI", "WPW", 
				'W', "plankWood",
				'I', LibOreDictionary.IRON,
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
				'C', LibOreDictionary.CLOTHS,
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
