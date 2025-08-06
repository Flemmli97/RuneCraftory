package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import io.github.flemmli97.runecraftory.common.blocks.entity.TreeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 1)
    private Set<BlockPos> handleLogSet(Set<BlockPos> logs, FeaturePlaceContext<TreeConfiguration> context,
                                       @Share("runecraftory_placingFruitTree") LocalRef<TreeBlockEntity> ref) {
        BlockEntity entity = context.level().getBlockEntity(context.origin());
        if (entity instanceof TreeBlockEntity tree) {
            ref.set(tree);
            tree.updateTreeLogs(context.level(), logs);
        }
        return logs;
    }

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 2)
    private Set<BlockPos> handleLeaveSet(Set<BlockPos> leaves, FeaturePlaceContext<TreeConfiguration> context,
                                         @Share("runecraftory_placingFruitTree") LocalRef<TreeBlockEntity> ref) {
        if (ref.get() != null) {
            ref.get().updateTreeLeaves(context.level(), leaves);
        }
        return leaves;
    }

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 3)
    private Set<BlockPos> handleDecoratorSet(Set<BlockPos> decorators, FeaturePlaceContext<TreeConfiguration> context,
                                             @Share("runecraftory_placingFruitTree") LocalRef<TreeBlockEntity> ref) {
        if (ref.get() != null) {
            ref.get().updateTreeFruits(context.level(), decorators);
        }
        return decorators;
    }
}
