package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderWindBlade extends EntityRenderer<EntityWindBlade> {
    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/wind_blade.png");

    public RenderWindBlade(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityWindBlade entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + 90.0f));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(-Mth.lerp(partialTicks, entity.xRotO, entity.getXRot())));
        PoseStack.Pose pose = matrixStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(this.getTextureLocation(entity)));
        matrixStack.scale(0.075f, 0.05f, 0.05f);
        for (int r = 0; r < 4; ++r) {
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(90.0f));
            this.vertex(matrix4f, matrix3f, vertexConsumer, -2, 2, 0, 0.0f, 1, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 2, 2, 0, 1, 1, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, 2, -2, 0, 1, 0.0f, 0, 1, 0, packedLight);
            this.vertex(matrix4f, matrix3f, vertexConsumer, -2, -2, 0, 0.0f, 0.0f, 0, 1, 0, packedLight);
        }
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    public void vertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int normalX, int i, int j, int packedLight) {
        vertexBuilder.vertex(matrix, offsetX, offsetY, offsetZ).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normals, normalX, j, i).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWindBlade entity) {
        return tex;
    }
}
