package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.lib.ItemTiers;
import com.flemmli97.runecraftory.common.registry.ModTags;
import com.flemmli97.runecraftory.common.utils.LevelCalc;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.Rarity;
import net.minecraft.item.ToolItem;
import net.minecraft.item.UseAction;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class ItemToolSickle extends ToolItem implements IItemUsable, IChargeable {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.CACTUS, Blocks.CHORUS_FLOWER, Blocks.CHORUS_PLANT, Blocks.PUMPKIN, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, Blocks.VINE);

    public final EnumToolTier tier;

    public ItemToolSickle(EnumToolTier tier, Item.Properties props) {
        super(0, 0, ItemTiers.tier, EFFECTIVE_ON, props);
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
    public Rarity getRarity(ItemStack stack) {
        return this.tier == EnumToolTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON;
    }

    @Override
    public EnumToolCharge chargeType(ItemStack stack) {
        return EnumToolCharge.CHARGESICKLE;
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
                .ifPresent(cap -> LevelCalc.levelSkill(player, cap, EnumSkills.WIND, 0.3f));
    }

    @Override
    public void onBlockBreak(ServerPlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap)
                .ifPresent(cap -> {
                    LevelCalc.levelSkill(player, cap, EnumSkills.FARMING, this.tier.getTierLevel() * 0.5f + 1);
                    LevelCalc.levelSkill(player, cap, EnumSkills.WIND, this.tier.getTierLevel() * 0.4f + 0.5f);
                });
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
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.isIn(BlockTags.LEAVES) || state.isIn(BlockTags.WART_BLOCKS))
            return this.efficiency;
        //if (getToolTypes(stack).stream().anyMatch(e -> state.isToolEffective(e))) return efficiency;
        return super.getDestroySpeed(stack, state);
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
        if (duration != 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
            player.playSound(SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 1);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && !world.isRemote) {
            int useTime = (this.getUseDuration(stack) - timeLeft) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel());
            BlockPos pos = entity.getPosition();
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
                    if (this.sickleUse((ServerWorld) world, p, stack, entity))
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
            return ActionResult.resultSuccess(itemstack);
        }
        return ActionResult.resultPass(itemstack);
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
        if (this.sickleUse((ServerWorld) ctx.getWorld(), ctx.getPos(), stack, ctx.getPlayer())) {
            this.onBlockBreak((ServerPlayerEntity) ctx.getPlayer());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean sickleUse(ServerWorld world, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if (entity instanceof PlayerEntity && !((PlayerEntity) entity).canPlayerEdit(pos.offset(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = world.getBlockState(pos);
        if (state.isIn(ModTags.sickleDestroyable)) {
            if (entity instanceof ServerPlayerEntity) {
                if (((ServerPlayerEntity) entity).interactionManager.tryHarvestBlock(pos)) {
                    world.playEvent(2001, pos, Block.getStateId(state));
                    ((ServerPlayerEntity) entity).connection.sendPacket(new SChangeBlockPacket(pos, world.getBlockState(pos)));
                    return true;
                }
            } else {
                return world.destroyBlock(pos, true, entity, 3);
            }
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
