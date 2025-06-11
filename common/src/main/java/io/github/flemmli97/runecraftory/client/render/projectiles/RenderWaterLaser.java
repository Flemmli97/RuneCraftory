package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWaterLaser;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderWaterLaser extends RenderBeam<EntityWaterLaser> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/water_laser.png");

    private final float widthMod;

    public RenderWaterLaser(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 0.8f, 4);
        this.widthMod = Mth.sqrt(0.8f * 0.8f / 2) * 2;
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
        return super.widthFunc(entity) / this.widthMod;
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
