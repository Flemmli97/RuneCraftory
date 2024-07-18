package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.client.model.ModelGate;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderGate extends LivingEntityRenderer<GateEntity, ModelGate> {

    /*private static final ResourceLocation NONE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_none.png");
    private static final ResourceLocation FIRE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_fire.png");
    private static final ResourceLocation WATER = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_water.png");
    private static final ResourceLocation EARTH = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_earth.png");
    private static final ResourceLocation WIND = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_wind.png");
    private static final ResourceLocation LIGHT = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_light.png");
    private static final ResourceLocation DARK = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_dark.png");
    private static final ResourceLocation LOVE = new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_love.png");*/
    private static final float[][] NONE = new float[][]{
            new float[]{0.310f, 0.470f, 0.298f},
            new float[]{0.414f, 0.083f, 0.495f}
    };
    private static final float[][] FIRE = new float[][]{
            new float[]{0.980f, 0.038f, 0.019f},
            new float[]{0.970f, 0.613f, 0.120f}
    };
    private static final float[][] WATER = new float[][]{
            new float[]{0.000f, 0.150f, 0.570f},
            new float[]{0.540f, 0.924f, 0.970f}
    };
    private static final float[][] EARTH = new float[][]{
            new float[]{0.450f, 0.316f, 0.094f},
            new float[]{0.689f, 0.775f, 0.149f}
    };
    private static final float[][] WIND = new float[][]{
            new float[]{0.161f, 0.715f, 0.052f},
            new float[]{0.533f, 0.880f, 0.305f}
    };
    private static final float[][] LIGHT = new float[][]{
            new float[]{0.480f, 0.428f, 0.024f},
            new float[]{0.606f, 0.880f, 0.728f}
    };
    private static final float[][] DARK = new float[][]{
            new float[]{0.075f, 0.042f, 0.325f},
            new float[]{0.405f, 0.064f, 0.328f}
    };
    private static final float[][] LOVE = new float[][]{
            new float[]{0.295f, 0.155f, 0.258f},
            new float[]{0.920f, 0.846f, 0.723f}
    };
    //protected final RenderUtils.TextureBuilder textureBuilder = new RenderUtils.TextureBuilder();

    public RenderGate(EntityRendererProvider.Context ctx) {
        super(ctx, new ModelGate(ctx.bakeLayer(ModelGate.LAYER_LOCATION)), 0);
        ClientHandlers.initNonRendererModels(ctx);
    }

    @Override
    public void render(GateEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        float scale = 1.2f + Mth.sin(entity.tickCount * 0.1f) * 0.04f;
        stack.scale(scale, scale, scale);
        stack.translate(0, entity.getBbHeight() * 0.5 - 0.1, 0);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        stack.mulPose(Vector3f.ZP.rotationDegrees(entity.clientParticles * 0.1f));

        float xSize = 1.5f / 2f;
        float ySize = 1.5f / 2f;
        Matrix4f matrix4f = stack.last().pose();
        float[][] colors = this.getColor(entity);
        VertexConsumer builder = bufferIn.getBuffer(RunecraftoryShaders.GATE_RENDER);
        builder.vertex(matrix4f, -xSize, -ySize, 0).color(colors[0][0], colors[0][1], colors[0][2], 1)
                .color(colors[1][0], colors[1][1], colors[1][2], 1).uv(0, 1).overlayCoords(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks))).endVertex();
        builder.vertex(matrix4f, xSize, -ySize, 0).color(colors[0][0], colors[0][1], colors[0][2], 1)
                .color(colors[1][0], colors[1][1], colors[1][2], 1).uv(1, 1).overlayCoords(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks))).endVertex();
        builder.vertex(matrix4f, xSize, ySize, 0).color(colors[0][0], colors[0][1], colors[0][2], 1)
                .color(colors[1][0], colors[1][1], colors[1][2], 1).uv(1, 0).overlayCoords(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks))).endVertex();
        builder.vertex(matrix4f, -xSize, ySize, 0).color(colors[0][0], colors[0][1], colors[0][2], 1)
                .color(colors[1][0], colors[1][1], colors[1][2], 1).uv(0, 0).overlayCoords(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks))).endVertex();
        // Legacy
        // stack.translate(0, 0, 0.1);
        // this.textureBuilder.setOverlay(getOverlayCoords(entity, this.getWhiteOverlayProgress(entity, partialTicks)));
        // this.textureBuilder.setLight(packedLightIn);
        // RenderUtils.renderTexture(stack, bufferIn.getBuffer(RenderType.entityTranslucent(new ResourceLocation(RuneCraftory.MODID, "textures/entity/gate_dark.png"))), 1, 1, this.textureBuilder);
        stack.popPose();
    }

    @Override
    protected float getFlipDegrees(GateEntity entityLivingBaseIn) {
        return 0;
    }

    @Override
    protected boolean shouldShowName(GateEntity entity) {
        return false;
    }

    @Override
    protected int getBlockLightLevel(GateEntity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(GateEntity entity) {
        return null;
    }

    private float[][] getColor(GateEntity entity) {
        return switch (entity.getElement()) {
            case NONE -> NONE;
            case WATER -> WATER;
            case EARTH -> EARTH;
            case WIND -> WIND;
            case FIRE -> FIRE;
            case LIGHT -> LIGHT;
            case DARK -> DARK;
            case LOVE -> LOVE;
        };
    }
}
