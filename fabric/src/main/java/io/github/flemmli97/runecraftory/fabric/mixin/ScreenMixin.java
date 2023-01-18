package io.github.flemmli97.runecraftory.fabric.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.fabric.mixinhelper.TooltipHelper;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(Screen.class)
public abstract class ScreenMixin {

    @Shadow
    private int width;
    @Shadow
    private int height;

    @Unique
    private ItemStack runecraftoryTooltipStack = ItemStack.EMPTY;

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V", at = @At("HEAD"))
    private void onItemStackTooltipPre(PoseStack poseStack, ItemStack itemStack, int mouseX, int mouseY, CallbackInfo info) {
        this.runecraftoryTooltipStack = itemStack;
    }

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/item/ItemStack;II)V",
            at = @At(value = "RETURN"))
    private void onItemStackTooltipPost(PoseStack poseStack, ItemStack itemStack, int mouseX, int mouseY, CallbackInfo info) {
        this.runecraftoryTooltipStack = ItemStack.EMPTY;
    }

    @Inject(method = "renderTooltip(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;Ljava/util/Optional;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/Screen;renderTooltipInternal(Lcom/mojang/blaze3d/vertex/PoseStack;Ljava/util/List;II)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void onItemStackTooltip(PoseStack poseStack, List<Component> tooltips, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY, CallbackInfo info, List<ClientTooltipComponent> components) {
        TooltipHelper.gatherComponents(this.runecraftoryTooltipStack, this.width, this.height, components);
    }
}
