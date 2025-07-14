package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.BigRaccoonLeafEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.client.render.TextureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BigRaccoonLeafRender extends TextureRenderer<BigRaccoonLeafEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/leaf.png");

    public BigRaccoonLeafRender(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.8f, 1, 1);
    }

    @Override
    public void render(BigRaccoonLeafEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.27, 0.05);
        float spin = entity.initialYaw() + Mth.lerp(packedLight, 40 * entity.livingTicks(), 40 * entity.livingTicks() + 1);
        stack.mulPose(Axis.YP.rotationDegrees(entity.spinRight() ? spin : -spin));
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public void adjustYawPitch(PoseStack stack, BigRaccoonLeafEntity entity, float partialTicks, float yaw, float pitch) {
        super.adjustYawPitch(stack, entity, partialTicks, 0, 0);
    }

    @Override
    public void doRender(BigRaccoonLeafEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        stack.mulPose(Axis.XP.rotationDegrees(-20));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        stack.mulPose(Axis.XP.rotationDegrees(40));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
    }

    @Override
    public ResourceLocation getTextureLocation(BigRaccoonLeafEntity entity) {
        return TEX;
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