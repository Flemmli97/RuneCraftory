package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkBeam;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderDarkBeam extends RenderBeam<EntityDarkBeam> {
    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/dark_beam.png");

    public RenderDarkBeam(EntityRendererProvider.Context ctx) {
        super(ctx, 0.8f, 0.65f, 4);
    }

    @Override
    public RenderBeam.ResourcePair startTexture(EntityDarkBeam entity) {
        return null;
    }

    @Override
    public RenderBeam.ResourcePair endTexture(EntityDarkBeam entity) {
        return null;
    }

    @Override
    public float widthFunc(EntityDarkBeam entity) {
        return (float) (this.radius * (Math.sin(Math.sqrt(entity.tickCount / (float) entity.livingTickMax()) * Math.PI))) + 0.2f;
    }

    @Override
    public int animationFrames(RenderBeam.BeamPart part) {
        return 1;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDarkBeam entity) {
        return tex;
    }
}
