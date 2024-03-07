package io.github.flemmli97.runecraftory.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.client.render.RiderLayerRenderer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.BiConsumer;

public class RiderLayerRendererExt<T extends LivingEntity, M extends EntityModel<T> & RideableModel<T>> extends RiderLayerRenderer<T, M> {

    private final BiConsumer<PoseStack, T> preRender;

    public RiderLayerRendererExt(LivingEntityRenderer<T, M> renderer, BiConsumer<PoseStack, T> preRender) {
        super(renderer);
        this.preRender = preRender;
    }

    @Override
    protected void undoLivingRendererTransform(EntityRenderer<?> entityRenderer, PoseStack stack, T entity, Entity rider, float partialTicks, boolean transformed) {
        super.undoLivingRendererTransform(entityRenderer, stack, entity, rider, partialTicks, transformed);
        if (this.preRender != null)
            this.preRender.accept(stack, entity);
    }
}
