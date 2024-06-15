package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityThrownItem;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RenderThrownItem extends RenderProjectileItem<EntityThrownItem> {

    public RenderThrownItem(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ItemStack getRenderItemStack(EntityThrownItem entity) {
        ItemStack stack = entity.getItem();
        if (stack.getItem() instanceof ItemProp prop)
            return prop.clientItemStack();
        return stack;
    }

    @Override
    public void render(EntityThrownItem entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        if (entity.isRotating()) {
            stack.mulPose(Vector3f.ZP.rotationDegrees(entity.tickCount * 60));
            stack.translate(0, -0.3, 0);
        }
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public Type getRenderType(EntityThrownItem entity) {
        return Type.NORMAL;
    }
}