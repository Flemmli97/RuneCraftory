package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.common.blocks.tile.TileBrokenOre;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumMineralTier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockBrokenMineral extends Block implements ITileEntityProvider{
	
	private final EnumMineralTier tier;

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockBrokenMineral(EnumMineralTier tier) {
		super(Material.ROCK);
		this.tier=tier;
        this.blockSoundType = SoundType.STONE;
        this.setResistance(15.0F);
        this.setHardness(50.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "ore_broken_"+tier.getName()));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	public EnumMineralTier getTier()
	{
		return this.tier;
	}
	
	private AxisAlignedBB box = new AxisAlignedBB(0,0,0,1,0.125,1);
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.box;
	}
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	@Override
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
    {
		if(this.tier==EnumMineralTier.DRAGONIC && entity instanceof EntityDragon)
			return false;
		return super.canEntityDestroy(state, world, pos, entity);
    }

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if(player.capabilities.isCreativeMode)
	        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
		else if((player.getHeldItemMainhand().getItem() instanceof ItemToolHammer || player.getHeldItemMainhand().getItem() instanceof ItemHammerBase) && player.isSneaking())
		{
			return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
		}
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileBrokenOre(world);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
		{
			enumfacing = EnumFacing.NORTH;
		}

		return this.getDefaultState().withProperty(FACING, enumfacing);
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(FACING)).getIndex();
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
