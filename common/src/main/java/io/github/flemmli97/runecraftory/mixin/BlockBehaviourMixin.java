package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import io.github.flemmli97.runecraftory.common.utils.CropUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

    @ModifyReturnValue(method = "getDrops", at = @At("TAIL"))
    private List<ItemStack> modifyBlockDrops(List<ItemStack> original, BlockState state, LootParams.Builder params) {
        if (state.getBlock() instanceof CropBlock cropBlock)
            CropUtils.modifyCropDrops(state, params, cropBlock, original);
        return original;
    }
}
