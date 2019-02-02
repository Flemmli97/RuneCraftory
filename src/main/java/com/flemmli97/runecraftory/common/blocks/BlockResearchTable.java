package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.blocks.tile.TileResearchTable;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockResearchTable extends BlockContainer{

	public BlockResearchTable() {
		super(Material.WOOD);
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.WOOD;
        this.setResistance(30.0F);
        this.setHardness(2.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "research_table"));
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
    		player.openGui(RuneCraftory.instance, LibReference.guiResearch, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
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
	public void breakBlock(World world, BlockPos pos, IBlockState blockstate) {
	    TileResearchTable te = (TileResearchTable) world.getTileEntity(pos);
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
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileResearchTable();
	}
}
