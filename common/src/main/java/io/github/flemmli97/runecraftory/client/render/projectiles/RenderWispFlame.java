package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.api.enums.EnumElement;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityWispFlame;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

public class RenderWispFlame extends RenderTexture<EntityWispFlame> {

    private static final ResourceLocation DARK = new ResourceLocation("minecraft", "block/soul_fire_0");
    private static final ResourceLocation FIRE = new ResourceLocation("minecraft", "block/fire_0");

    public RenderWispFlame(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityWispFlame entity) {
        return switch (entity.element()) {
            case FIRE -> FIRE;
            default -> DARK;
        };
    }

    @Override
    public void render(EntityWispFlame entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.translate(0, this.ySize * 0.45, 0);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180));
        if (entity.element() == EnumElement.DARK || entity.element() == EnumElement.FIRE) {
            var t = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                    .apply(this.getTextureLocation(entity));
            this.textureBuilder.setUV(t.getU0(), t.getV0());
            this.textureBuilder.setUVLength(t.getU1() - t.getU0(), t.getV1() - t.getV0());
            RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, t.atlas().location())), 1, 2, this.textureBuilder);
        } else {

        }
        stack.popPose();
    }
}
