package com.flemmli97.runecraftory.client.render.projectiles;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.client.model.ModelButterfly;
import com.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import com.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderButterfly<T extends EntityButterfly> extends RenderProjectileModel<T> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/butterfly.png");

    public RenderButterfly(EntityRendererManager manager) {
        super(manager, new ModelButterfly<>());
    }

    @Override
    public ResourceLocation getEntityTexture(T entity) {
        return this.tex;
    }
}
