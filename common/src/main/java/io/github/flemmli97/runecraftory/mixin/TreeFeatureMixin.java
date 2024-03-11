package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.blocks.tile.TreeBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(TreeFeature.class)
public abstract class TreeFeatureMixin {

    @Unique
    private TreeBlockEntity runecraftory_placingFruitTree;

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 0)
    private Set<BlockPos> handleLogSet(Set<BlockPos> logs, FeaturePlaceContext<TreeConfiguration> context) {
        BlockEntity entity = context.level().getBlockEntity(context.origin());
        if (entity instanceof TreeBlockEntity tree) {
            this.runecraftory_placingFruitTree = tree;
            tree.updateTreeLogs(logs);
        }
        return logs;
    }

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 1)
    private Set<BlockPos> handleLeaveSet(Set<BlockPos> leaves) {
        if (this.runecraftory_placingFruitTree != null) {
            this.runecraftory_placingFruitTree.updateTreeLeaves(leaves);
        }
        return leaves;
    }

    @ModifyVariable(method = "place", at = @At("TAIL"), ordinal = 2)
    private Set<BlockPos> handleDecoratorSet(Set<BlockPos> decorators) {
        if (this.runecraftory_placingFruitTree != null) {
            this.runecraftory_placingFruitTree.updateTreeFruits(decorators);
        }
        return decorators;
    }
}
