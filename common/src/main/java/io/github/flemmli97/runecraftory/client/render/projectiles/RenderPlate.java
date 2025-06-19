package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.SimpleGeoModel;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityBigPlate;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderPlate extends SimpleModelRenderer<EntityBigPlate> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/big_plate.png");
    private static final ResourceLocation MODEL_LOCATION = RuneCraftory.modRes("plate_model");

    public RenderPlate(EntityRendererProvider.Context ctx) {
        super(ctx, new SimpleGeoModel<>(MODEL_LOCATION));
    }

    @Override
    public void translate(EntityBigPlate entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        stack.scale(1.5f, 1.5f, 1.5f);
        super.translate(entity, stack, 0, 0, partialTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityBigPlate entity) {
        return TEX;
    }
}
