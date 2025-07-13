package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.world.data.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemToolWateringCan extends Item {

    public ItemToolWateringCan(Item.Properties props) {
        super(props);
    }

    public void postUse(ServerPlayer player) {
        player.disableShield();
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        LevelCalc.useRP(data, 2, true, 0, true, Skills.FARMING, Skills.WATER);
        LevelCalc.levelSkill(data, Skills.FARMING, 4);
        LevelCalc.levelSkill(data, Skills.WATER, 1);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            ToolItemTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
            int chargeTime = ItemUtils.getChargeTime(entity, tier);
            if (duration > 0 && duration / chargeTime <= tier.getTierLevel() && duration % chargeTime == 0)
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ToolItemTier tier = ctx.getItemInHand().getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        BlockHitResult ray = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);
        ItemStack stack = player.getItemInHand(hand);
        BlockState state = world.getBlockState(ray.getBlockPos());
        if (state.getFluidState().getType() == Fluids.WATER) {
            stack.set(ModDataComponentTypes.WATER.get(), stack.getOrDefault(ModDataComponentTypes.MAX_WATER.get(), 0));
            world.setBlock(ray.getBlockPos(), state.getFluidState().createLegacyBlock(), 3);
            player.playSound(SoundEvents.BUCKET_FILL, 1.0f, 1.0f);
            return InteractionResultHolder.success(stack);
        }
        ToolItemTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        ToolItemTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            int useTime = (stack.getUseDuration(entity) - timeLeft - 1) / ItemUtils.getChargeTime(entity, tier);
            int range = Math.min(useTime, tier.getTierLevel());
            BlockHitResult result = getPlayerPOVHitResult(world, (Player) entity, ClipContext.Fluid.NONE);
            if (range == 0) {
                this.useOnBlock(new UseOnContext((Player) entity, entity.getUsedItemHand(), result));
            } else {
                BlockPos pos = entity.blockPosition().below();
                if (result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, -1, -range), pos.offset(range, 0, range))
                        .filter(p -> this.moisten((ServerLevel) world, p.immutable(), stack, entity))
                        .count();
                if (amount > 0) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player);
                    LevelCalc.useRP(data, 0, true, range * 17.5f, true, Skills.FARMING);
                    LevelCalc.levelSkill(data, Skills.FARMING, range * 10);
                    LevelCalc.levelSkill(data, Skills.WATER, range * 3);
                }
            }
        }
        super.releaseUsing(stack, world, entity, timeLeft);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        int water = stack.getOrDefault(ModDataComponentTypes.WATER.get(), 0);
        int max = stack.getOrDefault(ModDataComponentTypes.MAX_WATER.get(), 0);
        return (int) (water / (float) max * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        int water = stack.getOrDefault(ModDataComponentTypes.WATER.get(), 0);
        int max = stack.getOrDefault(ModDataComponentTypes.MAX_WATER.get(), 0);
        float f = Math.max(0.0f, water / (float) max);
        return Mth.hsvToRgb(f / 3.0f, 1.0f, 1.0f);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
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
            this.postUse((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean moisten(ServerLevel world, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player && !((Player) entity).mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return false;
        boolean creative = !(entity instanceof Player) || ((Player) entity).isCreative();
        BlockState state = world.getBlockState(pos);
        int water = stack.getOrDefault(ModDataComponentTypes.WATER.get(), 0);
        if ((creative || water > 0) && state.is(RunecraftoryTags.Blocks.FARMLAND) && state.getValue(FarmBlock.MOISTURE) != 7) {
            FarmlandHandler.waterLand(world, pos, state);
            if (!creative) {
                stack.set(ModDataComponentTypes.WATER.get(), water - 1);
            }
            return true;
        }
        return false;
    }
}
