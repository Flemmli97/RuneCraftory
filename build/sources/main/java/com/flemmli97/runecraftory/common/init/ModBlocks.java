package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.blocks.BlockAccessoryCrafter;
import com.flemmli97.runecraftory.common.blocks.BlockBossSpawner;
import com.flemmli97.runecraftory.common.blocks.BlockBrokenMineral;
import com.flemmli97.runecraftory.common.blocks.BlockCookingBench;
import com.flemmli97.runecraftory.common.blocks.BlockForge;
import com.flemmli97.runecraftory.common.blocks.BlockMineral;
import com.flemmli97.runecraftory.common.blocks.BlockPharmacy;
import com.flemmli97.runecraftory.common.blocks.BlockRFFarmland;
import com.flemmli97.runecraftory.common.blocks.BlockResearchTable;
import com.flemmli97.runecraftory.common.blocks.BlockTileTest;
import com.flemmli97.runecraftory.common.blocks.tile.TileAccessory;
import com.flemmli97.runecraftory.common.blocks.tile.TileBossSpawner;
import com.flemmli97.runecraftory.common.blocks.tile.TileBrokenOre;
import com.flemmli97.runecraftory.common.blocks.tile.TileChem;
import com.flemmli97.runecraftory.common.blocks.tile.TileCooking;
import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;
import com.flemmli97.runecraftory.common.items.itemblocks.ItemBlockWithMeta;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = LibReference.MODID)
public class ModBlocks {
	
	public static Block forge = new BlockForge();
	public static Block cooking = new BlockCookingBench();
	public static Block pharm = new BlockPharmacy();
	public static Block accessory = new BlockAccessoryCrafter();
	public static Block farmland = new BlockRFFarmland();
	public static Block mineral = new BlockMineral();
	public static Block brokenMineral = new BlockBrokenMineral();
	public static Block bossSpawner = new BlockBossSpawner();
	public static Block test = new BlockTileTest();
	public static Block research = new BlockResearchTable();

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
	    event.getRegistry().register(test);
	    event.getRegistry().register(research);
  		GameRegistry.registerTileEntity(TileBrokenOre.class, "tile_broken_ore");
  		GameRegistry.registerTileEntity(TileBossSpawner.class, "tile_boss_spawner");
  		GameRegistry.registerTileEntity(TileForge.class, "tile_forge");
  		GameRegistry.registerTileEntity(TileAccessory.class, "tile_access");
  		GameRegistry.registerTileEntity(TileChem.class, "tile_chemitry");
  		GameRegistry.registerTileEntity(TileCooking.class, "tile_cooking");
  		GameRegistry.registerTileEntity(TileResearchTable.class, "tile_research");
	}
	
	@SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(fromBlock(farmland));
	    event.getRegistry().register(fromBlock(bossSpawner));
	    event.getRegistry().register(fromBlockMeta(mineral));
	    event.getRegistry().register(fromBlockMeta(brokenMineral));
	    event.getRegistry().register(fromBlock(test));
	    event.getRegistry().register(fromBlock(research));
	}
	
	private static ItemBlock fromBlock(Block block)
	{
		ItemBlock item = new ItemBlock(block);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
	private static ItemBlock fromBlockMeta(Block block)
	{
		ItemBlockWithMeta item = new ItemBlockWithMeta(block);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static void initModel(ModelRegistryEvent event)
	{
		((BlockRFFarmland) farmland).initModel();
		((BlockBossSpawner) bossSpawner).initModel();
		((BlockMineral) mineral).initModel();
		((BlockBrokenMineral) brokenMineral).initModel();
		((BlockResearchTable) research).initModel();
	}
}
