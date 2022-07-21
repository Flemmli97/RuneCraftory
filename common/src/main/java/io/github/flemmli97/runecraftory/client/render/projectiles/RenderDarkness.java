package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityDarkness;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderDarkness extends EntityRenderer<EntityDarkness> {

    private static final ResourceLocation tex = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/darkness.png");

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderDarkness(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityDarkness entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float size = 1.2f + entity.getRadius() + Mth.sin((entity.tickCount + partialTicks) * 0.3f) * 0.06f;
        this.textureBuilder.setLight(packedLight);
        stack.pushPose();
        boolean playerView = entity.getOwner() == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON;
        if (playerView) {
            stack.translate(0, 0.01, 0);
            stack.mulPose(Vector3f.YP.rotationDegrees(-(entity.getOwner().yRotO + (entity.getOwner().getYRot() - entity.getOwner().yRotO) * partialTicks) - 180.0F));
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), size, size, this.textureBuilder);
        } else {
            stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), size, size, this.textureBuilder);
        }
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityDarkness entity) {
        return tex;
    }
}
