package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.blocks.tile.TileFarm;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFertilizer extends Item {

    private final IFertilizerOnUse use;

    public ItemFertilizer(IFertilizerOnUse use, Properties properties) {
        super(properties);
        this.use = use;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World world = ctx.getWorld();
        if (world.isRemote)
            return ActionResultType.PASS;
        BlockPos blockpos = ctx.getPos();
        TileEntity tile = world.getTileEntity(blockpos);
        if (tile instanceof TileFarm && this.use.useItemOnFarmland(ctx.getItem(), world, (TileFarm) tile, ctx.getPlayer())) {
            world.playEvent(2005, blockpos, 0);
            return ActionResultType.SUCCESS;
        } else if (world.getBlockState(blockpos).getBlock() instanceof BushBlock) {
            blockpos = blockpos.down();
            tile = world.getTileEntity(blockpos);
            if (tile instanceof TileFarm && this.use.useItemOnFarmland(ctx.getItem(), world, (TileFarm) tile, ctx.getPlayer())) {
                world.playEvent(2005, blockpos, 0);
                return ActionResultType.SUCCESS;
            }
        }
        return ActionResultType.PASS;
    }

    public interface IFertilizerOnUse {
        boolean useItemOnFarmland(ItemStack stack, World world, TileFarm tile, PlayerEntity player);

    }

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
}
