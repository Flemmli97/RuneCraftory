package io.github.flemmli97.runecraftory.fabric.mixin;

import io.github.flemmli97.runecraftory.fabric.mixinhelper.TooltipHelper;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {

    @Unique
    private ItemStack runecraftory$TooltipStack = ItemStack.EMPTY;

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"))
    private void onItemStackTooltipPre(Font font, ItemStack stack, int mouseX, int mouseY, CallbackInfo info) {
        this.runecraftory$TooltipStack = stack;
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V", at = @At(value = "RETURN"))
    private void onItemStackTooltipPost(Font font, ItemStack stack, int mouseX, int mouseY, CallbackInfo info) {
        this.runecraftory$TooltipStack = ItemStack.EMPTY;
    }

    @ModifyVariable(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"),
            ordinal = 1)
    private List<ClientTooltipComponent> onItemStackTooltip(List<ClientTooltipComponent> list) {
        TooltipHelper.gatherComponents(this.runecraftory$TooltipStack, list);
        return list;
    }
}
