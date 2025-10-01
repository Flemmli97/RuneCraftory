package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.render.BeamRenderer;
import io.github.flemmli97.tenshilib.common.entity.BeamEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class WaterLaserRender extends BeamRenderer<BeamEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/water_laser.png");

    private final float widthMod;

    public WaterLaserRender(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 0.8f, 4);
        this.widthMod = Mth.sqrt(0.8f * 0.8f / 2) * 2;
    }

    @Override
    public ResourcePair startTexture(BeamEntity entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(BeamEntity entity) {
        return null;
    }

    @Override
    public float widthFunc(BeamEntity entity, float partialTicks) {
        return super.widthFunc(entity, partialTicks) / this.widthMod;
    }

    @Override
    public int animationFrames(BeamPart part) {
        return 8;
    }

    @Override
    public ResourceLocation getTextureLocation(BeamEntity entity) {
        return TEX;
    }
}
