package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileForge;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTileTest extends BlockContainer{

    public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockTileTest() {
		super(Material.ROCK);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "test"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
        {
            return true;
        }
        else
        {
        		if(player.isSneaking())
        		player.openGui(RuneCraftory.instance, LibReference.guiUpgrade, world, pos.getX(), pos.getY(), pos.getZ());
        		else
        			player.openGui(RuneCraftory.instance, LibReference.guiMaking, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileForge();
	}
}
