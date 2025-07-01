package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.entities.GateEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class RenderGate extends EntityRenderer<GateEntity> {

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

    public RenderGate(EntityRendererProvider.Context ctx) {
        super(ctx);
        ClientHandlers.initNonRendererModels(ctx);
    }

    @Override
    protected int getBlockLightLevel(GateEntity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(GateEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        float scale = 1.2f + Mth.sin(entity.tickCount * 0.1f) * 0.04f;
        stack.scale(scale, scale, scale);
        stack.translate(0, entity.getBbHeight() * 0.5 - 0.1, 0);
        stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        stack.mulPose(Axis.ZP.rotationDegrees(entity.clientRenderTick * 0.1f));

        float xSize = 1.5f / 2f;
        float ySize = 1.5f / 2f;
        Matrix4f matrix4f = stack.last().pose();
        float[][] colors = this.getColor(entity);
        VertexConsumer builder = bufferIn.getBuffer(RunecraftoryShaders.GATE_RENDER);
        builder.addVertex(matrix4f, -xSize, -ySize, 0).setColor(colors[0][0], colors[0][1], colors[0][2], 1)
                .setNormal(colors[1][0], colors[1][1], colors[1][2])
                .setUv(0, 1).setUv2(entity.getId(), 0).setOverlay(LivingEntityRenderer.getOverlayCoords(entity, 0));
        builder.addVertex(matrix4f, xSize, -ySize, 0).setColor(colors[0][0], colors[0][1], colors[0][2], 1)
                .setNormal(colors[1][0], colors[1][1], colors[1][2])
                .setUv(1, 1).setUv2(entity.getId(), 0).setOverlay(LivingEntityRenderer.getOverlayCoords(entity, 0));
        builder.addVertex(matrix4f, xSize, ySize, 0).setColor(colors[0][0], colors[0][1], colors[0][2], 1)
                .setNormal(colors[1][0], colors[1][1], colors[1][2])
                .setUv(1, 0).setUv2(entity.getId(), 0).setOverlay(LivingEntityRenderer.getOverlayCoords(entity, 0));
        builder.addVertex(matrix4f, -xSize, ySize, 0).setColor(colors[0][0], colors[0][1], colors[0][2], 1)
                .setNormal(colors[1][0], colors[1][1], colors[1][2])
                .setUv(0, 0).setUv2(entity.getId(), 0).setOverlay(LivingEntityRenderer.getOverlayCoords(entity, 0));
        stack.popPose();
    }

    @Override
    protected boolean shouldShowName(GateEntity entity) {
        return false;
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
