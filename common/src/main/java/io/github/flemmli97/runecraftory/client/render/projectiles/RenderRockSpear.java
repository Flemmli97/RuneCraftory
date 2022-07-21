package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityRockSpear;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.OwnableEntity;

public class RenderRockSpear extends EntityRenderer<EntityRockSpear> {

    private static final ResourceLocation big = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/avenger_rock_entity.png");
    private static final ResourceLocation small = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/screw_rock_entity.png");

    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderRockSpear(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityRockSpear entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float size = entity.getRange() + 0.5f;
        float yaw = -(entity.yRotO + (entity.getYRot() - entity.yRotO) * partialTicks) - 90.0F;
        float pitch = -(entity.xRotO + (entity.getXRot() - entity.xRotO) * partialTicks);
        RenderUtils.applyYawPitch(stack, yaw, pitch);
        this.textureBuilder.setLight(packedLight);
        stack.pushPose();
        boolean playerView = ((OwnableEntity) entity).getOwner() == Minecraft.getInstance().player
                && Minecraft.getInstance().options.getCameraType() != CameraType.THIRD_PERSON_BACK;
        stack.translate(size * 0.5, 0, 0);
        if (playerView) {
            stack.mulPose(Vector3f.XP.rotationDegrees(-90));
            float height = (float) (((entity.bigRock() ? 2.5f : 1.5f) * (Math.sin(Math.sqrt(entity.tickCount / (float) entity.livingTickMax()) * Math.PI))) + 0.2f);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), size, height, this.textureBuilder);
        } else {
            stack.mulPose(Vector3f.XP.rotationDegrees(45));
            float height = (float) (((entity.bigRock() ? 2.5f : 1.5f) * (Math.sin(Math.sqrt(entity.tickCount / (float) entity.livingTickMax()) * Math.PI))) + 0.2f);
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), size, height, this.textureBuilder);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            RenderUtils.renderTexture(stack, buffer.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), size, height, this.textureBuilder);
        }
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityRockSpear entity) {
        return entity.bigRock() ? big : small;
    }
}
