package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.api.enums.EnumElement;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.capability.IStaffCap;
import com.flemmli97.runecraftory.common.capability.StaffCapImpl;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public class ItemStaffBase extends Item implements IItemUsable, IChargeable {

    private int chargeXP = 25;
    public final EnumElement startElement;

    public ItemStaffBase(EnumElement startElement, Item.Properties props) {
        super(props);
        this.startElement = startElement;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        return stack.getCapability(CapabilityInsts.StaffCap).map(cap ->
                cap.getTier1Spell() != null ? cap.getTier1Spell().coolDown() : cap.getTier2Spell() != null ?  cap.getTier1Spell().coolDown() : cap.getTier3Spell() != null ? cap.getTier3Spell().coolDown() : 0).orElse(GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime());
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return stack.getCapability(CapabilityInsts.StaffCap).map(cap ->
                cap.getTier3Spell() != null ? 3 : cap.getTier2Spell() != null ? 2 : cap.getTier1Spell() != null ? 1 : 0).orElse(0);
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPWEAPON;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.STAFF;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.HAMMERAXE, 1));
    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.LOGGING, 0.5f));
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

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        int duration = stack.getUseDuration() - count;
        if (duration != 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (!world.isRemote) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int level = Math.min(useTime, this.chargeAmount(stack));
            Spell spell = this.getSpell(stack, level);
            if (spell != null) {
                if (spell.use((ServerWorld) world, entity, stack) && entity instanceof PlayerEntity)
                    spell.levelSkill((ServerPlayerEntity) entity);
            }
        }
    }

    public Spell getSpell(ItemStack stack, int level) {
        IStaffCap cap = stack.getCapability(CapabilityInsts.StaffCap).orElseThrow(()->new NullPointerException("Error getting capability for staff item"));
        switch (level) {
            case 3:
                return cap.getTier3Spell();
            case 2:
                return cap.getTier2Spell();
            case 1:
                return cap.getTier1Spell();
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(this.chargeAmount(player.getHeldItem(hand)) > 0){
            player.setActiveHand(hand);
            return ActionResult.success(player.getHeldItem(hand));
        }
        return ActionResult.pass(player.getHeldItem(hand));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return new StaffCapImpl();
    }
}

