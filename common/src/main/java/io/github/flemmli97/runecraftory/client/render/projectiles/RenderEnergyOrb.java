package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.ModelEnergyOrb;
import io.github.flemmli97.runecraftory.client.render.layer.EnergyOrbSwirlLayer;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityHomingEnergyOrb;
import io.github.flemmli97.tenshilib.client.render.RenderProjectileModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class RenderEnergyOrb extends RenderProjectileModel<EntityHomingEnergyOrb> implements RenderLayerParent<EntityHomingEnergyOrb, EntityModel<EntityHomingEnergyOrb>> {

    public static final ResourceLocation TEXTURE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/projectile/energy_orb.png");
    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(new ResourceLocation("textures/entity/guardian_beam.png"));

    private final EnergyOrbSwirlLayer layer;

    public RenderEnergyOrb(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelEnergyOrb<>(ctx.bakeLayer(ModelEnergyOrb.LAYER_LOCATION)));
        this.layer = new EnergyOrbSwirlLayer(this, ctx.getModelSet());
        this.alpha = 0.8f;
    }

    @Override
    public void render(EntityHomingEnergyOrb entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
        stack.popPose();
        Vec3 start = entity.rootPosition(partialTicks);
        if (start == null)
            return;
        Vec3 vec32 = this.getPositionLerped(entity, partialTicks);
        Vec3 dir = start.subtract(vec32);
        float len = (float) (dir.length() + 1.0);
        dir = dir.normalize();
        float beamPitch = (float) Math.acos(dir.y);
        float beamYaw = (float) Math.atan2(dir.z, dir.x);
        stack.pushPose();
        stack.mulPose(Vector3f.YP.rotationDegrees(90 - beamYaw * Mth.RAD_TO_DEG));
        stack.mulPose(Vector3f.XP.rotationDegrees(beamPitch * Mth.RAD_TO_DEG));

        float colorScale = Mth.sin(entity.tickCount) * 0.5f + 0.5f;
        int red = 66 + (int) (colorScale * (166 - 66));
        int green = 154 + (int) (colorScale * (210 - 154));
        int blue = 207 - (int) (colorScale * (237 - 207));

        float tick = entity.tickCount + partialTicks;
        float h = tick * 0.5f % 1.0f;
        float n = tick * 0.05f * -1.5f;

        float u = Mth.cos(n + 2.3561945f) * 0.282f;
        float v = Mth.sin(n + 2.3561945f) * 0.282f;
        float w = Mth.cos(n + 0.7853982f) * 0.282f;
        float x = Mth.sin(n + 0.7853982f) * 0.282f;
        float y = Mth.cos(n + 3.926991f) * 0.282f;
        float z = Mth.sin(n + 3.926991f) * 0.282f;
        float aa = Mth.cos(n + 5.4977875f) * 0.282f;
        float ab = Mth.sin(n + 5.4977875f) * 0.282f;
        float ac = Mth.cos(n + (float) Math.PI) * 0.2f;
        float ad = Mth.sin(n + (float) Math.PI) * 0.2f;
        float ae = Mth.cos(n + 0.0f) * 0.2f;
        float af = Mth.sin(n + 0.0f) * 0.2f;
        float ag = Mth.cos(n + 1.5707964f) * 0.2f;
        float ah = Mth.sin(n + 1.5707964f) * 0.2f;
        float ai = Mth.cos(n + 4.712389f) * 0.2f;
        float aj = Mth.sin(n + 4.712389f) * 0.2f;
        float an = -1.0f + h;
        float ao = len * 2.5f + an;
        VertexConsumer vertexConsumer = buffer.getBuffer(BEAM_RENDER_TYPE);
        PoseStack.Pose pose = stack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        vertex(vertexConsumer, matrix4f, matrix3f, ac, len, ad, red, green, blue, 0.4999f, ao);
        vertex(vertexConsumer, matrix4f, matrix3f, ac, 0.0f, ad, red, green, blue, 0.4999f, an);
        vertex(vertexConsumer, matrix4f, matrix3f, ae, 0.0f, af, red, green, blue, 0.0f, an);
        vertex(vertexConsumer, matrix4f, matrix3f, ae, len, af, red, green, blue, 0.0f, ao);
        vertex(vertexConsumer, matrix4f, matrix3f, ag, len, ah, red, green, blue, 0.4999f, ao);
        vertex(vertexConsumer, matrix4f, matrix3f, ag, 0.0f, ah, red, green, blue, 0.4999f, an);
        vertex(vertexConsumer, matrix4f, matrix3f, ai, 0.0f, aj, red, green, blue, 0.0f, an);
        vertex(vertexConsumer, matrix4f, matrix3f, ai, len, aj, red, green, blue, 0.0f, ao);
        float ap = 0.0f;
        if (entity.tickCount % 2 == 0) {
            ap = 0.5f;
        }
        vertex(vertexConsumer, matrix4f, matrix3f, u, len, v, red, green, blue, 0.5f, ap + 0.5f);
        vertex(vertexConsumer, matrix4f, matrix3f, w, len, x, red, green, blue, 1.0f, ap + 0.5f);
        vertex(vertexConsumer, matrix4f, matrix3f, aa, len, ab, red, green, blue, 1.0f, ap);
        vertex(vertexConsumer, matrix4f, matrix3f, y, len, z, red, green, blue, 0.5f, ap);
        stack.popPose();
    }

    private Vec3 getPositionLerped(Entity livingEntity, float partialTick) {
        double x = Mth.lerp(partialTick, livingEntity.xOld, livingEntity.getX());
        double y = Mth.lerp(partialTick, livingEntity.yOld, livingEntity.getY()) + livingEntity.getBbHeight() * 0.5;
        double z = Mth.lerp(partialTick, livingEntity.zOld, livingEntity.getZ());
        return new Vec3(x, y, z);
    }

    @Override
    public void translate(EntityHomingEnergyOrb entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        float scale = 0.85f + Mth.sin(entity.tickCount * 0.2f) * 0.075f;
        stack.scale(scale, scale, scale);
        stack.scale(-1.0f, -1.0f, 1.0f);
    }

    @Override
    public void afterModelRender(EntityHomingEnergyOrb entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot()) + this.yawOffset();
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot()) + this.pitchOffset();
        this.layer.render(stack, buffer, packedLight, entity, 0, 0, partialTicks, entity.tickCount, yaw, pitch);
    }

    private static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, float x, float y, float z, int r, int g, int b, float u, float v) {
        vertexConsumer.vertex(matrix4f, x, y, z).color(r, g, b, 200).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(0xF000F0).normal(matrix3f, 0.0f, 1.0f, 0.0f).endVertex();
    }

    @Override
    public EntityModel<EntityHomingEnergyOrb> getModel() {
        return this.model;
    }

    @Override
    public ResourceLocation getTextureLocation(EntityHomingEnergyOrb entity) {
        return TEXTURE;
    }
}
