package io.github.flemmli97.runecraftory.mixin;

import io.github.flemmli97.runecraftory.common.events.EntityCalls;
import io.github.flemmli97.runecraftory.common.utils.ItemNBT;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "mineBlock", at = @At("HEAD"))
    private void onBlockMine(Level level, BlockState state, BlockPos pos, Player player, CallbackInfo info) {
        if (player instanceof ServerPlayer serverPlayer)
            EntityCalls.onBlockBreak(serverPlayer, state, pos);
    }

    @ModifyVariable(method = "getTooltipLines", at = @At(value = "FIELD", args = "Lnet/minecraft/world/item/ItemStack$TooltipPart;MODIFIERS:Lnet/minecraft/world/item/ItemStack$TooltipPart;", opcode = Opcodes.GETSTATIC, shift = At.Shift.BEFORE), ordinal = 0)
    private int hideTooltip(int old) {
        return ItemNBT.shouldHaveStats((ItemStack) (Object) this) ? old | ItemStack.TooltipPart.MODIFIERS.getMask() : old;
    }
}
