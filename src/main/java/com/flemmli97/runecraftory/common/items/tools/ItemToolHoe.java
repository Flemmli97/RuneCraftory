package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemToolHoe extends HoeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;

    public ItemToolHoe(EnumToolTier tier, Item.Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if(this.tier == EnumToolTier.PLATINUM)
            return (int) (GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return GeneralConfig.weaponProps.get(this.getWeaponType()).chargeTime();
    }

    @Override
    public int chargeAmount(ItemStack stack) {
        return this.tier.getTierLevel();
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGEUPTOOL;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }

    @Override
    public int itemCoolDownTicks() {
        return GeneralConfig.weaponProps.get(this.getWeaponType()).cooldown();
    }

    @Override
    public void onEntityHit(ServerPlayerEntity player) {

    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {

    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        int duration = stack.getUseDuration() - count;
        if (duration != 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && !world.isRemote) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel());
            BlockPos pos = entity.getBlockPos();
            AtomicBoolean flag = new AtomicBoolean(false);
            if (range == 0) {
                if (entity instanceof PlayerEntity) {
                    BlockRayTraceResult result = rayTrace(world, (PlayerEntity) entity, RayTraceContext.FluidMode.NONE);
                    if (result != null) {
                        this.useOnBlock(new ItemUseContext((PlayerEntity) entity, entity.getActiveHand(), result));
                        return;
                    }
                }
            } else {
                BlockPos.getAllInBox(pos.add(-range, -1, -range), pos.add(range, 0, range)).forEach(p -> {
                    if (this.hoeBlock((ServerWorld) world, p, stack, entity))
                        flag.set(true);
                });
            }
            if (flag.get() && entity instanceof PlayerEntity) {
                entity.getCapability(PlayerCapProvider.PlayerCap).ifPresent(cap -> {

                });
            }
        }
        super.onPlayerStoppedUsing(stack, world, entity, timeLeft);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (this.tier.getTierLevel() != 0) {
            player.setActiveHand(hand);
            return ActionResult.success(itemstack);
        }
        return ActionResult.pass(itemstack);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
        }
        return ActionResultType.PASS;
    }

    private ActionResultType useOnBlock(ItemUseContext ctx) {
        if (ctx.getWorld().isRemote)
            return ActionResultType.PASS;
        ItemStack stack = ctx.getItem();
        if (this.hoeBlock((ServerWorld) ctx.getWorld(), ctx.getPos(), stack, ctx.getPlayer())) {
            this.onBlockBreak((ServerPlayerEntity) ctx.getPlayer());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean hoeBlock(ServerWorld world, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if(!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).canPlayerEdit(pos.offset(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = world.getBlockState(pos);
        BlockState blockstate = state.getToolModifiedState(world, pos, (PlayerEntity) entity, stack, ToolType.HOE);
        if(blockstate != null && world.getBlockState(pos.up()).getMaterial() == Material.AIR){
            world.setBlockState(pos, blockstate, 3);
            world.playSound(null, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0f, 1.1f);
            return true;
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
