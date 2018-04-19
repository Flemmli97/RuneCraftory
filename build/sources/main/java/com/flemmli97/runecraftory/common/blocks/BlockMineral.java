package com.flemmli97.runecraftory.common.blocks;

import java.util.Random;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.tools.ItemToolHammer;
import com.flemmli97.runecraftory.common.items.weapons.HammerBase;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockMineral extends Block{
	
    public static final PropertyEnum<BlockMineral.EnumTier> TIER = PropertyEnum.<BlockMineral.EnumTier>create("tier", BlockMineral.EnumTier.class);

    private static float skillChanceUp = 0.005F;
	public BlockMineral() {
		super(Material.ROCK);
		this.setCreativeTab(RuneCraftory.blocks);
        this.blockSoundType = SoundType.STONE;
        this.setResistance(5.0F);
        this.setHardness(3.0F);
        this.setDefaultState(this.getDefaultState().withProperty(TIER, EnumTier.NONE));
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "ore"));
		this.setUnlocalizedName(this.getRegistryName().toString());
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab==RuneCraftory.blocks)
			for(EnumTier tier : EnumTier.values())
			{
				items.add(new ItemStack(this, 1, tier.getMeta()));
			}
	}

	public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return Items.AIR;
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TIER});
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if(player.capabilities.isCreativeMode)
	        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), world.isRemote ? 11 : 3);
		else if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer || player.getHeldItemMainhand().getItem() instanceof HammerBase)
		{
			this.dropItem(state, world, pos, player);
			float breakChance = 0.5F;
			if(player.getHeldItemMainhand().getItem() instanceof ItemToolHammer)
			{
				ItemToolHammer item = (ItemToolHammer) player.getHeldItemMainhand().getItem();
				breakChance-=item.getTier().getTierLevel()*0.05;
			}
			if(world.rand.nextFloat()<breakChance)
			{
		        return world.setBlockState(pos, ModBlocks.brokenMineral.getDefaultState().withProperty(BlockMineral.TIER, state.getValue(TIER)), world.isRemote ? 11 : 3);
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
		switch(state.getValue(TIER))
		{
			case DRAGONIC:
				if(world.rand.nextFloat()<addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 6));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 0));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case EARTH:
				break;
			case FIRE:
				break;
			case NONE:
				if(world.rand.nextFloat()<addChance-0.01F*30)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 7));
				}
				else if(world.rand.nextFloat()<0.35F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 0));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.scrap, 1, 0));
				}
				break;
			case RARE:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 7));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 4));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 3));
				}
				break;
			case UNCOMMON:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 9));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 5));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 0));
				}
				break;
			case URARE:
				if(world.rand.nextFloat()<0.05F+addChance-skillChanceUp*40)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.crystal, 1, 9));
				}
				else if(world.rand.nextFloat()<0.25F+addChance)
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 7));
				}
				else
				{
					ItemUtils.spawnItemAt(world, pos, new ItemStack(ModItems.mineral, 1, 6));
				}
				break;
			case WATER:
				break;
			case WIND:
				break;
		}
	}
	
	@Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(TIER, EnumTier.fromMeta(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state)
    {
    		return state.getValue(TIER).getMeta();
    }

	@Override
	public int damageDropped(IBlockState state) {
		return this.getMetaFromState(state);
	}

	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
	
	public static enum EnumTier implements IStringSerializable
    {
		NONE("basic", 0),
		UNCOMMON("uncommon",1),
		RARE("rare",2),
		URARE("urare",3),
		DRAGONIC("dragonic",4),
		FIRE("fire",5),
		WATER("water",6),
		EARTH("earth",7),
		WIND("wind",8);

		private final String name;
        private final int meta;

        private EnumTier(String name, int meta)
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
        
        public static EnumTier fromMeta(int meta)
        {
        		return values()[meta];
        }
		
    }
}
