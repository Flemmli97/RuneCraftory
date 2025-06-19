package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMobArrow;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderMobArrow<T extends EntityMobArrow> extends EntityRenderer<T> {

    public static final ResourceLocation ARROW = ResourceLocation.withDefaultNamespace("textures/entity/projectiles/arrow.png");

    public RenderMobArrow(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) - 90.0F));
        stack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));

        stack.mulPose(Axis.XP.rotationDegrees(45.0F));
        stack.scale(0.05625F, 0.05625F, 0.05625F);
        stack.translate(-4.0D, 0.0D, 0.0D);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        PoseStack.Pose pose = stack.last();
        this.buildVertex(pose, consumer, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
        this.buildVertex(pose, consumer, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);

        for (int j = 0; j < 4; ++j) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.buildVertex(pose, consumer, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.buildVertex(pose, consumer, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
            this.buildVertex(pose, consumer, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
            this.buildVertex(pose, consumer, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
        }

        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return ARROW;
    }

    public void buildVertex(PoseStack.Pose pose, VertexConsumer consumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int packedLight) {
        consumer.addVertex(pose, x, y, z).setColor(255, 255, 255, 255).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(packedLight).setNormal(pose, normalX, normalY, normalZ);
    }
}
