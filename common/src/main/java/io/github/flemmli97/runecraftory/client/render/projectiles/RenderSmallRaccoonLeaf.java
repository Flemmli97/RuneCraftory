package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySmallRaccoonLeaf;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSmallRaccoonLeaf extends RenderTexture<EntitySmallRaccoonLeaf> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/leaf.png");
    private boolean rotFlag;

    public RenderSmallRaccoonLeaf(EntityRendererProvider.Context ctx) {
        super(ctx, 0.4f, 0.4f, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySmallRaccoonLeaf entity) {
        return TEX;
    }

    @Override
    public void render(EntitySmallRaccoonLeaf entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        stack.pushPose();
        this.rotFlag = true;
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
        stack.pushPose();
        this.rotFlag = false;
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
        stack.popPose();
    }

    @Override
    public void adjustYawPitch(PoseStack stack, EntitySmallRaccoonLeaf entity, float partialTicks, float yaw, float pitch) {
        super.adjustYawPitch(stack, entity, partialTicks, yaw, 0);
        stack.mulPose(Vector3f.XP.rotationDegrees(this.rotFlag ? 20 : -20));
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