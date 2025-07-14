package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ButterflyModel;
import io.github.flemmli97.runecraftory.common.entities.misc.ButterflyEntity;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ButterflyRender<T extends ButterflyEntity> extends SimpleModelRenderer<T> {

    private final ResourceLocation tex = RuneCraftory.modRes("textures/entity/projectile/butterfly.png");

    public ButterflyRender(EntityRendererProvider.Context ctx) {
        super(ctx, new ButterflyModel<>());
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
