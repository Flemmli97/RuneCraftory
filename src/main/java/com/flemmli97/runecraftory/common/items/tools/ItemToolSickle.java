package com.flemmli97.runecraftory.common.items.tools;

import java.util.List;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumToolTier;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
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
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolSickle extends ItemTool implements IRpUseItem, IModelRegister{

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.YELLOW_FLOWER, Blocks.SAPLING, Blocks.LEAVES, Blocks.LEAVES2, Blocks.TALLGRASS, Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.DEADBUSH, Blocks.DOUBLE_PLANT, Blocks.MELON_BLOCK, Blocks.PUMPKIN, Blocks.RED_FLOWER, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.REEDS, Blocks.VINE, Blocks.WATERLILY);
	private int[] chargeRunes = new int[] {1, 5, 15, 50, 100};
	private int[] levelXP = new int[] {5, 20, 50, 200, 500};

	private EnumToolTier tier;
	public ItemToolSickle(EnumToolTier tier) {
		super(ModItems.mat, EFFECTIVE_ON);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "sickle_"+tier.getName()));	
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
		NBTTagList emtpyUpgrade = new NBTTagList();
		nbt.setString("Element", EnumElement.NONE.getName());
		switch(this.tier)
		{
			case SCRAP:
				stats.setInteger(ItemStats.RFATTACK.getName(), 2);
				break;
			case IRON:
				stats.setInteger(ItemStats.RFATTACK.getName(), 20);
				break;
			case SILVER:
				stats.setInteger(ItemStats.RFATTACK.getName(), 60);
				break;
			case GOLD:
				nbt.setString("Element", EnumElement.WIND.getName());
				stats.setInteger(ItemStats.RFATTACK.getName(), 120);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 20);
				break;
			case PLATINUM:
				nbt.setString("Element", EnumElement.WIND.getName());
				stats.setInteger(ItemStats.RFATTACK.getName(), 238);
				stats.setInteger(ItemStats.RFMAGICATT.getName(), 80);
				stats.setInteger(ItemStats.RFDIZ.getName(), 5);
				stats.setInteger(ItemStats.RFCRIT.getName(), 20);
				stats.setInteger(ItemStats.RFSTUN.getName(), 30);
				break;
		}
		nbt.setTag("ItemStats", stats);
		nbt.setInteger("ItemLevel", 1);
		nbt.setString("Element", EnumElement.NONE.getName());
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
				return 300;
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
				return 38;
			case IRON:
				return 140;
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
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving)
    {
		if(!world.isRemote && entityLiving instanceof EntityPlayer && this.getDestroySpeed(stack, state)==this.efficiency)
		{
			this.levelSkillOnBreak((EntityPlayer) entityLiving);
		}
		return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }
	
	@Override
	public void levelSkillOnHit(EntityPlayer player)
	{
		
	}
	
	@Override
	public void levelSkillOnBreak(EntityPlayer player)
	{
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		cap.decreaseRunePoints(player, this.chargeRunes[0]);
		cap.increaseSkill(EnumSkills.WIND, player, this.tier.getTierLevel()+1);
		cap.increaseSkill(EnumSkills.FARMING, player, this.tier.getTierLevel()+1);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
        return EnumAction.BOW;
    }
		
	@Override
	public EnumToolCharge chargeType(ItemStack stack)
    {
        return EnumToolCharge.CHARGESICKLE;
    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if(entityLiving instanceof EntityPlayer && this.tier.getTierLevel()!=0)
		{	       
			ItemStack itemstack = entityLiving.getHeldItem(EnumHand.MAIN_HAND);
	        int useTimeMulti = (this.getMaxItemUseDuration(stack) - timeLeft)/this.getChargeTime()[0];
			EntityPlayer player = (EntityPlayer) entityLiving;
			int range = Math.min(useTimeMulti, this.tier.getTierLevel());
			BlockPos pos = player.getPosition();
			boolean flag = false;
			for(int x = -range; x <= range;x ++)
			{
				for(int z = -range; z<= range;z++)
				{
					BlockPos posNew = pos.add(x, 0, z);
					if (player.canPlayerEdit(posNew.offset(EnumFacing.UP), EnumFacing.DOWN, itemstack))
					{
						IBlockState iblockstate = worldIn.getBlockState(posNew);
						Block block = iblockstate.getBlock();
						if (EFFECTIVE_ON.contains(block))
						{
			                worldIn.destroyBlock(posNew, true);
			                flag=true;
						}
					}
				}
			}
			if(flag)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
				capSync.decreaseRunePoints(player, this.chargeRunes[range]);
				capSync.increaseSkill(EnumSkills.WIND, player, this.levelXP[range]);
				capSync.increaseSkill(EnumSkills.FARMING, player, this.levelXP[range]);
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
			EnumActionResult result = EnumActionResult.FAIL;
			if (player.canPlayerEdit(pos.offset(EnumFacing.UP), EnumFacing.DOWN, player.getHeldItem(hand)))
			{
				IBlockState iblockstate = worldIn.getBlockState(pos);
				Block block = iblockstate.getBlock();
				if (EFFECTIVE_ON.contains(block))
				{
	                worldIn.destroyBlock(pos, true);
	                result=EnumActionResult.SUCCESS;
				}
			}
			if(result == EnumActionResult.SUCCESS)
			{
				IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
				capSync.decreaseRunePoints(player, 1);
				capSync.increaseSkill(EnumSkills.WIND, player, 1);
				capSync.increaseSkill(EnumSkills.FARMING, player, 1);
			}
			return result;
		}
		else
			return EnumActionResult.FAIL;
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
