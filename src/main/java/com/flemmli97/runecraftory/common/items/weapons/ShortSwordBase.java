package com.flemmli97.runecraftory.common.items.weapons;

import java.util.List;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.entities.IEntityBase;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.CustomDamage;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketWeaponAnimation;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.common.utils.RFCalculations;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ShortSwordBase extends ItemSword implements IItemUsable, IModelRegister, IChargeable
{
    private int chargeXP = 25;
    
    public ShortSwordBase(String name) {
        super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    	if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ItemNBT.initNBT(stack);
            items.add(stack);
        }
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.SHORTSWORD;
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
    public int itemCoolDownTicks() {
        return 7;
    }

    @Override
    public int[] getChargeTime() {
        return new int[] { 15, 1 };
    }

    @Override
    public void levelSkillOnHit(EntityPlayer player) {
        IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        cap.increaseSkill(EnumSkills.SHORTSWORD, player, 1);
    }

    @Override
    public void levelSkillOnBreak(EntityPlayer player) {
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGESWORD;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
            if (this.getMaxItemUseDuration(stack) - timeLeft >= this.getChargeTime()[0]) 
            {
                if (!player.world.isRemote && player instanceof EntityPlayerMP) 
                {
                    PacketHandler.sendTo(new PacketWeaponAnimation(25), (EntityPlayerMP)player);
                }
                cap.decreaseRunePoints(player, 10);
                cap.increaseSkill(EnumSkills.SHORTSWORD, player, 100);
                List<EntityLivingBase> entityList = RFCalculations.calculateEntitiesFromLook(player, this.getWeaponType().getRange() + 2.0f, this.getWeaponType().getAOE() + 1.0f);
                if (!entityList.isEmpty()) 
                {
                    cap.decreaseRunePoints(player, 15);
                    cap.increaseSkill(EnumSkills.SHORTSWORD, player, this.chargeXP);
                    for (EntityLivingBase e : entityList) {
                        float damagePhys = cap.getStr();
                        damagePhys += RFCalculations.getAttributeValue(player, ItemStatAttributes.RFATTACK, null, null);
                        if (!(e instanceof IEntityBase)) {
                            damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                        }
                        RFCalculations.playerDamage(player, e, CustomDamage.attack(player, ItemNBT.getElement(stack), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.UP, 0.65f, 20), damagePhys, cap, stack);
                        player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0f, 1.0f);
                    }
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IPlayer cap = playerIn.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        if (handIn == EnumHand.MAIN_HAND && (cap.getSkillLevel(EnumSkills.SHORTSWORD)[0] >= 5 || playerIn.capabilities.isCreativeMode)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        return HashMultimap.<String, AttributeModifier>create();

    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
