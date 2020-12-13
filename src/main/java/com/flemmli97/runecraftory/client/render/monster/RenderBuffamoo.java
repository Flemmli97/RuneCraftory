package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelBuffamoo;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityBuffamoo;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBuffamoo<T extends EntityBuffamoo> extends RenderMonster<T, ModelBuffamoo<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/buffamoo.png");

    public RenderBuffamoo(EntityRendererManager renderManager) {
        super(renderManager, new ModelBuffamoo<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }


}
