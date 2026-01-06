package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.tenshilib.client.model.RideableModel;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import io.github.flemmli97.tenshilib.client.render.layer.RiderEntityLayer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Random;

public class RenderMonster<T extends BaseMonster, M extends EntityModel<T> & RideableModel<T>> extends MobRenderer<T, M> {

    private final ResourceLocation tex;

    private final Random random = new Random();

    private final RenderUtils.BeamBuilder builder = create();

    public RenderMonster(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow) {
        this(ctx, model, texture, shadow, true);
    }

    public RenderMonster(EntityRendererProvider.Context ctx, M model, ResourceLocation texture, float shadow, boolean withDefaultRiderLayer) {
        super(ctx, model, shadow);
        this.tex = texture;
        if (withDefaultRiderLayer)
            this.layers.add(new RiderEntityLayer<>(this));
    }

    private static RenderUtils.BeamBuilder create() {
        RenderUtils.BeamBuilder beam = new RenderUtils.BeamBuilder();
        beam.setEndColor(0x50c5f0);
        return beam;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        if (entity.deathRays() > 0) {
            poseStack.pushPose();
            poseStack.translate(0, entity.deathRayOffset(), 0);
            this.random.setSeed(entity.getUUID().getLeastSignificantBits());
            for (int i = 0; i < entity.deathRays(); i++) {
                poseStack.mulPose(Axis.XP.rotationDegrees(this.random.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360.0F));
                poseStack.mulPose(Axis.ZP.rotationDegrees(this.random.nextFloat() * 360.0F));
                RenderUtils.renderGradientBeam3d(poseStack, buffer, entity.getBbWidth() + 3, 1, this.builder);
            }
            poseStack.popPose();
        }
    }

    @Override
    protected void setupRotations(T entity, PoseStack stack, float ageInTicks, float rotationYaw, float partialTick, float scale) {
        super.setupRotations(entity, stack, ageInTicks, rotationYaw, partialTick, scale);
        if (entity.getPlayDeathTick() > 0 && entity.getDeathAnimation() == null) {
            float f = (entity.getPlayDeathTick() + (entity.playDeath() ? partialTick : -partialTick)) / 20.0f * 1.6f;
            if ((f = Mth.sqrt(f)) > 1.0f) {
                f = 1.0f;
            }
            stack.translate(0, f * 0.1, -f * entity.getBbHeight() * 0.5);
            stack.mulPose(Axis.XP.rotationDegrees(f * this.getFlipDegrees(entity)));
        }
    }

    @Override
    protected float getFlipDegrees(T entity) {
        return entity.getDeathAnimation() != null ? 0 : super.getFlipDegrees(entity);
    }

    @Override
    public boolean shouldRender(T entity, Frustum camera, double camX, double camY, double camZ) {
        if (entity.getPlayDeathTick() > 0 && !entity.playDeath()) {
            if (entity.getPlayDeathTick() > 8) {
                if (entity.getPlayDeathTick() % 2 == 0)
                    return false;
            }
            if (entity.getPlayDeathTick() % 3 == 0)
                return false;
        }
        return super.shouldRender(entity, camera, camX, camY, camZ);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return this.tex;
    }
}
