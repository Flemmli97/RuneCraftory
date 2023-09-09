package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.items.tools.ItemToolHoe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Inject(method = "useOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/context/UseOnContext;getItemInHand()Lnet/minecraft/world/item/ItemStack;"))
    private void onHoeUse(UseOnContext ctx, CallbackInfoReturnable<InteractionResult> info) {
        if (ctx.getPlayer() instanceof ServerPlayer player && ctx.getLevel().getBlockState(ctx.getClickedPos()).is(Blocks.FARMLAND))
            ItemToolHoe.onHoeUse(player);
    }
}
