package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockForge extends BlockMultiBase{

	public BlockForge() {
		super();
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "forge"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return meta/4==0?new TileForge():null;
	}

	@Override
	public Item drop() {
		return ModItems.itemBlockForge;
	}

	@Override
	public boolean hasUpgrade() {
		return true;
	}
}
