package com.flemmli97.runecraftory.mobs.client.render;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.client.model.ModelGate;
import com.flemmli97.runecraftory.mobs.entity.GateEntity;
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
