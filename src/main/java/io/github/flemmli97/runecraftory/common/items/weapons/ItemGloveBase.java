package io.github.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import com.flemmli97.tenshilib.api.item.IDualWeapon;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.capability.CapabilityInsts;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemGloveBase extends Item implements IItemUsable, IChargeable, IDualWeapon, IAOEWeapon {

    public ItemGloveBase(Item.Properties props) {
        super(props.maxStackSize(1));
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return 1;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPWEAPON;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.GLOVE;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.FIST, 1));
    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {

    }

    @Override
    public float getRange() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).range();
    }

    @Override
    public float getFOV() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).aoe();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        int duration = stack.getUseDuration() - count;
        if (duration == this.getChargeTime(stack))
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }
    /*
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)entityLiving;
            IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
            if (this.getMaxItemUseDuration(stack) - timeLeft >= this.getChargeTime()[0]) {
                if (!player.world.isRemote && player instanceof EntityPlayerMP) {
                    PacketHandler.sendTo(new PacketWeaponAnimation(35), (EntityPlayerMP)player);
                }
                cap.startGlove(player);
            }
        }
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        IPlayer cap = playerIn.getCapability(PlayerCapProvider.PlayerCap, null);
        if (handIn == EnumHand.MAIN_HAND && (cap.getSkillLevel(EnumSkills.FIST)[0] >= 5 || playerIn.capabilities.isCreativeMode)) {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
    }*/

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.isCreative() || player.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getSkillLevel(EnumSkills.FIST)[0] >= 5).orElse(false)) {
            player.setActiveHand(hand);
            return ActionResult.resultSuccess(itemstack);
        }
        return ActionResult.resultPass(itemstack);
    }
}
