package io.github.flemmli97.runecraftory.common.items.tools;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.api.enums.EnumToolTier;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.lib.ItemTiers;
import io.github.flemmli97.runecraftory.common.registry.ModDataComponentTypes;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ItemToolHoe extends HoeItem {

    public ItemToolHoe(Item.Properties props) {
        super(ItemTiers.TIER, props);
    }

    public static void onHoeUse(ServerPlayer player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        LevelCalc.useRP(data, 3, true, 0, true, EnumSkills.FARMING, EnumSkills.EARTH);
        LevelCalc.levelSkill(data, EnumSkills.FARMING, 3);
        LevelCalc.levelSkill(data, EnumSkills.EARTH, 1.5f);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingUseDuration) {
        EnumToolTier tier = stack.getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
        if (entity instanceof ServerPlayer player) {
            int duration = stack.getUseDuration(entity) - remainingUseDuration;
            int chargeTime = ItemUtils.getChargeTime(entity, tier);
            if (duration > 0 && duration / chargeTime <= tier.getTierLevel() && duration % chargeTime == 0)
                EntityUtils.playSoundForPlayer(player, SoundEvents.NOTE_BLOCK_XYLOPHONE, 1, 1);
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        EnumToolTier tier = ctx.getItemInHand().getOrDefault(ModDataComponentTypes.TOOL_TIER.get(), EnumToolTier.SCRAP);
        if (tier.getTierLevel() == 0) {
            return this.useOnBlock(ctx);
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
            int useTime = (stack.getUseDuration(entity) - timeLeft - 1) / ItemUtils.getChargeTime(entity, tier);
            int range = Math.min(useTime, tier.getTierLevel());
            BlockHitResult result = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);
            if (range == 0) {
                this.useOnBlock(new UseOnContext(player, entity.getUsedItemHand(), result));
            } else {
                BlockPos pos = entity.blockPosition().below();
                if (result.getType() != HitResult.Type.MISS) {
                    pos = result.getBlockPos();
                }
                Function<BlockPos, BlockHitResult> hit = bh -> new BlockHitResult(Vec3.atCenterOf(bh), Direction.UP, bh, false);
                int amount = (int) BlockPos.betweenClosedStream(pos.offset(-range, 0, -range), pos.offset(range, 0, range))
                        .filter(p -> this.hoeBlock(new UseOnContext(player, entity.getUsedItemHand(), hit.apply(p.immutable()))))
                        .count();
                if (amount > 0) {
                    PlayerData data = Platform.INSTANCE.getPlayerData(player);
                    LevelCalc.useRP(data, 0, true, range * 17.5f, true, EnumSkills.FARMING);
                    LevelCalc.levelSkill(data, EnumSkills.FARMING, range * 15);
                    LevelCalc.levelSkill(data, EnumSkills.EARTH, range * 2);
                }
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

    private InteractionResult useOnBlock(UseOnContext ctx) {
        if (ctx.getLevel().isClientSide)
            return InteractionResult.PASS;
        if (this.hoeBlock(ctx)) {
            onHoeUse((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean hoeBlock(UseOnContext ctx) {
        BlockState state = ctx.getLevel().getBlockState(ctx.getClickedPos());
        Pair<Predicate<UseOnContext>, Consumer<UseOnContext>> tille = ItemToolHoe.TILLABLES.get(state.getBlock());
        if (tille != null && tille.getFirst().test(ctx)) {
            tille.getSecond().accept(ctx);
            if (ctx.getLevel().getBlockState(ctx.getClickedPos()).is(Blocks.FARMLAND))
                ctx.getLevel().setBlock(ctx.getClickedPos(), Blocks.FARMLAND.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            ctx.getLevel().playSound(null, ctx.getClickedPos(), SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.1f);
            return true;
        }
        return false;
    }
}
