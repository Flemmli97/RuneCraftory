package com.flemmli97.runecraftory.common.blocks.crops;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibOreDictionary;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockDungeon extends Block{

	public BlockDungeon()
    {
		super(Material.ROCK);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, LibOreDictionary.DUNGEON));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setCreativeTab(RuneCraftory.blocks);
    }
}
