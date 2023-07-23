package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigRaccoonLeaf;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderBigRaccoonLeaf extends RenderTexture<EntityBigRaccoonLeaf> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/leaf.png");

    public RenderBigRaccoonLeaf(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.8f, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBigRaccoonLeaf entity) {
        return TEX;
    }

    @Override
    public void render(EntityBigRaccoonLeaf entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.27, 0.05);
        float spin = entity.initialYaw() + Mth.lerp(packedLight, 40 * entity.livingTicks(), 40 * entity.livingTicks() + 1);
        stack.mulPose(Vector3f.YP.rotationDegrees(entity.spinRight() ? spin : -spin));
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public void doRender(EntityBigRaccoonLeaf entity, float partialTicks, PoseStack stack, MultiBufferSource buffer) {
        stack.mulPose(Vector3f.XP.rotationDegrees(-20));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
        stack.mulPose(Vector3f.XP.rotationDegrees(40));
        RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, this.getTextureLocation(entity))), this.xSize, this.ySize, this.textureBuilder);
    }

    @Override
    public void adjustYawPitch(PoseStack stack, EntityBigRaccoonLeaf entity, float partialTicks, float yaw, float pitch) {
        super.adjustYawPitch(stack, entity, partialTicks, 0, 0);
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