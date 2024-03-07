package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBullet;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderBullet extends RenderTexture<EntityBullet> {

    private final ResourceLocation tex;

    public RenderBullet(EntityRendererProvider.Context ctx, ResourceLocation tex) {
        super(ctx, 0.5f, 0.5f, 1, 1);
        this.tex = tex;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBullet entity) {
        return this.tex;
    }

    @Override
    public void render(EntityBullet entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        if (entity.element() == EnumElement.FIRE)
            this.textureBuilder.setColor(255, 150, 150, 255);
        else
            this.textureBuilder.setColor(0xFFFFFFFF);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }
}
