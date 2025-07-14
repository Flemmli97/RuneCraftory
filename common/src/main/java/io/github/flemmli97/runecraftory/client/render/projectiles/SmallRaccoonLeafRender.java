package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.SmallRaccoonLeafEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.client.render.TextureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SmallRaccoonLeafRender extends TextureRenderer<SmallRaccoonLeafEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/leaf.png");

    public SmallRaccoonLeafRender(EntityRendererProvider.Context ctx) {
        super(ctx, 0.4f, 0.4f, 1, 1);
    }

    @Override
    public void render(SmallRaccoonLeafEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public void doRender(SmallRaccoonLeafEntity entity, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(-20));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        stack.mulPose(Axis.XP.rotationDegrees(40));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SmallRaccoonLeafEntity entity) {
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