package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSingleFrameBeam<T extends EntityBeam> extends RenderBeam<T> {

    public static final ResourceLocation DARK_BEAM = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/dark_beam.png");
    public static final ResourceLocation LIGHT_BEAM = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/light_beam.png");

    private final ResourceLocation texture;

    public RenderSingleFrameBeam(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 0.8f, 0.65f);
    }

    public RenderSingleFrameBeam(EntityRendererProvider.Context ctx, ResourceLocation texture, float glowWidth, float innerWidth) {
        super(ctx, glowWidth, innerWidth, 4);
        this.texture = texture;
    }

    @Override
    public RenderBeam.ResourcePair startTexture(T entity) {
        return null;
    }

    @Override
    public RenderBeam.ResourcePair endTexture(T entity) {
        return null;
    }

    @Override
    public float widthFunc(T entity) {
        return (float) (this.radius * (Math.sin(Math.sqrt(entity.tickCount / (float) entity.livingTickMax()) * Math.PI))) + 0.2f;
    }

    @Override
    public int animationFrames(RenderBeam.BeamPart part) {
        return 1;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }
}
