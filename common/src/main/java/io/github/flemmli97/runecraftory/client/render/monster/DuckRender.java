package io.github.flemmli97.runecraftory.client.render.monster;

import io.github.flemmli97.runecraftory.client.model.monster.DuckModel;
import io.github.flemmli97.runecraftory.client.render.RenderMonster;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.common.entities.monster.Duck;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DuckRender<T extends Duck> extends RenderMonster<T, DuckModel<T>> {

    private final ResourceLocation sleepTexture;

    public DuckRender(EntityRendererProvider.Context ctx, ResourceLocation texture, ResourceLocation sleepTexture) {
        super(ctx, new DuckModel<>(), texture, 0.7f);
        this.sleepTexture = sleepTexture;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        if (EntityData.getSleepStateFrom(entity) != EntityData.SleepState.NONE)
            return this.sleepTexture;
        return super.getTextureLocation(entity);
    }
}
