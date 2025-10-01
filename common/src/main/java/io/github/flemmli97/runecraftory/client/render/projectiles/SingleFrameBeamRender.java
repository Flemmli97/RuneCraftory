package io.github.flemmli97.runecraftory.client.render.projectiles;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.tenshilib.client.render.BeamRenderer;
import io.github.flemmli97.tenshilib.common.entity.BeamEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SingleFrameBeamRender<T extends BeamEntity> extends BeamRenderer<T> {

    public static final ResourceLocation DARK_BEAM = RuneCraftory.modRes("textures/entity/projectile/dark_beam.png");
    public static final ResourceLocation LIGHT_BEAM = RuneCraftory.modRes("textures/entity/projectile/light_beam.png");

    private final ResourceLocation texture;

    private final float widthMod;

    public SingleFrameBeamRender(EntityRendererProvider.Context ctx, ResourceLocation texture) {
        this(ctx, texture, 1, 0.85f);
    }

    public SingleFrameBeamRender(EntityRendererProvider.Context ctx, ResourceLocation texture, float glowWidth, float innerWidth) {
        super(ctx, glowWidth, innerWidth, 4);
        this.widthMod = Mth.sqrt(innerWidth * innerWidth / 2) * 2;
        this.texture = texture;
    }

    @Override
    public ResourcePair startTexture(T entity) {
        return null;
    }

    @Override
    public ResourcePair endTexture(T entity) {
        return null;
    }

    @Override
    public float widthFunc(T entity, float partialTicks) {
        return super.widthFunc(entity, partialTicks) / this.widthMod;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.texture;
    }
}
