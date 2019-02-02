package com.flemmli97.runecraftory.common.items.itemblocks;

import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemDungeonSeed extends ItemCropSeed{

	private Block dungeonBlock;

	public ItemDungeonSeed() {
		super("dungeon", "");
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		if(this.dungeonBlock==null)
			this.dungeonBlock=ForgeRegistries.BLOCKS.getValue(new ResourceLocation(LibReference.MODID, LibOreDictionary.DUNGEON));
        return this.dungeonBlock.getDefaultState();
	}
}
