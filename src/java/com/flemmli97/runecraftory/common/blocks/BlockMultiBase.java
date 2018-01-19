package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMultiBase extends Block{

    public static final PropertyEnum<BlockMultiBase.EnumPartType> PART = PropertyEnum.<BlockMultiBase.EnumPartType>create("part", BlockMultiBase.EnumPartType.class);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMultiBase() {
		super(Material.IRON);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(100.0F);
        this.setHardness(3.0F);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);

        if (state.getValue(PART) == BlockMultiBase.EnumPartType.LEFT)
        {
            if (worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this)
        {
            if (!worldIn.isRemote)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }
            worldIn.setBlockToAir(pos);
        }
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {PART, FACING});
	}
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(PART) == BlockMultiBase.EnumPartType.LEFT ? Items.AIR : Items.BED;
    }
	@Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (state.getValue(PART) == BlockMultiBase.EnumPartType.RIGHT)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(ModItems.itemBlockForge));
        }
    }
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);
    }
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
		EnumFacing facing = EnumFacing.getHorizontal(meta);
		EnumPartType type = EnumPartType.LEFT;
		if(meta>4)
		{
			facing = EnumFacing.getHorizontal(meta-4);
			type = EnumPartType.RIGHT;
		}
        return this.getDefaultState().withProperty(PART, type).withProperty(FACING, facing);
    }
	@Override
    public int getMetaFromState(IBlockState state)
    {
    		int part = state.getValue(PART).getMeta();
    		int facing = state.getValue(FACING).getHorizontalIndex();
    		return facing + part*4;
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

	public static enum EnumPartType implements IStringSerializable
    {
        LEFT("left", 0),
        RIGHT("right", 1);

        private final String name;
        private final int meta;

        private EnumPartType(String name, int meta)
        {
            this.name = name;
            this.meta = meta;
        }

        public String toString()
        {
            return this.name;
        }

        public String getName()
        {
            return this.name;
        }
        
        public int getMeta()
        {
        		return this.meta;
        }
    }
}
