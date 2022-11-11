package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.blocks.tile.FarmBlockEntity;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemFertilizer extends Item {

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
        Level world = ctx.getLevel();
        if (world.isClientSide)
            return InteractionResult.PASS;
        BlockPos blockpos = ctx.getClickedPos();
        BlockEntity blockEntity = world.getBlockEntity(blockpos);
        if (blockEntity instanceof FarmBlockEntity && this.use.useItemOnFarmland(ctx.getItemInHand(), world, (FarmBlockEntity) blockEntity, ctx.getPlayer())) {
            world.levelEvent(2005, blockpos, 0);
            if (ctx.getPlayer() != null && ctx.getPlayer().isCreative())
                ctx.getItemInHand().shrink(1);
            ModCriteria.FERTILIZE_FARM.trigger((ServerPlayer) ctx.getPlayer());
            return InteractionResult.SUCCESS;
        } else if (world.getBlockState(blockpos).getBlock() instanceof BushBlock) {
            blockpos = blockpos.below();
            blockEntity = world.getBlockEntity(blockpos);
            if (blockEntity instanceof FarmBlockEntity && this.use.useItemOnFarmland(ctx.getItemInHand(), world, (FarmBlockEntity) blockEntity, ctx.getPlayer())) {
                world.levelEvent(2005, blockpos, 0);
                if (ctx.getPlayer() != null && ctx.getPlayer().isCreative())
                    ctx.getItemInHand().shrink(1);
                ModCriteria.FERTILIZE_FARM.trigger((ServerPlayer) ctx.getPlayer());
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    public interface IFertilizerOnUse {
        boolean useItemOnFarmland(ItemStack stack, Level world, FarmBlockEntity tile, Player player);

    }
}
