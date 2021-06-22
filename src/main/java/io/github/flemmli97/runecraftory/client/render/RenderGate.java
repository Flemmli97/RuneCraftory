package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.runecraftory.common.particles.ColoredParticleData4f;
import io.github.flemmli97.runecraftory.common.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.util.ResourceLocation;

public class RenderGate extends LivingRenderer<GateEntity, ModelGate> {

    public RenderGate(EntityRendererManager renderManager) {
        super(renderManager, new ModelGate(), 0);
    }

    @Override
    public ResourceLocation getEntityTexture(GateEntity entity) {
        return new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_" + entity.elementName() + ".png");
    }

    @Override
    public void render(GateEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        ParticleStatus status = Minecraft.getInstance().gameSettings.particles;
        if (status == ParticleStatus.ALL) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 2,
                        0, 0.01f, entity.clientParticles, 5), true, entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else if (status == ParticleStatus.DECREASED) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 2,
                        0, 0.01f, entity.clientParticles, 5), entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    @Override
    protected void preRenderCallback(GateEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.DECREASED) {
            matrixStackIn.scale(0.3f, 0.3f, 0.3f);
            matrixStackIn.translate(0, -1, 0);
        }
    }

    @Override
    protected boolean canRenderName(GateEntity entity) {
        return false;
    }

    @Override
    protected float getDeathMaxRotation(GateEntity entityLivingBaseIn) {
        return 0;
    }
}
