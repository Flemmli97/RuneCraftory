package com.flemmli97.runecraftory.common.items.tools;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModBlocks;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolHoe extends ItemHoe implements IRpUseItem{

	private EnumToolTier tier;
	private int[] levelXP = new int[] {5, 20, 50, 200, 500};
	private int[] chargeRunes = new int[] {1, 5, 15, 50, 100};

	public ItemToolHoe(EnumToolTier tier) {
		super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "hoe_" + tier.getName()));	
        this.setUnlocalizedName(this.getRegistryName().toString());
		this.tier = tier;
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
	@Override
	public EnumWeaponType getWeaponType()
	{
		return EnumWeaponType.FARM;
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getRegistryName().toString();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab))
        {
			ItemStack stack = new ItemStack(this);
			ItemNBT.initItemNBT(stack, this.defaultNBTStats(stack));
            items.add(stack);
        }
	}

	@Override
	public NBTTagCompound defaultNBTStats(ItemStack stack)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagCompound stats = new NBTTagCompound();
		NBTTagCompound emtpyUpgrade = new NBTTagCompound();

		switch(this.tier)
		{
			case SCRAP:
				stats.setInteger(ItemStats.RFATTACK.getName(), 1);
				break;
			case IRON:
				stats.setInteger(ItemStats.RFATTACK.getName(), 18);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 5);
				break;
			case SILVER:
				stats.setInteger(ItemStats.RFATTACK.getName(), 30);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 10);
				break;
			case GOLD:
				stats.setInteger(ItemStats.RFATTACK.getName(), 64);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 50);
				break;
			case PLATINUM:
				stats.setInteger(ItemStats.RFATTACK.getName(), 170);
				stats.setInteger(ItemStats.RFDEFENCE.getName(), 70);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 60);
				stats.setInteger(ItemStats.RFMAGICDEF.getName(), 30);
				break;
		}
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.EARTH.getName());
		nbt.setTag("Upgrades", emtpyUpgrade);
		return nbt;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(stack.hasTagCompound())
		{
			EnumElement element = EnumElement.fromName(stack.getTagCompound().getString("Element"));
			if(element!=EnumElement.NONE)
				tooltip.add(TextFormatting.getValueByName(element.getColor()) + I18n.format("attribute." + element.getName()));
			if(this.getBuyPrice(stack)>0)
				tooltip.add(I18n.format("level")+ ": " + ItemNBT.itemLevel(stack) +"  "+ I18n.format("buy") +": " + this.getBuyPrice(stack) + "  "+ I18n.format("sell")+": "+this.getSellPrice(stack));
			else
				tooltip.add(I18n.format("level")+ ": " + ItemNBT.itemLevel(stack)+ "  "+ I18n.format("sell")+": "+this.getSellPrice(stack));
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				for(ItemStats att : ItemNBT.statIncrease(stack).keySet())
				{
					tooltip.add(I18n.format(att.getName()) + ": " +  ItemNBT.statIncrease(stack).get(att));
				}
			}
		}
	}

	@Override
	public int getBuyPrice(ItemStack stack) {
		switch(this.tier)
		{
			case SCRAP:
				return 150;
			case IRON:
				return 3000;
			case SILVER:
				return 25000;
			case GOLD:
				return -1;
			case PLATINUM:		
				return -1;
		}
		return -1;
	}

	@Override
	public int getSellPrice(ItemStack stack) {
		switch(this.tier)
		{
			case SCRAP:
				return 24;
			case IRON:
				return 120;
			case SILVER:
				return 400;
			case GOLD:
				return 600;
			case PLATINUM:		
				return 2500;
		}
		return -1;	
	}

	@Override
	public int getUpgradeDifficulty() {
		return 0;
	}
	
	@Override
	public int itemCoolDownTicks() {
		return 15;
	}
	
	@Override
	public int[] getChargeTime()
	{
		int charge = 15;
		if(this.tier==EnumToolTier.PLATINUM)
			charge = 7;
		return new int[] {charge, this.tier.getTierLevel()};
	}
	
	@Override
	public void levelSkillOnHit(EntityPlayer player){}
	
	@Override
	public void levelSkillOnBreak(EntityPlayer player){}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if(entityLiving instanceof EntityPlayer && this.tier.getTierLevel()!=0)
		{	       
			ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
	        int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft)/this.getChargeTime()[0];
			EntityPlayer player = (EntityPlayer) entityLiving;
			int range = Math.min(useTimeMulti, this.tier.getTierLevel());
			BlockPos pos = player.getPosition().down();
			boolean flag = false;
			if(range==0)
			{
				RayTraceResult result = this.rayTrace(worldIn, player, false);
				if(result!=null && result.typeOfHit==Type.BLOCK)
				{
					this.useOnBlock(player, worldIn, result.getBlockPos(), EnumHand.MAIN_HAND, result.sideHit, (float)result.hitVec.x, (float)result.hitVec.y, (float)result.hitVec.z);
					return;
				}
			}
			else
				for(int x = -range; x <= range;x ++)
				{
					for(int z = -range; z<= range;z++)
					{
						BlockPos posNew = pos.add(x, 0, z);
						if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack))
						{
							int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);	
							if (hook==0)
							{
								IBlockState iblockstate = worldIn.getBlockState(posNew);
								Block block = iblockstate.getBlock();
								if (worldIn.isAirBlock(posNew.up()))
								{
									IBlockState farmland = ModBlocks.farmland.getDefaultState();
									if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
									{
										this.setBlock(itemstack, player, worldIn, posNew, farmland);
										flag=true;
									}
									else if (block == Blocks.DIRT)
									{
										switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
										{
											case DIRT:
												flag=true;
												this.setBlock(itemstack, player, worldIn, posNew, farmland);											
												break;
											case COARSE_DIRT:
												flag=true;
												this.setBlock(itemstack, player, worldIn, posNew, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));		                        				
												break;
											default:
												break;
										}
									}
								}
							}
						}
					}
				}
			if(flag)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
				capSync.decreaseRunePoints(player, this.chargeRunes[range]);
				capSync.increaseSkill(EnumSkills.EARTH, player, levelXP[range]);
				capSync.increaseSkill(EnumSkills.FARMING, player, levelXP[range]);
			}
		}
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(handIn == EnumHand.MAIN_HAND && this.tier.getTierLevel()!=0)
		{
	        playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		else
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(this.tier.getTierLevel()==0)
		{
			return this.useOnBlock(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
		}
		else
			return EnumActionResult.PASS;
	}
	
	private EnumActionResult useOnBlock(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);
		EnumActionResult result = EnumActionResult.PASS;
        if(player.canPlayerEdit(pos.offset(facing), facing, itemstack))
        {
            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemstack, player, worldIn, pos);
            if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;

            IBlockState iblockstate = worldIn.getBlockState(pos);
            Block block = iblockstate.getBlock();

            if (facing != EnumFacing.DOWN && worldIn.isAirBlock(pos.up()))
            {
				IBlockState farmland = ModBlocks.farmland.getDefaultState();

                if (block == Blocks.GRASS || block == Blocks.GRASS_PATH)
                {
                    this.setBlock(itemstack, player, worldIn, pos, farmland);
                    result = EnumActionResult.SUCCESS;
                }
                else if (block == Blocks.DIRT)
                {
                	System.out.println((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT));
                    switch ((BlockDirt.DirtType)iblockstate.getValue(BlockDirt.VARIANT))
                    {
                        case DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, farmland);
                            result = EnumActionResult.SUCCESS;
                        case COARSE_DIRT:
                            this.setBlock(itemstack, player, worldIn, pos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
                            result = EnumActionResult.SUCCESS;
                        case PODZOL:
                        	break;                
                    }
                }
            }
			if(result == EnumActionResult.SUCCESS)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
				capSync.decreaseRunePoints(player, 1);
				capSync.increaseSkill(EnumSkills.EARTH, player, 2);
				capSync.increaseSkill(EnumSkills.FARMING, player, 2);
			}
        }
        return result;
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
