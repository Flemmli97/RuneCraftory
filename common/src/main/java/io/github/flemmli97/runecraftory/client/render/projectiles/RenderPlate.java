package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelPlate;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPlate extends RenderProjectileModel<EntityBigPlate> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/big_plate.png");

    public RenderPlate(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelPlate<>(ctx.bakeLayer(ModelPlate.LAYER_LOCATION)));
    }

    @Override
    public void translate(EntityBigPlate entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.scale(1.5f, 1.5f, 1.5f);
        stack.translate(0.0, -1.501f, 0.0);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBigPlate entity) {
        return TEX;
    }
}
