package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.ModelDuck;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityDuck;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderDuck<T extends EntityDuck> extends RenderMonster<T, ModelDuck<T>> {

    private final ResourceLocation sleepTexture;

    public RenderDuck(EntityRendererProvider.Context ctx, ResourceLocation texture, ResourceLocation sleepTexture) {
        super(ctx, new ModelDuck<>(ctx.bakeLayer(ModelDuck.LAYER_LOCATION)), texture, 0.5f);
        this.sleepTexture = sleepTexture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (EntityData.getSleepState(entity) != EntityData.SleepState.NONE)
            return this.sleepTexture;
        return super.getTextureLocation(entity);
    }
}
