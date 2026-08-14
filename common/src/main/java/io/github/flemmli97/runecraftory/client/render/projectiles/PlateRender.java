package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.BigPlateEntity;
import io.github.flemmli97.tenshilib.client.model.ExtendedEntityModel;
import io.github.flemmli97.tenshilib.client.render.SimpleModelRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PlateRender extends SimpleModelRenderer<BigPlateEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/plate.png");
    private static final ResourceLocation MODEL_LOCATION = RuneCraftory.modRes("entity/plate");

    public PlateRender(EntityRendererProvider.Context ctx) {
        super(ctx, new ExtendedEntityModel<>(MODEL_LOCATION));
    }

    @Override
    public void translate(BigPlateEntity entity, PoseStack stack, float pitch, float yaw, float partialTick) {
        stack.scale(1.5f, 1.5f, 1.5f);
        super.translate(entity, stack, 0, 0, partialTick);
    }

    @Override
    public ResourceLocation getTextureLocation(BigPlateEntity entity) {
        return TEX;
    }
}
