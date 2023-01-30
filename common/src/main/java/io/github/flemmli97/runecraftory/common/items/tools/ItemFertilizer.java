package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.network.S2CTriggers;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandData;
import io.github.flemmli97.runecraftory.common.world.farming.FarmlandHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.stream.Stream;

public class ItemFertilizer extends Item {

    private static final List<BlockPos> AFFECTED = List.of(
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, -1)
    );

    public static final IFertilizerOnUse FORMULAR_A = ((stack, level, data, player) -> {
        data.applyGrowthFertilizer(level, 0.4f);
        return true;
    });
    public static final IFertilizerOnUse FORMULAR_B = ((stack, level, data, player) -> {
        data.applyGrowthFertilizer(level, 1);
        return true;
    });
    public static final IFertilizerOnUse FORMULAR_C = ((stack, level, data, player) -> {
        data.applyGrowthFertilizer(level, 2);
        return true;
    });
    public static final IFertilizerOnUse GREENIFIER = ((stack, level, data, player) -> {
        data.modifyQuality(level, 0.2f);
        return true;
    });
    public static final IFertilizerOnUse GREENIFIER_PLUS = ((stack, level, data, player) -> {
        data.modifyQuality(level, 0.5f);
        return true;
    });
    public static final IFertilizerOnUse GIANTIZER = ((stack, level, data, player) -> {
        data.applySizeFertilizer(level, true);
        return true;
    });
    public static final IFertilizerOnUse MINIMIZER = ((stack, level, data, player) -> {
        data.applySizeFertilizer(level, false);
        return true;
    });
    public static final IFertilizerOnUse WETTABLE = ((stack, level, data, player) -> {
        data.modifyDefence(level, 48);
        return true;
    });
    private final IFertilizerOnUse use;

    public ItemFertilizer(IFertilizerOnUse use, Properties properties) {
        super(properties);
        this.use = use;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        if (!(ctx.getLevel() instanceof ServerLevel level))
            return InteractionResult.SUCCESS;
        InteractionResult res = this.applyTo(level, ctx.getClickedPos(), ctx, false);
        if (res == InteractionResult.CONSUME) {
            getOtherForTargeted(ctx.getPlayer() == null ? Direction.NORTH : ctx.getPlayer().getDirection(), ctx.getClickedPos())
                    .forEach(p -> this.applyTo(level, p, ctx, true));
            if (ctx.getPlayer() instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.isCreative())
                    ctx.getItemInHand().shrink(1);
                ModCriteria.FERTILIZE_FARM.trigger(serverPlayer);
            }
        }
        return res;
    }

    private InteractionResult applyTo(ServerLevel level, BlockPos blockPos, UseOnContext ctx, boolean consecutive) {
        BlockState state = level.getBlockState(blockPos);
        if (FarmlandHandler.isFarmBlock(state)) {
            return FarmlandHandler.get(level.getServer())
                    .getData(level, blockPos)
                    .map(d -> {
                        if (this.use.useItemOnFarmland(ctx.getItemInHand(), level, d, ctx.getPlayer())) {
                            Platform.INSTANCE.sendToAll(new S2CTriggers(S2CTriggers.Type.FERTILIZER, blockPos), level.getServer());
                            return InteractionResult.CONSUME;
                        }
                        return InteractionResult.PASS;
                    }).orElse(InteractionResult.PASS);
        } else if (consecutive || state.getBlock() instanceof BushBlock) {
            BlockPos below = blockPos.below();
            return FarmlandHandler.get(level.getServer())
                    .getData(level, below)
                    .map(d -> {
                        if (this.use.useItemOnFarmland(ctx.getItemInHand(), level, d, ctx.getPlayer())) {
                            Platform.INSTANCE.sendToAll(new S2CTriggers(S2CTriggers.Type.FERTILIZER, below), level.getServer());
                            return InteractionResult.CONSUME;
                        }
                        return InteractionResult.PASS;
                    }).orElse(InteractionResult.PASS);
        }
        return InteractionResult.PASS;
    }

    public interface IFertilizerOnUse {
        boolean useItemOnFarmland(ItemStack stack, ServerLevel level, FarmlandData data, Player player);
    }

    public static Stream<BlockPos> getOtherForTargeted(Direction direction, BlockPos ctxPos) {
        Rotation rot = switch (direction) {
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
        return AFFECTED.stream().map(p -> p.rotate(rot).offset(ctxPos));
    }
}
