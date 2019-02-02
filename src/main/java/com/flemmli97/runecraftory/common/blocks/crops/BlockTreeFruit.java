package com.flemmli97.runecraftory.common.blocks.crops;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;

public class BlockTreeFruit extends Block{

	public BlockTreeFruit(String name) {
		super(Material.LEAVES);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setCreativeTab(RuneCraftory.blocks);
	}
}
