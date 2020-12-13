package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelWooly;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderWooly<T extends EntityWooly> extends RenderMonster<T, ModelWooly<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/wooly.png");

    public RenderWooly(EntityRendererManager renderManager) {
        super(renderManager, new ModelWooly<>());
        this.layerRenderers.add(new LayerWooly<>(this));
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
