package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//something like an enderchest
public class BlockShippingBin extends Block{

	public BlockShippingBin() {
		super(Material.WOOD);
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.WOOD;
        this.setResistance(10.0F);
        this.setHardness(3.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "shipping_bin"));
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
			player.openGui(RuneCraftory.instance, LibReference.guiShipping, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
	}
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

}
