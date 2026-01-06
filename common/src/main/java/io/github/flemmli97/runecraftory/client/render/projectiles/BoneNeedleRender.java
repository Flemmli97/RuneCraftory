package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.BoneNeedleEntity;
import io.github.flemmli97.tenshilib.client.render.CrossedTextureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BoneNeedleRender extends CrossedTextureRenderer<BoneNeedleEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/bone_needle.png");

    public BoneNeedleRender(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.8f, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(BoneNeedleEntity entity) {
        return TEX;
    }

    @Override
    public void render(BoneNeedleEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.2, 0);
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
        stack.popPose();
    }
}
