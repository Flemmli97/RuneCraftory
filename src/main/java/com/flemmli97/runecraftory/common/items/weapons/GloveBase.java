package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayerAnim;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;
import com.flemmli97.runecraftory.common.lib.enums.EnumWeaponType;
import com.flemmli97.runecraftory.common.network.PacketHandler;
import com.flemmli97.runecraftory.common.network.PacketSwingArm;
import com.flemmli97.runecraftory.common.network.PacketWeaponAnimation;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
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

public abstract class GloveBase extends ItemSword implements IItemUsable, IModelRegister, IChargeable
{
    public GloveBase(String name) {
        super(ModItems.mat);
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.weaponToolTab);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.addPropertyOverride(new ResourceLocation("held"), (IItemPropertyGetter)new IItemPropertyGetter() {
            public float apply(ItemStack stack, World world, EntityLivingBase entity) {
                if (entity == null || entity.getHeldItemMainhand() != stack) {
                    return 0.0f;
                }
                if (entity instanceof EntityPlayer && entity.world.isRemote && ((AbstractClientPlayer)entity).getSkinType().equals("slim")) {
                    return 2.0f;
                }
                return 1.0f;
            }
        });
    }
    
    @Override
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack) {
        if (entityLiving instanceof EntityPlayer && !entityLiving.world.isRemote) {
            PacketHandler.sendTo(new PacketSwingArm(), (EntityPlayerMP)entityLiving);
        }
        return true;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }
    
    @Override
    public EnumWeaponType getWeaponType() {
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
    	if (this.isInCreativeTab(tab)) {
            ItemStack stack = new ItemStack(this);
            ItemNBT.initNBT(stack);
            items.add(stack);
        }
    }
    
    @Override
    public int itemCoolDownTicks() {
        return 5;
    }
    
    @Override
    public int[] getChargeTime() {
        return new int[] { 15, 1 };
    }
    
    @Override
    public void levelSkillOnHit(EntityPlayer player) {
        IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        cap.increaseSkill(EnumSkills.FIST, player, 1);
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
        return EnumToolCharge.CHARGEFIST;
    }
    
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            IPlayerAnim anim = (IPlayerAnim)player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerAnim, null);
            if (this.getMaxItemUseDuration(stack) - timeLeft >= this.getChargeTime()[0]) {
                if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                    PacketHandler.sendTo(new PacketWeaponAnimation(35), (EntityPlayerMP)player);
                }
                anim.startGlove(player);
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IPlayer cap = playerIn.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        if (handIn == EnumHand.MAIN_HAND && (cap.getSkillLevel(EnumSkills.FIST)[0] >= 5 || playerIn.capabilities.isCreativeMode)) {
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
        ModelLoader.setCustomModelResourceLocation((Item)this, 0, new ModelResourceLocation(this.getRegistryName(), "inventory"));
    }
}
