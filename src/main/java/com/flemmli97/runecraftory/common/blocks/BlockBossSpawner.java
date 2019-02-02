package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileBossSpawner;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBossSpawner extends BlockContainer{

	public BlockBossSpawner() {
		super(Material.IRON);
		this.setCreativeTab(RuneCraftory.monsters);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(99999.0F);
        this.setHardness(30.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "bossSpawner"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	@Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return true;
    }
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBossSpawner(worldIn);
	}
}
