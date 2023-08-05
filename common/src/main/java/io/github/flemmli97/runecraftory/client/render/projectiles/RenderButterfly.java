package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelButterfly;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityButterfly;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderButterfly<T extends EntityButterfly> extends RenderProjectileModel<T> {

    private final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/butterfly.png");

    public RenderButterfly(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelButterfly<>(ctx.bakeLayer(ModelButterfly.LAYER_LOCATION)));
    }

    @Override
    public void translate(T entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        super.translate(entity, stack, pitch, yaw, partialTicks);
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.translate(0.0, -1.501f, 0.0);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
