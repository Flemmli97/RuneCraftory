package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.common.entities.misc.ThrownItemEntity;
import io.github.flemmli97.runecraftory.common.items.creative.ItemProp;
import io.github.flemmli97.tenshilib.client.render.ItemProjectileRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class ThrownItemRender extends ItemProjectileRenderer<ThrownItemEntity> {

    public ThrownItemRender(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(ThrownItemEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        if (entity.isRotating()) {
            stack.mulPose(Axis.ZP.rotationDegrees(entity.tickCount * 60));
            stack.translate(0, -0.3, 0);
        }
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public Type getRenderType(ThrownItemEntity entity) {
        return Type.NORMAL;
    }

    @Override
    public ItemStack getRenderItemStack(ThrownItemEntity entity) {
        ItemStack stack = entity.getItem();
        if (stack.getItem() instanceof ItemProp prop)
            return prop.clientItemStack();
        return stack;
    }
}