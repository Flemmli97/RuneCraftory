package com.flemmli97.runecraftory.common.blocks.crops;

import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.google.common.collect.Lists;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockTreeLeaves extends BlockLeaves{

	public BlockTreeLeaves(String name)
	{
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setCreativeTab(RuneCraftory.blocks);
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Lists.newArrayList();
	}

	@Override
	public EnumType getWoodType(int meta) {
		return EnumType.OAK;
	}

}
