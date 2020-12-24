package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.registry.ModTags;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemToolHammer extends PickaxeItem implements IItemUsable, IChargeable {

    public final EnumToolTier tier;
    private static AxisAlignedBB farmlandTop = new AxisAlignedBB(0.0, 0.9375, 0.0, 1.0, 1.0, 1.0);
    private int[] chargeRunes = new int[]{1, 5, 15, 50, 100};

    public ItemToolHammer(EnumToolTier tier, Properties props) {
        super(ItemTiers.tier, 0, 0, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
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
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.HAMMERAXE, 0.5f));
    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.MINING, this.tier.getTierLevel() + 1));
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
                        this.useOnBlock(new ItemUseContext((PlayerEntity) entity, entity.getActiveHand(), result), false);
                        return;
                    }
                }
            } else {
                BlockPos.getAllInBox(pos.add(-range, -1, -range), pos.add(range, 0, range)).forEach(p -> {
                    if (this.hammer((ServerWorld) world, p, stack, entity, true))
                        flag.set(true);
                });
            }
            if (flag.get() && entity instanceof PlayerEntity) {
                entity.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {

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
            return this.useOnBlock(ctx, false);
        }
        return ActionResultType.PASS;
    }

    private ActionResultType useOnBlock(ItemUseContext ctx, boolean canHammer) {
        if (ctx.getWorld().isRemote)
            return ActionResultType.PASS;
        ItemStack stack = ctx.getItem();
        if (this.hammer((ServerWorld) ctx.getWorld(), ctx.getPos(), stack, ctx.getPlayer(), canHammer)) {
            this.onBlockBreak((ServerPlayerEntity) ctx.getPlayer());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean hammer(ServerWorld world, BlockPos pos, ItemStack stack, LivingEntity entity, boolean canHammer) {
        if (entity instanceof PlayerEntity && !((PlayerEntity) entity).canPlayerEdit(pos.offset(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = world.getBlockState(pos);
        if (canHammer && state.isIn(ModTags.hammerBreakable)) {
            if (entity instanceof ServerPlayerEntity) {
                if (((ServerPlayerEntity) entity).interactionManager.tryHarvestBlock(pos)) {
                    world.playEvent(2001, pos, Block.getStateId(state));
                    ((ServerPlayerEntity) entity).connection.sendPacket(new SChangeBlockPacket(pos, world.getBlockState(pos)));
                    return true;
                }
            } else {
                return world.breakBlock(pos, true, entity, 3);
            }
        } else if (state.isIn(ModTags.hammerFlattenable) && world.getBlockState(pos.up()).getMaterial() == Material.AIR) {
            if (world.setBlockState(pos, Block.nudgeEntitiesWithNewState(state, Blocks.DIRT.getDefaultState(), world, pos))) {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLOCK_STONE_BREAK, SoundCategory.BLOCKS, 1, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
