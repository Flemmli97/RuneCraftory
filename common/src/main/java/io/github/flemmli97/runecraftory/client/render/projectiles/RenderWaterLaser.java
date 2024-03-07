package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWaterLaser extends RenderBeam<EntityWaterLaser> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/water_laser.png");

    public RenderWaterLaser(EntityRendererProvider.Context ctx) {
        super(ctx, 0.6f, 0.4f, 4);
    }

    @Override
    public ResourcePair startTexture(EntityWaterLaser entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(EntityWaterLaser entity) {
        return null;
    }

    @Override
    public float widthFunc(EntityWaterLaser entity) {
        return (float) (this.radius * (Math.sin(Math.sqrt(entity.tickCount / (float) entity.livingTickMax()) * Math.PI))) + 0.2f;
    }

    @Override
    public int animationFrames(BeamPart part) {
        return 8;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWaterLaser entity) {
        return TEX;
    }
}
