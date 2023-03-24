package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityPoisonNeedle;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPoisonNeedle extends RenderTexture<EntityPoisonNeedle> {

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
        stack.translate(0, this.ySize * 0.25, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
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