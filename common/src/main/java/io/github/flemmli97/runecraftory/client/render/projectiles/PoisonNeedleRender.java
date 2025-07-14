package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.PoisonNeedleEntity;
import io.github.flemmli97.tenshilib.client.render.CrossedTextureRenderer;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PoisonNeedleRender extends CrossedTextureRenderer<PoisonNeedleEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/poison_needle.png");

    public PoisonNeedleRender(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.8f, 1, 1);
    }

    @Override
    public void render(PoisonNeedleEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.2, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public void doRender(PoisonNeedleEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(45.0F));
        for (int j = 0; j < 2; ++j) {
            stack.mulPose(Axis.XP.rotationDegrees(90.0F));
            stack.pushPose();
            stack.translate(0, this.xSize * 1 / 16f * 0.5, 0);
            RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
            stack.popPose();
        }
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(PoisonNeedleEntity entity) {
        return TEX;
    }
}