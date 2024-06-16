package io.github.flemmli97.runecraftory.client.render.monster;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.client.model.monster.ModelDuck;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.client.render.layer.RiderLayerRendererExt;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDuck;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderDuck<T extends EntityDuck> extends RenderMonster<T, ModelDuck<T>> {

    private static final float SCALE = 0.85f;

    private final ResourceLocation sleepTexture;

    public RenderDuck(EntityRendererProvider.Context ctx, ResourceLocation texture, ResourceLocation sleepTexture) {
        super(ctx, new ModelDuck<>(ctx.bakeLayer(ModelDuck.LAYER_LOCATION)), texture, 0.5f, false);
        this.sleepTexture = sleepTexture;
        this.layers.add(new RiderLayerRendererExt<>(this, (stack, entity) -> stack.scale(1 / SCALE, 1 / SCALE, 1 / SCALE)));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (EntityData.getSleepState(entity) != EntityData.SleepState.NONE)
            return this.sleepTexture;
        return super.getTextureLocation(entity);
    }

    @Override
    protected void scale(T entity, PoseStack stack, float partialTick) {
        super.scale(entity, stack, partialTick);
        stack.scale(SCALE, SCALE, SCALE);
    }
}
