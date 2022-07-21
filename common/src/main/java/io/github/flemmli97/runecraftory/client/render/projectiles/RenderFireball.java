package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityFireball;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class RenderFireball extends RenderTexture<EntityFireball> {

    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/fireball_n.png");

    public RenderFireball(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 6, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityFireball entity) {
        return tex;
    }

    @Override
    public float[] uvOffset(int timer) {
        return super.uvOffset((int) (timer * 0.5));
    }


    @Override
    public void render(EntityFireball entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        if (entity.big())
            stack.scale(1.3f, 1.3f, 1.3f);
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
    }
}
