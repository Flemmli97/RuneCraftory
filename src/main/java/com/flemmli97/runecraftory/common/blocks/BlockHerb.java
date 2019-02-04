package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHerb extends BlockBush{

    private String item;
	    
	public BlockHerb(String name, String drop) {
		super(Material.PLANTS);
        this.blockSoundType = SoundType.PLANT;
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.item=drop;
	}
	
	public Item drop()
	{
		return CropMap.cropFromString(this.item);
	}
    
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return this.drop();
    }
	
	@Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {        
		Random random = new Random();
        drops.add(ItemNBT.getLeveledItem(new ItemStack(this.drop()), MathHelper.clamp(random.nextInt(5)+random.nextInt(4)+random.nextInt(3)+random.nextInt(2), 1, 10)));
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
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
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this.drop(), 1);
    }
	
	@Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos)
    {
		return EnumPlantType.Plains;
    }
}
