package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.blocks.BlockAccessoryCrafter;
import com.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockCookingBench;
import com.flemmli97.runecraftory.common.blocks.BlockForge;
import com.flemmli97.runecraftory.common.blocks.BlockIgnore;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.blocks.BlockPharmacy;
import com.flemmli97.runecraftory.common.blocks.BlockRFFarmland;
import com.flemmli97.runecraftory.common.blocks.BlockRequestBoard;
import com.flemmli97.runecraftory.common.blocks.BlockResearchTable;
import com.flemmli97.runecraftory.common.blocks.BlockShippingBin;
import com.flemmli97.runecraftory.common.blocks.crops.BlockCropBase;
import com.flemmli97.runecraftory.common.blocks.tile.TileAccessory;
import com.flemmli97.runecraftory.common.blocks.tile.TileBossSpawner;
import com.flemmli97.runecraftory.common.blocks.tile.TileBrokenOre;
import com.flemmli97.runecraftory.common.blocks.tile.TileChem;
import com.flemmli97.runecraftory.common.blocks.tile.TileCooking;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrop;
import com.flemmli97.runecraftory.common.blocks.tile.TileFarmland;
import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;
import com.flemmli97.runecraftory.common.fluids.blocks.BlockHotSpring;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockWithMeta;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModBlocks {
	
	public static final Block forge = new BlockForge();
	public static final Block cooking = new BlockCookingBench();
	public static final Block pharm = new BlockPharmacy();
	public static final Block accessory = new BlockAccessoryCrafter();
	public static final Block farmland = new BlockRFFarmland();
	public static final Block mineral = new BlockMineral();
	public static final Block brokenMineral = new BlockBrokenMineral();
	public static final Block bossSpawner = new BlockBossSpawner();
	public static final Block research = new BlockResearchTable();
	public static final Block ignore = new BlockIgnore();
	public static final Block board = new BlockRequestBoard();
	public static final Block shipping = new BlockShippingBin();

	public static final Block hotSpring = new BlockHotSpring();

	//Crops
	public static final Block turnip = new BlockCropBase("turnip", LibOreDictionary.TURNIP);
	public static final Block turnipPink = new BlockCropBase("turnip_pink", LibOreDictionary.PINKTURNIP);
	public static final Block cabbage = new BlockCropBase("cabbage", LibOreDictionary.CABBAGE);
	public static final Block pinkMelon = new BlockCropBase("pink_melon", LibOreDictionary.PINKMELON);

	@SubscribeEvent
	public static final void registerBlocks(RegistryEvent.Register<Block> event) {
	    event.getRegistry().register(farmland);
	    event.getRegistry().register(forge);
	    event.getRegistry().register(accessory);
	    event.getRegistry().register(cooking);
	    event.getRegistry().register(pharm);
	    event.getRegistry().register(mineral);
	    event.getRegistry().register(brokenMineral);
	    event.getRegistry().register(bossSpawner);
	    event.getRegistry().register(research);
	    event.getRegistry().register(ignore);
	    event.getRegistry().register(board);
	    event.getRegistry().register(shipping);
	    
        event.getRegistry().register(hotSpring);

  		GameRegistry.registerTileEntity(TileBrokenOre.class, LibReference.MODID+"tile_broken_ore");
  		GameRegistry.registerTileEntity(TileFarmland.class, LibReference.MODID+"tile_farmland");
  		GameRegistry.registerTileEntity(TileCrop.class, LibReference.MODID+"tile_crop");
  		GameRegistry.registerTileEntity(TileBossSpawner.class, LibReference.MODID+"tile_boss_spawner");
  		GameRegistry.registerTileEntity(TileForge.class, LibReference.MODID+"tile_forge");
  		GameRegistry.registerTileEntity(TileAccessory.class, LibReference.MODID+"tile_access");
  		GameRegistry.registerTileEntity(TileChem.class, LibReference.MODID+"tile_chemitry");
  		GameRegistry.registerTileEntity(TileCooking.class, LibReference.MODID+"tile_cooking");
  		GameRegistry.registerTileEntity(TileResearchTable.class, LibReference.MODID+"tile_research");
  		
  		event.getRegistry().register(turnip);
  		event.getRegistry().register(turnipPink);
  		event.getRegistry().register(cabbage);
  		event.getRegistry().register(pinkMelon);
  		event.getRegistry().getValue(new ResourceLocation("minecraft:farmland")).setTickRandomly(false);
	}
	
	@SubscribeEvent
    public static final void registerItemBlocks(final RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(fromBlock(farmland));
	    event.getRegistry().register(fromBlock(bossSpawner));
	    event.getRegistry().register(fromBlockMeta(mineral));
	    event.getRegistry().register(fromBlockMeta(brokenMineral));
	    event.getRegistry().register(fromBlock(research));
	    event.getRegistry().register(fromBlock(ignore));
	    event.getRegistry().register(fromBlock(board));
	    event.getRegistry().register(fromBlock(shipping));
	}
	
	private static final ItemBlock fromBlock(Block block)
	{
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
	private static final ItemBlock fromBlockMeta(Block block)
	{
		ItemBlockWithMeta item = new ItemBlockWithMeta(block);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static final void initModel(ModelRegistryEvent event)
	{
		((BlockRFFarmland) farmland).initModel();
		((BlockBossSpawner) bossSpawner).initModel();
		((BlockMineral) mineral).initModel();
		((BlockBrokenMineral) brokenMineral).initModel();
		((BlockResearchTable) research).initModel();
		((BlockIgnore) ignore).initModel();
		((BlockRequestBoard) board).initModel();
	}
}
