package io.github.flemmli97.runecraftory.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.mixinhelper.ClientMixinUtils;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {

    /**
     * This is a common mixin because
     * 1. Equivalent doesn't exist on fabric
     * 2. The neo event does not capture the player
     */
    @WrapOperation(method = "renderHandsWithItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderArmWithItem(Lnet/minecraft/client/player/AbstractClientPlayer;FFLnet/minecraft/world/InteractionHand;FLnet/minecraft/world/item/ItemStack;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"))
    private void onRender(ItemInHandRenderer instance, AbstractClientPlayer entity,
                          float partialTick, float pitch, InteractionHand hand, float swingProgress, ItemStack itemStack, float equippedProgress,
                          PoseStack stack, MultiBufferSource buffer, int combinedLight, Operation<Void> original) {
        boolean right = (hand == InteractionHand.MAIN_HAND ? entity.getMainArm() : entity.getMainArm().getOpposite()) == HumanoidArm.RIGHT;
        if (!ClientMixinUtils.onRenderHeldItem(entity, itemStack, right ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, !right, buffer, combinedLight, partialTick)) {
            original.call(instance, entity, partialTick, pitch, hand, swingProgress, itemStack, equippedProgress, stack, buffer, combinedLight);
        }
    }
}
