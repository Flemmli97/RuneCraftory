package com.flemmli97.runecraftory.common.items.weapons;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.entities.ItemStats;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage.KnockBackType;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.network.PacketHandler;
import com.flemmli97.runecraftory.common.core.network.PacketSwingArm;
import com.flemmli97.runecraftory.common.core.network.PacketWeaponAnimation;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GloveBase extends ItemSword implements IRpUseItem{

	private int chargeXP=25;

	public GloveBase(String name) {
		super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));	
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.addPropertyOverride(new ResourceLocation("held"), new IItemPropertyGetter() {

			@Override
			public float apply(ItemStack stack, World world, EntityLivingBase entity) {
				if(entity!=null)
				{
					if(entity.getHeldItemMainhand()==stack)
					{
						if(entity instanceof EntityPlayer && entity.world.isRemote)
						{
							if(((AbstractClientPlayer)entity).getSkinType().equals("slim"))
								return 2;
						}
						return 1;
					}
				}
				return 0;
			}});
	}
	
	@Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
		if(entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote)
			PacketHandler.sendTo(new PacketSwingArm(), (EntityPlayerMP) entityLiving);
		return true;
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
	public int getUpgradeDifficulty() {
		return 0;
	}
	
	@Override
	public int itemCoolDownTicks() {
		return 5;
	}
	
	@Override
	public int[] getChargeTime()
	{
		return new int[] {15, 1};
	}
	
	@Override
	public void levelSkillOnHit(EntityPlayer player)
	{
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		cap.increaseSkill(EnumSkills.FIST, player, 1);
	}
	
	@Override
	public void levelSkillOnBreak(EntityPlayer player){}
	
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
				cap.decreaseRunePoints(player, 10);
				cap.increaseSkill(EnumSkills.FIST, player, 100);

				float f1 = MathHelper.sin(player.rotationYaw * 0.017453292F);
	            float f2 = MathHelper.cos(player.rotationYaw * 0.017453292F);
				List<EntityLivingBase> entityList = RFCalculations.calculateEntitiesFromLook(player, 10, 10);
				player.setVelocity((double)(f2 - f1), 0, (double)(f2 + f1));
				if(!entityList.isEmpty())
				{
					cap.decreaseRunePoints(player, 15);
					cap.increaseSkill(EnumSkills.FIST, player, this.chargeXP);
			    		for (EntityLivingBase e: entityList)
			    		{
			    			float damagePhys = cap.getStr();
			    			damagePhys+= RFCalculations.getAttributeValue(player, ItemStats.RFATTACK, null, null);
			    			if(!(e instanceof IEntityBase))
			    				damagePhys=LevelCalc.scaleForVanilla(damagePhys);
	            			e.attackEntityFrom(CustomDamage.attack(player, EnumElement.fromName(stack.getTagCompound().getString("Element")), CustomDamage.DamageType.NORMAL, KnockBackType.BACK, 0, 20), damagePhys);
	            			player.world.playSound((EntityPlayer)null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0F, 1.0F);
			    		}
				}
			}
		}
    }
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IPlayer cap = playerIn.getCapability(PlayerCapProvider.PlayerCap, null);

		if(handIn == EnumHand.MAIN_HAND && (cap.getSkillLevel(EnumSkills.FIST)[0]>=5 || playerIn.capabilities.isCreativeMode))
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

	@SideOnly(Side.CLIENT)
	public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}
}
