package io.github.flemmli97.runecraftory.common.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class BlockPlaceCtxHelper extends BlockPlaceContext {

    protected BlockPlaceCtxHelper(Level level, @Nullable Player player, InteractionHand interactionHand, ItemStack itemStack, BlockHitResult blockHitResult) {
        super(level, player, interactionHand, itemStack, blockHitResult);
    }

    public static BlockPlaceContext of(Level level, @Nullable Player player, InteractionHand interactionHand, ItemStack itemStack, BlockHitResult blockHitResult) {
        return new BlockPlaceCtxHelper(level, player, interactionHand, itemStack, blockHitResult);
    }

    public static BlockPlaceContext entityPlaceAt(Level level, ItemStack itemStack, BlockPos pos, Direction placeDir) {
        return new BlockPlaceCtxHelper(level, null, InteractionHand.MAIN_HAND, itemStack,
                new BlockHitResult(new Vec3(pos.getX() + 0.5 + placeDir.getStepX() * 0.5, pos.getY() + 0.5 + placeDir.getStepY() * 0.5, pos.getZ() + 0.5 + placeDir.getStepZ() * 0.5), placeDir, pos, false));
    }
}
