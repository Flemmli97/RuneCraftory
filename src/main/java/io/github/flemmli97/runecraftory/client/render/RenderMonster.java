package io.github.flemmli97.runecraftory.client.render;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation tex;

    public RenderMonster(EntityRendererManager manager, M model, ResourceLocation texture) {
        super(manager, model, 0.5f);
        this.tex = texture;
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }

}
