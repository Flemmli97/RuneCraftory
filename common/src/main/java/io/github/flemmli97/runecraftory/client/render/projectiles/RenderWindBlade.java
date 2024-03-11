package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWindBlade;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderWindBlade extends RenderTexture<EntityWindBlade> {

    private static final ResourceLocation TEX = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/wind_blade.png");

    public RenderWindBlade(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 8, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWindBlade entity) {
        return TEX;
    }

    @Override
    public void render(EntityWindBlade entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(0.75f, 0.75f, 0.75f);
        stack.translate(0, this.ySize * 0.15, 0);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public float[] uvOffset(int timer) {
        return super.uvOffset((int) (timer * 0.7));
    }
}
