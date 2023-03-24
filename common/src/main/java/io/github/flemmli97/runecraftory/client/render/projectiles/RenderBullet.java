package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCirclingBullet;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderBullet extends RenderTexture<EntityCirclingBullet> {

    private final ResourceLocation tex;

    public RenderBullet(EntityRendererProvider.Context ctx, ResourceLocation tex) {
        super(ctx, 1, 1, 1, 1);
        this.tex = tex;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCirclingBullet entity) {
        return this.tex;
    }

    @Override
    public void render(EntityCirclingBullet entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }
}
