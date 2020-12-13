package com.flemmli97.runecraftory.client.render.monster;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.monster.ModelBigMuck;
import com.flemmli97.runecraftory.client.render.RenderMonster;
import com.flemmli97.runecraftory.common.entities.monster.EntityBigMuck;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderBigMuck<T extends EntityBigMuck> extends RenderMonster<T, ModelBigMuck<T>> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/monsters/big_muck.png");

    public RenderBigMuck(EntityRendererManager renderManager) {
        super(renderManager, new ModelBigMuck<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
