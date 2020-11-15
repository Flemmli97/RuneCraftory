package com.flemmli97.runecraftory.mobs.client.render.projectiles;

import com.flemmli97.runecraftory.mobs.entity.projectiles.EntityMobArrow;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;

public class RenderMobArrow<T extends EntityMobArrow> extends EntityRenderer<T> {

    public static final ResourceLocation ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public RenderMobArrow(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(T entity, float rotation, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        stack.push();
        stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(partialTicks, entity.prevRotationYaw, entity.rotationYaw) - 90.0F));
        stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(partialTicks, entity.prevRotationPitch, entity.rotationPitch)));

        stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(45.0F));
        stack.scale(0.05625F, 0.05625F, 0.05625F);
        stack.translate(-4.0D, 0.0D, 0.0D);
        IVertexBuilder ivertexbuilder = buffer.getBuffer(RenderType.getEntityCutout(this.getEntityTexture(entity)));
        MatrixStack.Entry matrixstack$entry = stack.peek();
        Matrix4f matrix4f = matrixstack$entry.getModel();
        Matrix3f matrix3f = matrixstack$entry.getNormal();
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLight);
        this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLight);

        for(int j = 0; j < 4; ++j) {
            stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90.0F));
            this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLight);
            this.buildVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLight);
            this.buildVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLight);
            this.buildVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLight);
        }

        stack.pop();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getEntityTexture(T p_110775_1_) {
        return ARROW;
    }

    public void buildVertex(Matrix4f matrix4f, Matrix3f matrix3f, IVertexBuilder builder, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int packedLight) {
        builder.vertex(matrix4f, x, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(packedLight).normal(matrix3f, normalX, normalY, normalZ).endVertex();
    }
}
