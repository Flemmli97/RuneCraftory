package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.render.RenderBeam;
import io.github.flemmli97.tenshilib.common.entity.EntityBeam;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderSingleFrameBeam<T extends EntityBeam> extends RenderBeam<T> {

    public static final ResourceLocation DARK_BEAM = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/dark_beam.png");
    public static final ResourceLocation LIGHT_BEAM = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/light_beam.png");

    private final ResourceLocation texture;

    private final float widthMod;

    public RenderSingleFrameBeam(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 1, 0.85f);
    }

    public RenderSingleFrameBeam(EntityRendererProvider.Context ctx, ResourceLocation texture, float glowWidth, float innerWidth) {
        super(ctx, glowWidth, innerWidth, 4);
        this.widthMod = Mth.sqrt(innerWidth * innerWidth / 2) * 2;
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
        return super.widthFunc(entity) / this.widthMod;
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
