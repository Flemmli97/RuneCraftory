package com.flemmli97.runecraftory.common.items.tools;

import com.flemmli97.runecraftory.api.enums.EnumToolCharge;
import com.flemmli97.runecraftory.api.enums.EnumToolTier;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IChargeable;
import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.common.capability.PlayerCapProvider;
import com.flemmli97.runecraftory.common.config.GeneralConfig;
import com.flemmli97.runecraftory.common.registry.ModBlocks;
import com.flemmli97.runecraftory.common.utils.ItemNBT;
import com.flemmli97.runecraftory.lib.ItemTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
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

import java.util.concurrent.atomic.AtomicBoolean;

public class ItemToolWateringCan extends ToolItem implements IItemUsable, IChargeable {

    private final EnumToolTier tier;

    public ItemToolWateringCan(EnumToolTier tier, Item.Properties props) {
        super(0, 0, ItemTiers.tier, Sets.newHashSet(), props);
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
        return EnumToolCharge.CHARGECAN;
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
    public double getDurabilityForDisplay(ItemStack stack) {
        if (!stack.hasTag()) {
            ItemNBT.initNBT(stack, true);
        }
        return 1.0f - stack.getTag().getInt("Water") / (float) this.maxWater();
    }

    public int maxWater() {
        switch (this.tier) {
            case IRON:
                return GeneralConfig.ironWateringCanWater;
            case SILVER:
                return GeneralConfig.silverWateringCanWater;
            case GOLD:
                return GeneralConfig.goldWateringCanWater;
            case PLATINUM:
                return GeneralConfig.platinumWateringCanWater;
            default:
                return GeneralConfig.scrapWateringCanWater;
        }
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
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
                    if (this.moisten((ServerWorld) world, p, stack, entity))
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
        BlockRayTraceResult ray = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);
        ItemStack itemstack = player.getHeldItem(hand);
        if (ray != null && world.getFluidState(ray.getPos()).getFluid() == Fluids.WATER) {
            BlockState state = world.getBlockState(ray.getPos());
            if(state.getBlock() instanceof IBucketPickupHandler && ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, ray.getPos(), state) != Fluids.EMPTY) {
                if (!itemstack.hasTag()) {
                    ItemNBT.initNBT(itemstack);
                }
                itemstack.getOrCreateTag().putInt("Water", this.maxWater());
                world.setBlockState(ray.getPos(), Blocks.AIR.getDefaultState(), 3);
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0f, 1.0f);
                return ActionResult.success(itemstack);
            }
        }
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
        PlayerEntity player = ctx.getPlayer();
        BlockPos pos = ctx.getPos();
        if (this.moisten((ServerWorld) ctx.getWorld(), pos, stack, player) || this.moisten((ServerWorld) ctx.getWorld(), pos.down(), stack, player)) {
            this.onBlockBreak((ServerPlayerEntity) ctx.getPlayer());
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    private boolean moisten(ServerWorld world, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if(entity instanceof PlayerEntity && !((PlayerEntity) entity).canPlayerEdit(pos.offset(Direction.UP), Direction.UP, stack))
            return false;
        boolean creative = !(entity instanceof PlayerEntity) || ((PlayerEntity) entity).isCreative();
        BlockState state = world.getBlockState(pos);
        if (!stack.hasTag()) {
            ItemNBT.initNBT(stack);
        }
        int water = stack.getOrCreateTag().getInt("Water");
        if ((creative || water > 0) && state.isIn(ModBlocks.farmland.get()) && state.get(FarmlandBlock.MOISTURE) == 0) {
            world.spawnParticle(ParticleTypes.FISHING, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, 4, 0.0, 0.01, 0.0, 0.1D);
            world.setBlockState(pos, state.with(FarmlandBlock.MOISTURE, 7), 3);
            world.playSound(null, pos, SoundEvents.ENTITY_BOAT_PADDLE_WATER, SoundCategory.BLOCKS, 1.0f, 1.1f);
            if (!creative) {
                stack.getTag().putInt("Water", water - 1);
            }
            return true;
        }
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return ImmutableMultimap.of();
    }
}
