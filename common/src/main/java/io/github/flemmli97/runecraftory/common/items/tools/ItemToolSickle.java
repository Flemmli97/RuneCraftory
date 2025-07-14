package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.ToolItemTier;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ItemToolSickle extends DiggerItem {

    public ItemToolSickle(Item.Properties props) {
        super(ItemTiers.TIER, RunecraftoryTags.Blocks.SICKLE_EFFECTIVE, props);
    }

    public void postUse(ServerPlayer player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        LevelCalc.useRP(data, 2, true, 0, true, Skills.FARMING, Skills.WIND);
        LevelCalc.levelSkill(data, Skills.FARMING, 3);
        LevelCalc.levelSkill(data, Skills.WIND, 2);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        Tool tool = stack.get(DataComponents.TOOL);
        if (tool != null && entityLiving instanceof ServerPlayer serverPlayer && tool.rules().stream().anyMatch(p -> state.is(p.blocks()))) {
            PlayerData data = Platform.INSTANCE.getPlayerData(serverPlayer);
            LevelCalc.levelSkill(data, Skills.FARMING, 3);
            LevelCalc.levelSkill(data, Skills.WIND, 2);
        }
        return super.mineBlock(stack, level, state, pos, entityLiving);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
            int chargeTime = ItemUtils.getChargeTime(entity, tier);
            if (duration > 0 && duration / chargeTime <= tier.getTierLevel() && duration % chargeTime == 0)
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ToolItemTier tier = ctx.getItemInHand().getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);
        ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0) {
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        ToolItemTier tier = stack.getOrDefault(RuneCraftoryDataComponentTypes.TOOL_TIER.get(), ToolItemTier.SCRAP);
        if (tier.getTierLevel() != 0 && entity instanceof ServerPlayer player) {
            int useTime = (stack.getUseDuration(entity) - timeLeft - 1) / ItemUtils.getChargeTime(entity, tier);
            int range = Math.min(useTime, tier.getTierLevel()) + 2;
            BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
            if (range == 0) {
                this.useOnBlock(new UseOnContext(player, entity.getUsedItemHand(), result));
            } else {
                BlockPos pos = entity.blockPosition();
                if (result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, 0, -range), pos.offset(range, 0, range))
                        .filter(p -> this.sickleUse(player.serverLevel(), p.immutable(), stack, entity))
                        .count();
                if (amount > 0) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player);
                    LevelCalc.useRP(data, range * 10, true, 0, true, Skills.FARMING);
                    LevelCalc.levelSkill(data, Skills.FARMING, 3.5f);
                    LevelCalc.levelSkill(data, Skills.WIND, 2.5f);
                }
            }
        }
        super.releaseUsing(stack, level, entity, timeLeft);
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
        if (!(ctx.getLevel() instanceof ServerLevel serverLevel))
            return InteractionResult.PASS;
        ItemStack stack = ctx.getItemInHand();
        if (this.sickleUse(serverLevel, ctx.getClickedPos(), stack, ctx.getPlayer())) {
            this.postUse((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean sickleUse(ServerLevel level, BlockPos pos, ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.mayUseItemAt(pos.relative(Direction.UP), Direction.UP, stack))
            return false;
        BlockState state = level.getBlockState(pos);
        if (state.is(RunecraftoryTags.Blocks.SICKLE_DESTROYABLE)) {
            if (entity instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.gameMode.destroyBlock(pos)) {
                    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(state));
                    serverPlayer.connection.send(new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
                    return true;
                }
            } else {
                return level.destroyBlock(pos, true, entity, 3);
            }
        }
        return false;
    }
}
