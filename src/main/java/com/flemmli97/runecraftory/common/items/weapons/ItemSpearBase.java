package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.lib.LibConstants;
import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSpearBase extends Item implements IItemUsable, IChargeable, IAOEWeapon
{
    private int chargeXP = 25;

    public ItemSpearBase(Item.Properties props) {
        super(props);
    }

    @Override
    public int[] getChargeTime() {
        return new int[] { 15, 1 };
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPWEAPON;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.HAXE;
    }

    @Override
    public int itemCoolDownTicks() {
        return LibConstants.axeWeaponCooldown;
    }

    @Override
    public void onEntityHit(PlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap)
                .ifPresent(cap-> LevelCalc.levelSkill(player, cap, EnumSkills.HAMMERAXE, 1));
    }

    @Override
    public void onBlockBreak(PlayerEntity player) {
        player.getCapability(PlayerCapProvider.PlayerCap)
                .ifPresent(cap->LevelCalc.levelSkill(player, cap, EnumSkills.LOGGING, 0.5f));
    }

    @Override
    public float getRange() {
        return GeneralConfig.weaponProps.get(EnumWeaponType.HAXE).range();
    }

    @Override
    public float getFOV() {
        return GeneralConfig.weaponProps.get(EnumWeaponType.HAXE).aoe();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    /*@Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            IPlayer cap = (IPlayer)player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (this.getMaxItemUseDuration(stack) - timeLeft >= this.getChargeTime()[0]) {
            	cap.startSpear();
                (player.getCapability(PlayerCapProvider.PlayerCap, null)).decreaseRunePoints(player, 10);
                this.useSpear(player, stack);
            }
            else if (cap.canUseSpear()) {
                this.useSpear(player, stack);
            }
        }
    }
    
    private void useSpear(EntityPlayer player, ItemStack stack) {
        IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
        if (!player.world.isRemote && player instanceof EntityPlayerMP) {
            PacketHandler.sendTo(new PacketWeaponAnimation(60), (EntityPlayerMP)player);
        }
        List<EntityLivingBase> entityList = RayTraceUtils.getEntities(player, this.getRange(), 10);
        if (!entityList.isEmpty()) {
            cap.decreaseRunePoints(player, 15);
            cap.increaseSkill(EnumSkills.SPEAR, player, this.chargeXP);
            for (EntityLivingBase e : entityList) {
                float damagePhys = RFCalculations.getAttributeValue(player, ItemStatAttributes.RFATTACK, null, null);
                if (!(e instanceof IEntityBase)) {
                    damagePhys = LevelCalc.scaleForVanilla(damagePhys);
                }
                RFCalculations.playerDamage(player, e, CustomDamage.attack(player, ItemNBT.getElement(stack), CustomDamage.DamageType.NORMAL, CustomDamage.KnockBackType.UP, 0.0f, 20), damagePhys, cap, stack);
                player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, player.getSoundCategory(), 1.0f, 1.0f);
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IPlayer cap = playerIn.getCapability(PlayerCapProvider.PlayerCap, null);
        if (handIn == EnumHand.MAIN_HAND && (cap.getSkillLevel(EnumSkills.SPEAR)[0] >= 5 || playerIn.capabilities.isCreativeMode)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }*/


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
