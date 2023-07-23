package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPoisonNeedle;
import io.github.flemmli97.tenshilib.client.render.RenderCrossedTextureEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPoisonNeedle extends RenderCrossedTextureEntity<EntityPoisonNeedle> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/poison_needle.png");

    public RenderPoisonNeedle(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.8f, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityPoisonNeedle entity) {
        return TEX;
    }

    @Override
    public void render(EntityPoisonNeedle entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.2, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public void doRender(EntityPoisonNeedle entity, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        stack.pushPose();
        stack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
        for (int j = 0; j < 2; ++j) {
            stack.mulPose(Vector3f.XP.rotationDegrees(90.0F));
            stack.pushPose();
            stack.translate(0, this.xSize * 1 / 16f * 0.5, 0);
            RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
            stack.popPose();
        }
        stack.popPose();
    }

    @Override
    public boolean facePlayer() {
        return false;
    }

    @Override
    public float yawOffset() {
        return -90.0F;
    }
}