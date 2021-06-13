package io.github.flemmli97.runecraftory.client.render;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
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
    protected boolean canRenderName(GateEntity entity) {
        return false;
    }

    @Override
    protected float getDeathMaxRotation(GateEntity entityLivingBaseIn) {
        return 0;
    }
}
