package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelCluckadoodle;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityCluckadoodle;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderCluckadoodle<T extends EntityCluckadoodle> extends RenderMonster<T, ModelCluckadoodle<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/cluckadoodle.png");

    public RenderCluckadoodle(EntityRendererManager renderManager) {
        super(renderManager, new ModelCluckadoodle<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }


}
