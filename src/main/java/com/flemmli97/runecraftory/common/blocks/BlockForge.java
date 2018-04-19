package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockForge extends BlockMultiBase{

	public BlockForge() {
		super();
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "forge"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	
	private AxisAlignedBB leftBox = new AxisAlignedBB(0,0,0,1,1.25,1);
	private AxisAlignedBB rightBoxNS = new AxisAlignedBB(0,0,0.25,1,0.375,0.8125);
	private AxisAlignedBB rightBoxEW = new AxisAlignedBB(0.25,0,0,0.8125,0.375,1);

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(PART)==EnumPartType.LEFT?leftBox:(state.getValue(FACING)==EnumFacing.NORTH||state.getValue(FACING)==EnumFacing.SOUTH)?rightBoxNS:rightBoxEW;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return meta/4==0?new TileForge():null;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return state.getValue(PART)==EnumPartType.LEFT;
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
