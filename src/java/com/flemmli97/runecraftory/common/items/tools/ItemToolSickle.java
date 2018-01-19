package com.flemmli97.runecraftory.common.items.tools;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.RFReference;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolSickle extends ItemTool implements IRpUseItem, IItemBase, IItemWearable{

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.YELLOW_FLOWER, Blocks.SAPLING, Blocks.LEAVES, Blocks.LEAVES2, Blocks.TALLGRASS, Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.DEADBUSH, Blocks.DOUBLE_PLANT, Blocks.MELON_BLOCK, Blocks.PUMPKIN, Blocks.RED_FLOWER, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.REEDS, Blocks.VINE, Blocks.WATERLILY);

	private EnumToolTier tier;
	private int level=1;
	private Item[] upgradeList = new Item[] {};
	private EnumElement element = EnumElement.NONE;
	private Map<IAttribute, Integer> stats = new LinkedHashMap<IAttribute, Integer>();
	public ItemToolSickle(EnumToolTier tier) {
		super(ModItems.mat, EFFECTIVE_ON);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, "sickle_" + tier.getName()));	
        this.setUnlocalizedName(this.getRegistryName().toString());
		this.tier = tier;
		this.initStats();
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

	private void initStats()
	{
		switch(this.tier)
		{
			case SCRAP:
				this.stats.put(IRFAttributes.RFATTACK, 2);
				break;
			case IRON:
				this.stats.put(IRFAttributes.RFATTACK, 20);
				break;
			case SILVER:
				this.stats.put(IRFAttributes.RFATTACK, 60);
				break;
			case GOLD:
				this.element=EnumElement.WIND;
				this.stats.put(IRFAttributes.RFATTACK, 120);
				this.stats.put(IRFAttributes.RFMAGICATT, 20);
				break;
			case PLATINUM:
				this.element=EnumElement.WIND;
				this.stats.put(IRFAttributes.RFATTACK, 238);
				this.stats.put(IRFAttributes.RFMAGICATT, 80);
				this.stats.put(IRFAttributes.RFDIZ, 5);
				this.stats.put(IRFAttributes.RFCRIT, 20);
				this.stats.put(IRFAttributes.RFSTUN, 30);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if(this.element!=EnumElement.NONE)
			tooltip.add(TextFormatting.getValueByName(this.element.getColor()) + I18n.format("attribute." + this.element.getName()));
		if(this.getBuyPrice(stack)>0)
			tooltip.add(I18n.format("level")+ ": " + this.itemLevel() +"  "+ I18n.format("buy") +": " + this.getBuyPrice(stack) + "  "+ I18n.format("sell")+": "+this.getSellPrice(stack));
		else
			tooltip.add(I18n.format("level")+ ": " + this.itemLevel()+ "  "+ I18n.format("sell")+": "+this.getSellPrice(stack));
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
		for(IAttribute att : this.stats.keySet())
		{
			tooltip.add(I18n.format(att.getName()) + ": " +  this.stats.get(att));
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
	public int getUpgradeDifficulty(ItemStack stack) {
		return 0;
	}

	@Override
	public void useRunePoints(EntityPlayer player, int amount) {
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		capSync.decreaseRunePoints(player, amount);
	}
	
	@Override
	public int itemCoolDownTicks() {
		return 15;
	}

	private int[] levelXP = new int[] {5, 20, 50, 200, 500};

	@Override
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill)
	{
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		capSync.increaseSkill(skill, player, levelXP[amount]);
	}
	
	@Override
	public int[] getChargeTime()
	{
		int charge = 15;
		if(this.tier==EnumToolTier.PLATINUM)
			charge = 7;
		return new int[] {charge, this.tier.getTierLevel()};
	}
	
	private int[] chargeRunes = new int[] {1, 5, 15, 50, 100};
	private void useRunePointCharge(EntityPlayer player, int charge)
	{
		int amount =  chargeRunes[charge];
		this.useRunePoints(player, amount);
	}

	@Override
	public Map<IAttribute, Integer> statIncrease() {
		return this.stats;
	}

	@Override
	public void updateStatIncrease(IAttribute attribute, int amount) {
		this.stats.put(attribute, amount);
	}

	@Override
	public void setElement(EnumElement element) {
		this.element=element;
	}

	@Override
	public EnumElement getElement() {
		return this.element;
	}

	@Override
	public Item[] upgradeItems() {
		return this.upgradeList;
	}

	@Override
	public void addUpgradeItem(Item stack) {
		for(int i = 0; i < 10; i ++)
		{
			if(this.upgradeList[i]==null)
				this.upgradeList[i] = stack;
		}
	}

	@Override
	public int itemLevel() {
		return this.level;
	}

	@Override
	public void addItemLevel() {
		if(this.level<10)
			this.level++;
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
				this.useRunePointCharge(player, range);
				this.levelSkill(player, range, EnumSkills.WATER);
				this.levelSkill(player, range, EnumSkills.FARMING);
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
				this.useRunePoints(player, 1);
				this.levelSkill(player, 0, EnumSkills.WATER);
				this.levelSkill(player, 0, EnumSkills.FARMING);
			}
			return result;
		}
		else
			return EnumActionResult.FAIL;
	}

	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		return super.updateItemStackNBT(nbt);
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
