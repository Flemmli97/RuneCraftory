package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileMultiBase;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMultiBase extends BlockContainer{

    public static final PropertyEnum<BlockMultiBase.EnumPartType> PART = PropertyEnum.<BlockMultiBase.EnumPartType>create("part", BlockMultiBase.EnumPartType.class);
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public BlockMultiBase() {
		super(Material.IRON);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(100.0F);
        this.setHardness(3.0F);
	}
	
	public abstract Item drop();
	public abstract boolean hasUpgrade();

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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote)
        {
            return true;
        }
        else
        {
    		BlockPos newPos = pos;
    		if(state.getValue(PART)==EnumPartType.RIGHT)
    			newPos=newPos.offset(state.getValue(FACING).rotateY());
    		if(!player.isSneaking())
    			player.openGui(RuneCraftory.instance, LibReference.guiMaking, world, newPos.getX(), newPos.getY(), newPos.getZ());
    		else if(this.hasUpgrade())
    			player.openGui(RuneCraftory.instance, LibReference.guiUpgrade, world, newPos.getX(), newPos.getY(), newPos.getZ());
            return true;
        }
	}
	
	@Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && state.getValue(PART) == BlockMultiBase.EnumPartType.LEFT)
        {
            BlockPos blockpos = pos.offset((EnumFacing)state.getValue(FACING).rotateYCCW());

            if (worldIn.getBlockState(blockpos).getBlock() == this)
            {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {FACING, PART});
	}
    
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(PART) == BlockMultiBase.EnumPartType.LEFT ? Items.AIR : this.drop();
    }
	@Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
    {
        if (state.getValue(PART) == BlockMultiBase.EnumPartType.RIGHT)
        {
            spawnAsEntity(worldIn, pos, new ItemStack(this.drop()));
        }
    }
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    TileMultiBase te = (TileMultiBase) world.getTileEntity(pos);
	    if(te!=null)
	    {
	    	for(int x=0;x<te.getSizeInventory();x++)
		    {
	    		EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), te.getStackInSlot(x));
			    world.spawnEntity(item);
		    }
	    }
	    super.breakBlock(world, pos, blockstate);
	}
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
		EnumPartType type = EnumPartType.fromMeta(meta/4);
		EnumFacing facing = EnumFacing.getHorizontal(meta/4==0?meta:meta-4);
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
        return true;
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
        
        public static EnumPartType fromMeta(int meta)
        {
        		return values()[meta];
        }
    }
}
