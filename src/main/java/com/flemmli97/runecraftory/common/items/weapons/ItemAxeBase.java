package com.flemmli97.runecraftory.common.items.weapons;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.lib.ItemTiers;
import com.flemmli97.runecraftory.common.utils.CombatUtils;
import com.flemmli97.runecraftory.common.utils.CustomDamage;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.tenshilib.api.item.IAOEWeapon;
import com.flemmli97.tenshilib.common.utils.RayTraceUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class ItemAxeBase extends AxeItem implements IItemUsable, IChargeable, IAOEWeapon {
    private int chargeXP;

    public ItemAxeBase(Item.Properties props) {
        super(ItemTiers.tier, 0, 0, props);
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
        return EnumWeaponType.HAXE;
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
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayerEntity && this.getDestroySpeed(stack, state) == this.efficiency) {
            this.onBlockBreak((ServerPlayerEntity) entityLiving);
        }
        return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        int duration = stack.getUseDuration() - count;
        if (duration == this.getChargeTime(stack))
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (!world.isRemote && this.getUseDuration(stack) - timeLeft >= this.getChargeTime(stack)) {
            List<Entity> list = RayTraceUtils.getEntitiesIgnorePitch(entity, this.getRange(), 360, null);
            if (!list.isEmpty()) {
                CustomDamage src = new CustomDamage.Builder(entity).element(ItemNBT.getElement(stack)).knock(CustomDamage.KnockBackType.UP).knockAmount(0.7f).hurtResistant(20).get();
                boolean player = entity instanceof PlayerEntity;
                boolean success = false;
                for (Entity e : list) {
                    float damagePhys = CombatUtils.getAttributeValueRaw(entity, Attributes.ATTACK_DAMAGE);
                    if (CombatUtils.damage(entity, e, src, damagePhys, stack))
                        success = true;
                }
                if (success) {
                    entity.world.playSound(null, entity.getPosX(), entity.getPosY(), entity.getPosZ(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, entity.getSoundCategory(), 1.0f, 1.0f);
                    if (player)
                        entity.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {

                        });
                }
            }
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (player.isCreative() || player.getCapability(CapabilityInsts.PlayerCap).map(cap -> cap.getSkillLevel(EnumSkills.HAMMERAXE)[0] >= 5).orElse(false)) {
            player.setActiveHand(hand);
            return ActionResult.resultSuccess(itemstack);
        }
        return ActionResult.resultPass(itemstack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
