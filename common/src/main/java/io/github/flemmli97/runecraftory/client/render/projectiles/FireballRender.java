package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.FireballEntity;
import io.github.flemmli97.tenshilib.client.render.TextureRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FireballRender extends TextureRenderer<FireballEntity> {

    private static final ResourceLocation TEX = RuneCraftory.modRes("textures/entity/projectile/fireball_n.png");

    public FireballRender(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 6, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(FireballEntity entity) {
        return TEX;
    }

    @Override
    public void render(FireballEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.25, 0);
        if (entity.big())
            stack.scale(1.3f, 1.3f, 1.3f);
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
        stack.popPose();
    }

    @Override
    public float[] uvOffset(int timer) {
        return super.uvOffset((int) (timer * 0.5));
    }
}
