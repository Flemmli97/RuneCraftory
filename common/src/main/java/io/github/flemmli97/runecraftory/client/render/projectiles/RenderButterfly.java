package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelButterfly;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderButterfly<T extends EntityButterfly> extends RenderProjectileModel<T> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/butterfly.png");

    public RenderButterfly(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelButterfly<>(ctx.bakeLayer(ModelButterfly.LAYER_LOCATION)));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
