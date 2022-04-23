package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class EmptyRender<T extends Entity> extends EntityRenderer<T> {

    public EmptyRender(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packetLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return null;
    }
}