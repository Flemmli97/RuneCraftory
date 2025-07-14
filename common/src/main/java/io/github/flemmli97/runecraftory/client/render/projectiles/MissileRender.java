package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.MissileModel;
import io.github.flemmli97.runecraftory.common.entities.misc.MissileEntity;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class MissileRender<T extends MissileEntity> extends SimpleModelRenderer<T> {

    private final ResourceLocation tex = RuneCraftory.modRes("textures/entity/projectile/missile.png");

    public MissileRender(EntityRendererProvider.Context ctx) {
        super(ctx, new MissileModel<>());
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
