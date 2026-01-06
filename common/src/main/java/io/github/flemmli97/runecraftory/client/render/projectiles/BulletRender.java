package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.misc.BulletEntity;
import io.github.flemmli97.runecraftory.common.items.ItemElement;
import io.github.flemmli97.tenshilib.client.render.TextureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BulletRender extends TextureRenderer<BulletEntity> {

    private final ResourceLocation tex;

    public BulletRender(EntityRendererProvider.Context ctx, ResourceLocation tex) {
        super(ctx, 0.5f, 0.5f, 1, 1);
        this.tex = tex;
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return this.tex;
    }

    @Override
    public void render(BulletEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        if (entity.element() == ItemElement.FIRE)
            this.textureBuilder.setColor(255, 150, 150, 255);
        else
            this.textureBuilder.setColor(0xFFFFFFFF);
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
        stack.popPose();
    }
}
