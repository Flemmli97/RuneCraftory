package com.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class EmptyRender<T extends Entity> extends EntityRenderer<T> {

    public EmptyRender(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packetLight) {

    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}