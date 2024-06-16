package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.ScaledRenderer;
import io.github.flemmli97.runecraftory.client.render.layer.RiderLayerRendererExt;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ScaledEntityRenderer<T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> extends RenderMonster<T, M> implements ScaledRenderer {

    public final float scale;

    public ScaledEntityRenderer(EntityRendererProvider.Context ctx, M model, ResourceLocation texture) {
        this(ctx, model, texture, 1);
    }

    public ScaledEntityRenderer(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float scale) {
        super(ctx, model, texture, 0.5f, false);
        this.scale = scale;
        this.layers.add(new RiderLayerRendererExt<>(this, (stack, entity) -> stack.scale(1 / this.scale, 1 / this.scale, 1 / this.scale)));
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(this.scale, this.scale, this.scale);
    }

    @Override
    public float getScale() {
        return this.scale;
    }
}
