package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelSpikes;
import io.github.flemmli97.runecraftory.common.entities.misc.EntitySpike;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderSpikes<T extends EntitySpike> extends RenderProjectileModel<T> {

    private static final ResourceLocation EARTH_SPIKE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/spikes_dirt_tiled.png");
    private static final ResourceLocation BRANCHES = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/spikes_wood_tiled.png");

    public RenderSpikes(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelSpikes<>(ctx.bakeLayer(ModelSpikes.LAYER_LOCATION)));
    }

    @Override
    public void render(T entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        if (entity.getAnimationProgress(partialTicks) > 0) {
            super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        }
    }

    @Override
    public void translate(T entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        float scale = entity.getAnimationProgress(partialTicks);
        stack.scale(1, scale, 1);
        super.translate(entity, stack, 0, 0, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.spikeType() == 1 ? BRANCHES : EARTH_SPIKE;
    }
}
