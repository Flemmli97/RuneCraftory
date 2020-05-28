package com.flemmli97.runecraftory.common.blocks;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.weapons.ItemHammerBase;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumMineralTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import com.flemmli97.runecraftory.common.utils.MineralBlockConverter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
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

public class BlockMineral extends Block{
	
    private static float skillChanceUp = 0.005F;
    private final EnumMineralTier tier;

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public BlockMineral(EnumMineralTier tier) {
		super(Material.ROCK);
		this.tier=tier;
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(5.0F);
        this.setHardness(3.0F);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "ore_"+tier.getName()));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	private AxisAlignedBB box = new AxisAlignedBB(0,0,0,1,0.875,1);
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.box;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	@Override
    public boolean isFullCube(IBlockState state)
    {
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
		else if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer || player.getHeldItemMainhand().getItem() instanceof ItemHammerBase)
		{
			if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer)
			this.dropItem(state, world, pos, player);
			float breakChance = 0.5F;
			if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer)
			{
				ItemToolHammer item = (ItemToolHammer) player.getHeldItemMainhand().getItem();
				breakChance-=item.getTier().getTierLevel()*0.075F;
			}
			if(world.rand.nextFloat()<breakChance)
			{
		        return world.setBlockState(pos, MineralBlockConverter.getBrokenState(this.tier).withProperty(BlockBrokenMineral.FACING, state.getValue(FACING)), world.isRemote ? 11 : 3);
			}
			else
				return false;
		}
		return false;
	}
	
	private void dropItem(IBlockState state, World world, BlockPos pos, EntityPlayer player)
	{
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		float addChance = cap.getSkillLevel(EnumSkills.MINING)[0]*skillChanceUp;
		if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer)
		{
			ItemToolHammer item = (ItemToolHammer) player.getHeldItemMainhand().getItem();
			addChance+=item.getTier().getTierLevel()*0.05F;
		}
		switch(this.tier)
		{
			case IRON:
				if(world.rand.nextFloat()<addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalSmall));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case BRONZE:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalBig));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.platinum));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.GOLD_INGOT));
				}
				break;
			case SILVER:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					//ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 7));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.silver));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.bronze));
				}
				break;
			case GOLD:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					//ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 9));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.orichalcum));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.platinum));
				}
				break;
			case PLATINUM:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					//ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 9));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.orichalcum));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.platinum));
				}
				break;
			case ORICHALCUM:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					//ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 9));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.orichalcum));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.platinum));
				}
				break;
			case DIAMOND:
				break;
			case DRAGONIC:
				if(world.rand.nextFloat()<addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.dragonic));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case EARTH:
				if(world.rand.nextFloat()<Math.min(0.1+addChance, 0.7))
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalEarth));
				}
				else if(world.rand.nextFloat()<0.15F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.amethyst));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case FIRE:
				if(world.rand.nextFloat()<Math.min(0.1+addChance, 0.7))
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalFire));
				}
				else if(world.rand.nextFloat()<0.15F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.ruby));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case WATER:
				if(world.rand.nextFloat()<Math.min(0.1+addChance, 0.7))
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalWater));
				}
				else if(world.rand.nextFloat()<0.15F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.aquamarine));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case WIND:
				if(world.rand.nextFloat()<Math.min(0.1+addChance, 0.7))
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystalWind));
				}
				else if(world.rand.nextFloat()<0.15F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.emerald));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(Items.IRON_INGOT));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
		}
	}

	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
