package com.flemmli97.runecraftory.mobs.client.render.projectiles;

import com.flemmli97.runecraftory.mobs.entity.projectiles.EntityAmbrosiaSleep;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderSleepBall<T extends EntityAmbrosiaSleep> extends EntityRenderer<T> {

    public RenderSleepBall(EntityRendererManager manager) {
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
