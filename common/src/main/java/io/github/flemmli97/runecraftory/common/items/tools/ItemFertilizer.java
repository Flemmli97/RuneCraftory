package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import io.github.flemmli97.runecraftory.common.network.S2CTriggers;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.stream.Stream;

public class ItemFertilizer extends Item {

    private static final List<BlockPos> affected = List.of(
            new BlockPos(1, 0, 0),
            new BlockPos(0, 0, -1),
            new BlockPos(1, 0, -1)
    );

    public static final IFertilizerOnUse formularA = ((stack, world, tile, player) -> {
        tile.applyGrowthFertilizer(0.4f);
        return true;
    });
    public static final IFertilizerOnUse formularB = ((stack, world, tile, player) -> {
        tile.applyGrowthFertilizer(1);
        return true;
    });
    public static final IFertilizerOnUse formularC = ((stack, world, tile, player) -> {
        tile.applyGrowthFertilizer(2);
        return true;
    });
    public static final IFertilizerOnUse greenifier = ((stack, world, tile, player) -> {
        tile.applyLevelFertilizer(0.2f);
        return true;
    });
    public static final IFertilizerOnUse greenifierPlus = ((stack, world, tile, player) -> {
        tile.applyLevelFertilizer(0.5f);
        return true;
    });
    public static final IFertilizerOnUse giantizer = ((stack, world, tile, player) -> {
        tile.applySizeFertilizer(true);
        return true;
    });
    public static final IFertilizerOnUse minimizer = ((stack, world, tile, player) -> {
        tile.applySizeFertilizer(false);
        return true;
    });
    public static final IFertilizerOnUse wettable = ((stack, world, tile, player) -> {
        tile.applyHealth(50);
        return true;
    });
    private final IFertilizerOnUse use;

    public ItemFertilizer(IFertilizerOnUse use, Properties properties) {
        super(properties);
        this.use = use;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (level.isClientSide)
            return InteractionResult.SUCCESS;
        InteractionResult res = this.applyTo(level, ctx.getClickedPos(), ctx);
        if (res == InteractionResult.CONSUME) {
            getOtherForTargeted(ctx.getPlayer() == null ? Direction.NORTH : ctx.getPlayer().getDirection(), ctx.getClickedPos())
                    .forEach(p -> this.applyTo(level, p, ctx));
            if (ctx.getPlayer() instanceof ServerPlayer serverPlayer) {
                if (!serverPlayer.isCreative())
                    ctx.getItemInHand().shrink(1);
                ModCriteria.FERTILIZE_FARM.trigger(serverPlayer);
            }
        }
        return res;
    }

    private InteractionResult applyTo(Level level, BlockPos blockpos, UseOnContext ctx) {
        BlockEntity blockEntity = level.getBlockEntity(blockpos);
        if (blockEntity instanceof FarmBlockEntity && this.use.useItemOnFarmland(ctx.getItemInHand(), level, (FarmBlockEntity) blockEntity, ctx.getPlayer())) {
            Platform.INSTANCE.sendToAll(new S2CTriggers(S2CTriggers.Type.FERTILIZER, blockpos), level.getServer());
            return InteractionResult.CONSUME;
        } else if (level.getBlockState(blockpos).getBlock() instanceof BushBlock) {
            blockpos = blockpos.below();
            blockEntity = level.getBlockEntity(blockpos);
            if (blockEntity instanceof FarmBlockEntity && this.use.useItemOnFarmland(ctx.getItemInHand(), level, (FarmBlockEntity) blockEntity, ctx.getPlayer())) {
                Platform.INSTANCE.sendToAll(new S2CTriggers(S2CTriggers.Type.FERTILIZER, blockpos), level.getServer());
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public interface IFertilizerOnUse {
        boolean useItemOnFarmland(ItemStack stack, Level world, FarmBlockEntity tile, Player player);
    }

    public static Stream<BlockPos> getOtherForTargeted(Direction direction, BlockPos ctxPos) {
        Rotation rot = switch (direction) {
            case SOUTH -> Rotation.CLOCKWISE_180;
            case EAST -> Rotation.CLOCKWISE_90;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE;
        };
        return affected.stream().map(p -> p.rotate(rot).offset(ctxPos));
    }
}
