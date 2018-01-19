package com.flemmli97.runecraftory.common.items.weapons;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.IRFAttributes;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketWeaponAnimation;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.RFCalculations;
import com.flemmli97.runecraftory.common.lib.RFReference;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GloveBase extends ItemSword implements IRpUseItem, IItemBase, IItemWearable{

	private int level=1;
	private Item[] upgradeList = new Item[] {};
	private EnumElement element = EnumElement.NONE;
	protected Map<IAttribute, Integer> stats = new LinkedHashMap<IAttribute, Integer>();
	public GloveBase(String name) {
		super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, name));	
        this.setUnlocalizedName(this.getRegistryName().toString());
		this.initStats();
	}
	
	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}
	
	@Override
	public EnumWeaponType getWeaponType()
	{
		return EnumWeaponType.GLOVE;
	}
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getRegistryName().toString();
	}

	protected abstract void initStats();
	
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
		return 5;
	}

	private int[] levelXP = new int[] {20, 100};

	@Override
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill)
	{
		IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
		capSync.increaseSkill(skill, player, levelXP[amount]);
	}
	
	@Override
	public int[] getChargeTime()
	{
		return new int[] {15, 1};
	}
	
	private void useRunePointCharge(EntityPlayer player, int charge)
	{
		this.useRunePoints(player, 10);
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
        return EnumAction.BLOCK;
    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		if(entityLiving instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLiving;
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if((this.getMaxItemUseDuration(stack) - timeLeft)>=this.getChargeTime()[0])
			{
				if(!player.world.isRemote && player instanceof EntityPlayerMP)
				{
					PacketHandler.sendTo(new PacketWeaponAnimation(35), (EntityPlayerMP) player);
				}
				this.useRunePointCharge(player, 0);
				this.levelSkill(player, 0, EnumSkills.FIST);
				float f1 = MathHelper.sin(player.rotationYaw * 0.017453292F);
	            float f2 = MathHelper.cos(player.rotationYaw * 0.017453292F);
				List<EntityLivingBase> entityList = RFCalculations.calculateEntitiesFromLook(player, 10, 10);
				player.setVelocity((double)(f2 - f1), 0, (double)(f2 + f1));
				if(!entityList.isEmpty())
				{
			    		for (EntityLivingBase e: entityList)
			    		{
			    			float damagePhys = cap.getStr();
			    			damagePhys+= RFCalculations.getAttributeValue(player, IRFAttributes.RFATTACK, null, null);
			    			if(!(e instanceof IEntityBase))
			    				damagePhys=RFCalculations.scaleForVanilla(damagePhys);
	            			e.attackEntityFrom(CustomDamage.doAttack(player, this.getElement(), 0), damagePhys);
	            			player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
			    		}
				}
			}
		}
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
		if(handIn == EnumHand.MAIN_HAND)
		{
	        playerIn.setActiveHand(handIn);
	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
		}
		else
			return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();
	}
		
	@Override
	public boolean updateItemStackNBT(NBTTagCompound nbt) {
		return super.updateItemStackNBT(nbt);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}
}
