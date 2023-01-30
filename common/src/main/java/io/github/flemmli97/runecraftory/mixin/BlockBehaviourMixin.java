package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.blocks.BlockCrop;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourMixin {

    @Inject(method = "getDrops", at = @At("TAIL"))
    private void modifyBlockDrops(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> info) {
        if (state.getBlock() instanceof CropBlock cropBlock)
            BlockCrop.modifyCropDrops(state, builder, cropBlock, info.getReturnValue());
    }
}
