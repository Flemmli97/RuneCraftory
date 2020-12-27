package com.flemmli97.runecraftory.client.render.projectiles;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.entities.projectiles.EntityFireball;
import com.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RenderFireball extends RenderTexture<EntityFireball> {

    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/fireball.png");

    public RenderFireball(EntityRendererManager renderManager) {
        super(renderManager, 1, 1, 6, 1);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityFireball p_110775_1_) {
        return tex;
    }

    @Override
    public float[] uvOffset(int timer) {
        return super.uvOffset((int) (timer*0.5));
    }
}
