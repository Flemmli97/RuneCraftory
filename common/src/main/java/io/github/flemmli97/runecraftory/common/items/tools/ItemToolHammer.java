package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.api.action.DataKey;
import io.github.flemmli97.runecraftory.api.action.ToolUseData;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModAttackActions;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ItemToolHammer extends PickaxeItem {

    public ItemToolHammer(Properties props) {
        super(ItemTiers.TIER, props);
    }

    public static void onHammering(ServerPlayer player, boolean level) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (data.getWeaponHandler().getCurrentAction() == ModAttackActions.TOOL_HAMMER_USE.get()) // Action does rp use itself
            return;
        LevelCalc.useRP(data, 5, true, 0, true, EnumSkills.MINING);
        if (level)
            LevelCalc.levelSkill(data, EnumSkills.MINING, 10);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            EnumToolTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
            int chargeTime = ItemUtils.getChargeTime(entity, tier);
            if (duration > 0 && duration / chargeTime <= tier.getTierLevel() && duration % chargeTime == 0)
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        EnumToolTier tier = ctx.getItemInHand().getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
        if (tier.getTierLevel() == 0) {
            return this.useOnSingleBlock(ctx, false);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        EnumToolTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
        if (tier.getTierLevel() != 0) {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity entity, int timeLeft) {
        EnumToolTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
        if (tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            PlayerData data = Platform.INSTANCE.getPlayerData(player);
            int useTime = data.getWeaponHandler().canExecuteAction(ModAttackActions.TOOL_HAMMER_USE.get(), false) ? data.getWeaponHandler().get(DataKey.TOOL_DATA).charge() : ((stack.getUseDuration(entity) - timeLeft - 1) / ItemUtils.getChargeTime(entity, tier));
            int range = Math.min(useTime, tier.getTierLevel());
            BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
            if (range == 0) {
                this.useOnSingleBlock(new UseOnContext((Player) entity, entity.getUsedItemHand(), result), false);
            } else {
                data.getWeaponHandler().doWeaponAttack(ModAttackActions.TOOL_HAMMER_USE.get(), stack);
                data.getWeaponHandler().store(DataKey.TOOL_DATA, new ToolUseData(result, range));
            }
        }
        super.releaseUsing(stack, world, entity, timeLeft);
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

    private InteractionResult useOnSingleBlock(UseOnContext ctx, boolean canHammer) {
        if (!(ctx.getPlayer() instanceof ServerPlayer player))
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        HammerState state = this.hammer((ServerLevel) ctx.getLevel(), ctx.getClickedPos(), stack, ctx.getPlayer(), canHammer);
        if (state != HammerState.FAIL) {
            onHammering(player, state == HammerState.BREAK);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public HammerState hammer(ServerLevel world, BlockPos pos, ItemStack stack, LivingEntity entity, boolean canHammer) {
        if (entity instanceof Player && !((Player) entity).mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return HammerState.FAIL;
        BlockState state = world.getBlockState(pos);
        if (canHammer && state.is(RunecraftoryTags.Blocks.HAMMER_BREAKABLE)) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.gameMode.destroyBlock(pos)) {
                    world.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, world.getBlockState(pos)));
                    return HammerState.BREAK;
                }
            } else {
                return world.destroyBlock(pos, true, entity, 3) ? HammerState.BREAK : HammerState.FAIL;
            }
        } else if (state.is(RunecraftoryTags.Blocks.HAMMER_FLATTENABLE) && world.getBlockState(pos.above()).isAir()) {
            if (world.setBlockAndUpdate(pos, Block.pushEntitiesUp(state, Blocks.DIRT.defaultBlockState(), world, pos))) {
                world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
                return HammerState.FLATTEN;
            }
        }
        return HammerState.FAIL;
    }

    public enum HammerState {
        FAIL,
        BREAK,
        FLATTEN
    }
}
