package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityCustomFishingHook;
import io.github.flemmli97.runecraftory.common.items.tools.ItemToolFishingRod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

/**
 * From {@link FishingHookRenderer}
 */
public class CustomFishingHookRenderer extends EntityRenderer<EntityCustomFishingHook> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/fishing_hook.png");
    private static final RenderType RENDER_TYPE = RenderType.entityCutout(TEXTURE_LOCATION);

    public CustomFishingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(EntityCustomFishingHook entity, float entityYaw, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
        Entity eOwner = entity.getOwner();
        if (!(eOwner instanceof LivingEntity owner)) {
            return;
        }
        matrixStack.pushPose();
        matrixStack.pushPose();
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0f));
        PoseStack.Pose pose = matrixStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        VertexConsumer vertexConsumer = buffer.getBuffer(RENDER_TYPE);
        vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 0.0f, 0, 0, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 1.0f, 0, 1, 1);
        vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 1.0f, 1, 1, 0);
        vertex(vertexConsumer, matrix4f, matrix3f, packedLight, 0.0f, 1, 0, 0);
        matrixStack.popPose();
        int i = owner.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        ItemStack itemStack = owner.getMainHandItem();
        if (!(itemStack.getItem() instanceof ItemToolFishingRod)) {
            i = -i;
        }
        float f = owner.getAttackAnim(partialTicks);
        float g = Mth.sin(Mth.sqrt(f) * (float) Math.PI);
        float yRot = Mth.lerp(partialTicks, owner.yBodyRotO, owner.yBodyRot) * ((float) Math.PI / 180);
        double d = Mth.sin(yRot);
        double e = Mth.cos(yRot);
        double j = i * 0.35;
        double p;
        double xPos;
        double yPos;
        double zPos;
        float o;
        if (this.entityRenderDispatcher.options != null && !this.entityRenderDispatcher.options.getCameraType().isFirstPerson() || owner != Minecraft.getInstance().player) {
            xPos = Mth.lerp(partialTicks, owner.xo, owner.getX()) - e * j - d * 0.8;
            yPos = owner.yo + owner.getEyeHeight() + (owner.getY() - owner.yo) * partialTicks - 0.45;
            zPos = Mth.lerp(partialTicks, owner.zo, owner.getZ()) - d * j + e * 0.8;
            o = owner.isCrouching() ? -0.1875f : 0.0f;
        } else {
            p = 960.0 / this.entityRenderDispatcher.options.fov;
            Vec3 vec3 = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float) i * 0.525f, -0.1f);
            vec3 = vec3.scale(p);
            vec3 = vec3.yRot(g * 0.5f);
            vec3 = vec3.xRot(-g * 0.7f);
            xPos = Mth.lerp(partialTicks, owner.xo, owner.getX()) + vec3.x;
            yPos = Mth.lerp(partialTicks, owner.yo, owner.getY()) + vec3.y;
            zPos = Mth.lerp(partialTicks, owner.zo, owner.getZ()) + vec3.z;
            o = owner.getEyeHeight();
        }
        p = Mth.lerp(partialTicks, entity.xo, entity.getX());
        double q = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.25;
        double r = Mth.lerp(partialTicks, entity.zo, entity.getZ());
        float s = (float) (xPos - p);
        float t = (float) (yPos - q) + o;
        float u = (float) (zPos - r);
        VertexConsumer vertexConsumer2 = buffer.getBuffer(RenderType.lineStrip());
        PoseStack.Pose pose2 = matrixStack.last();
        for (int w = 0; w <= 16; ++w) {
            stringVertex(s, t, u, vertexConsumer2, pose2, w / 16f, (w + 1) / 16f);
        }
        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, int k, int l) {
        vertexConsumer.vertex(matrix4f, f - 0.5f, (float) j - 0.5f, 0.0f).color(255, 255, 255, 255).uv(k, l).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(i).normal(matrix3f, 0.0f, 1.0f, 0.0f).endVertex();
    }

    private static void stringVertex(float f, float g, float h, VertexConsumer vertexConsumer, PoseStack.Pose pose, float i, float j) {
        float k = f * i;
        float l = g * (i * i + i) * 0.5f + 0.25f;
        float m = h * i;
        float n = f * j - k;
        float o = g * (j * j + j) * 0.5f + 0.25f - l;
        float p = h * j - m;
        float q = Mth.sqrt(n * n + o * o + p * p);
        vertexConsumer.vertex(pose.pose(), k, l, m).color(0, 0, 0, 255).normal(pose.normal(), n /= q, o /= q, p /= q).endVertex();
    }

    @Override
    public ResourceLocation getTextureLocation(EntityCustomFishingHook entity) {
        return TEXTURE_LOCATION;
    }
}
