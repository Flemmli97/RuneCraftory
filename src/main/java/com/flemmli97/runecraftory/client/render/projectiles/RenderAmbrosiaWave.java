package com.flemmli97.runecraftory.client.render.projectiles;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityAmbrosiaWave;
import com.flemmli97.tenshilib.client.render.RenderUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmbrosiaWave<T extends EntityAmbrosiaWave> extends EntityRenderer<T> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/ambrosia_wave.png");

    public RenderAmbrosiaWave(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLight) {
        for (float f : entity.clientWaveSizes) {
            f = f - EntityAmbrosiaWave.circleInc + EntityAmbrosiaWave.circleInc * partialTicks;
            if (f > 0) {
                float alpha = (1 - f * 0.16f) * entity.clientAlphaMult;
                RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.getEntityCutoutNoCull(this.tex)), f * 2.36f + partialTicks, f * 2.36f + partialTicks, 1, 1, 1, alpha, packedLight);
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return null;
    }
}
