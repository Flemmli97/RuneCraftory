package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolCharge;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.api.enums.EnumWeaponType;
import io.github.flemmli97.runecraftory.api.items.IChargeable;
import io.github.flemmli97.runecraftory.api.items.IItemUsable;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.lib.LibNBT;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemToolWateringCan extends TieredItem implements IItemUsable, IChargeable {

    private final EnumToolTier tier;

    public ItemToolWateringCan(EnumToolTier tier, Item.Properties props) {
        super(ItemTiers.TIER, props);
        this.tier = tier;
    }

    @Override
    public int getChargeTime(ItemStack stack) {
        if (this.tier == EnumToolTier.PLATINUM)
            return (int) (DataPackHandler.INSTANCE.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime() * GeneralConfig.platinumChargeTime);
        return DataPackHandler.INSTANCE.weaponPropertiesManager().getPropertiesFor(this.getWeaponType()).chargeTime();
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
    public boolean hasCooldown() {
        return true;
    }

    @Override
    public EnumWeaponType getWeaponType() {
        return EnumWeaponType.FARM;
    }

    @Override
    public void onBlockBreak(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            LevelCalc.useRP(player, data, 2, true, 0, true, EnumSkills.FARMING, EnumSkills.WATER);
            LevelCalc.levelSkill(player, data, EnumSkills.FARMING, 4);
            LevelCalc.levelSkill(player, data, EnumSkills.WATER, 1);
        });
    }

    public int maxWater() {
        return switch (this.tier) {
            case IRON -> GeneralConfig.ironWateringCanWater;
            case SILVER -> GeneralConfig.silverWateringCanWater;
            case GOLD -> GeneralConfig.goldWateringCanWater;
            case PLATINUM -> GeneralConfig.platinumWateringCanWater;
            default -> GeneralConfig.scrapWateringCanWater;
        };
    }

    public int getWater(ItemStack stack) {
        return stack.getOrCreateTag().getInt(LibNBT.WATERING_CAN_WATER);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if (livingEntity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration() - remainingUseDuration;
            if (duration > 0 && duration / this.getChargeTime(stack) <= this.chargeAmount(stack) && duration % this.getChargeTime(stack) == 0)
                player.connection.send(new ClientboundSoundPacket(SoundEvents.NOTE_BLOCK_XYLOPHONE, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (this.tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        ItemStack itemstack = player.getItemInHand(hand);
        BlockState state = world.getBlockState(ray.getBlockPos());
        if (state.getFluidState().getType() == Fluids.WATER) {
            itemstack.getOrCreateTag().putInt(LibNBT.WATERING_CAN_WATER, this.maxWater());
            world.setBlock(ray.getBlockPos(), state.getFluidState().createLegacyBlock(), 3);
            player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
            return InteractionResultHolder.success(itemstack);
        }
        if (this.tier.getTierLevel() != 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return (int) (this.getWater(stack) / (float) this.maxWater() * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        float f = Math.max(0.0f, this.getWater(stack) / (float) this.maxWater());
        return Mth.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        if (this.tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            int useTime = (stack.getUseDuration() - timeLeft - 1) / this.getChargeTime(stack);
            int range = Math.min(useTime, this.tier.getTierLevel());
            BlockHitResult result = getPlayerPOVHitResult(world, (Player) entity, ClipContext.Fluid.NONE);
            if (range == 0) {
                if (result != null) {
                    this.useOnBlock(new UseOnContext((Player) entity, entity.getUsedItemHand(), result));
                }
            } else {
                BlockPos pos = entity.blockPosition().below();
                if (result != null && result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                        .filter(p -> this.moisten((ServerLevel) world, p.immutable(), stack, entity))
                        .count();
                if (amount > 0)
                    Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
                        LevelCalc.useRP(player, data, 0, true, range * 17.5f, true, EnumSkills.FARMING);
                        LevelCalc.levelSkill(player, data, EnumSkills.FARMING, range * 10);
                        LevelCalc.levelSkill(player, data, EnumSkills.WATER, range * 3);
                    });
            }
        }
        super.releaseUsing(stack, world, entity, timeLeft);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return this.tier == EnumToolTier.PLATINUM ? Rarity.EPIC : Rarity.COMMON;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    private InteractionResult useOnBlock(UseOnContext ctx) {
        if (ctx.getLevel().isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        Player player = ctx.getPlayer();
        BlockPos pos = ctx.getClickedPos();
        if (this.moisten((ServerLevel) ctx.getLevel(), pos, stack, player) || this.moisten((ServerLevel) ctx.getLevel(), pos.below(), stack, player)) {
            this.onBlockBreak((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean moisten(ServerLevel world, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player && !((Player) entity).mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return false;
        boolean creative = !(entity instanceof Player) || ((Player) entity).isCreative();
        BlockState state = world.getBlockState(pos);
        int water = this.getWater(stack);
        if ((creative || water > 0) && state.is(RunecraftoryTags.FARMLAND) && state.getValue(FarmBlock.MOISTURE) != 7) {
            FarmlandHandler.waterLand(world, pos, state);
            if (!creative) {
                stack.getTag().putInt(LibNBT.WATERING_CAN_WATER, water - 1);
            }
            return true;
        }
        return false;
    }
}
