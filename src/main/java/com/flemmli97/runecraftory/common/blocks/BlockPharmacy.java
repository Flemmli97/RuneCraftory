package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileChem;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlockPharmacy extends BlockMultiBase{

	public BlockPharmacy() {
		super();
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "chemistry"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return meta/4==0?new TileChem():null;
	}

	@Override
	public Item drop() {
		return ModItems.itemBlockPharm;
	}
}
