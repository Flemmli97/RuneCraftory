package com.flemmli97.runecraftory.common.init;

import com.flemmli97.runecraftory.common.blocks.BlockForge;
import com.flemmli97.runecraftory.common.blocks.BlockRFFarmland;
import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = RFReference.MODID)
public class ModBlocks {
	
	public static Block forge = new BlockForge();
	public static Block farmland = new BlockRFFarmland();

	@SubscribeEvent
	public static final void registerBlocks(RegistryEvent.Register<Block> event) {
	    event.getRegistry().register(farmland);
	    event.getRegistry().register(forge);
	}
	
	@SubscribeEvent
    public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
	    event.getRegistry().register(fromBlock(farmland));
	}
	
	private static ItemBlock fromBlock(Block block)
	{
		ItemBlock item = new ItemBlock(farmland);
		item.setRegistryName(block.getRegistryName());
		return item;
	}
	
	@SubscribeEvent
	@SideOnly(value = Side.CLIENT)
	public static void initModel(ModelRegistryEvent event)
	{
		((BlockRFFarmland) farmland).initModel();
	}
}
