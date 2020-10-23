package com.flemmli97.runecraftory.mobs.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.mobs.client.model.monster.ModelAnt;
import com.flemmli97.runecraftory.mobs.client.render.RenderMonster;
import com.flemmli97.runecraftory.mobs.entity.monster.EntityAnt;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderAnt<T extends EntityAnt> extends RenderMonster<T, ModelAnt<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ant.png");

    public RenderAnt(EntityRendererManager renderManager) {
        super(renderManager, new ModelAnt<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

    @Override
    protected void scale(T entity, MatrixStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(0.7f, 0.7f, 0.7f);
    }
}
