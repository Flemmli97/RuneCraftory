package com.flemmli97.runecraftory.client.render;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;

public abstract class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    public RenderMonster(EntityRendererManager manager, M model) {
        super(manager, model, 0.5f);
    }

}
