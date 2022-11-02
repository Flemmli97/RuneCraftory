package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderGate extends LivingEntityRenderer<GateEntity, ModelGate> {

    private static final ResourceLocation none = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_none.png");
    private static final ResourceLocation fire = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_fire.png");
    private static final ResourceLocation water = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_water.png");
    private static final ResourceLocation earth = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_earth.png");
    private static final ResourceLocation wind = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_wind.png");
    private static final ResourceLocation light = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_light.png");
    private static final ResourceLocation dark = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_dark.png");
    private static final ResourceLocation love = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_love.png");
    protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderGate(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelGate(ctx.bakeLayer(ModelGate.LAYER_LOCATION)), 0);
        ClientHandlers.initNonRendererModels(ctx);
    }

    @Override
    public void render(GateEntity entity, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        /*ParticleStatus status = Minecraft.getInstance().gameSettings.particles;
        if (status == ParticleStatus.ALL) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 3,
                        0, 0.01f, entity.clientParticles, 5), true, entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else if (status == ParticleStatus.DECREASED) {
            if (entity.clientParticleFlag && entity.ticksExisted % 2 == 0) {
                int color = entity.entityElement().getParticleColor();
                float r = (color >> 16 & 255) / 255.0F;
                float g = (color >> 8 & 255) / 255.0F;
                float b = (color & 255) / 255.0F;
                entity.world.addOptionalParticle(new ColoredParticleData4f(ModParticles.vortex.get(),
                        r, g, b, 1, 3,
                        0, 0.01f, entity.clientParticles, 5), entity.getPosX(), entity.getPosY() + entity.getHeight() * 0.5, entity.getPosZ(), 0, 0, 0);
                entity.clientParticleFlag = false;
            }
        } else
            super.render(entity, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);*/
        matrixStackIn.pushPose();
        float scale = 1.2f + Mth.sin(entity.tickCount * 0.1f) * 0.04f;
        matrixStackIn.scale(scale, scale, scale);
        matrixStackIn.translate(0, entity.getBbHeight() * 0.5 - 0.1, 0);
        matrixStackIn.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(entity.clientParticles * 0.1f));

        this.textureBuilder.setOverlay(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks)));
        this.textureBuilder.setLight(packedLightIn);
        RenderUtils.renderTexture(matrixStackIn, bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity))), 1, 1, this.textureBuilder);
        matrixStackIn.popPose();
    }

    @Override
    protected float getFlipDegrees(GateEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    protected boolean shouldShowName(GateEntity entity) {
        return false;
    }

    /*@Override
    protected void preRenderCallback(GateEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        if (Minecraft.getInstance().gameSettings.particles == ParticleStatus.DECREASED) {
            matrixStackIn.scale(0.3f, 0.3f, 0.3f);
            matrixStackIn.translate(0, -1, 0);
        }
    }*/

    @Override
    protected int getBlockLightLevel(GateEntity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(GateEntity entity) {
        return switch (entity.getElement()) {
            case NONE -> none;
            case WATER -> water;
            case EARTH -> earth;
            case WIND -> wind;
            case FIRE -> fire;
            case LIGHT -> light;
            case DARK -> dark;
            case LOVE -> love;
        };
    }
}
