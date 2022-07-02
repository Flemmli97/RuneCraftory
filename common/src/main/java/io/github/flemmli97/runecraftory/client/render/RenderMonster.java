package io.github.flemmli97.runecraftory.client.render;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class RenderMonster<T extends BaseMonster, M extends EntityModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation tex;

    public RenderMonster(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow) {
        super(ctx, model, shadow);
        this.tex = texture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }

}
