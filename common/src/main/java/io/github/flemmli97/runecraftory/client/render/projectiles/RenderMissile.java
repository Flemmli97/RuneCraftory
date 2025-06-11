package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelMissile;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMissile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderMissile<T extends EntityMissile> extends RenderProjectileModel<T> {

    private final ResourceLocation tex = RuneCraftory.modRes("textures/entity/projectile/missile.png");

    public RenderMissile(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelMissile<>(ctx.bakeLayer(ModelMissile.LAYER_LOCATION)));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
