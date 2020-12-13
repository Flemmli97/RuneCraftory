package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelAmbrosia;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.boss.EntityAmbrosia;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderAmbrosia<T extends EntityAmbrosia> extends RenderMonster<T, ModelAmbrosia<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/ambrosia.png");

    public RenderAmbrosia(EntityRendererManager renderManager) {
        super(renderManager, new ModelAmbrosia<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

    @Override
    protected float getDeathMaxRotation(T entityLivingBaseIn) {
        return 0;
    }
}
