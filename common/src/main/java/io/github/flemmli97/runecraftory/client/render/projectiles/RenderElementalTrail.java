package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityElementalTrail;
import io.github.flemmli97.tenshilib.client.render.RenderTexture;
import io.github.flemmli97.tenshilib.client.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RenderElementalTrail extends RenderTexture<EntityElementalTrail> {

    private static final ResourceLocation DARK = new ResourceLocation("minecraft", "block/soul_fire_0");
    private static final ResourceLocation FIRE = new ResourceLocation("minecraft", "block/fire_0");

    private final BlockState ice = Blocks.ICE.defaultBlockState();
    private final BlockState dirt = Blocks.COARSE_DIRT.defaultBlockState();

    public RenderElementalTrail(EntityRendererProvider.Context ctx) {
        super(ctx, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityElementalTrail entity) {
        return switch (entity.element()) {
            case FIRE -> FIRE;
            default -> DARK;
        };
    }

    @Override
    public void render(EntityElementalTrail entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        switch (entity.element()) {
            case FIRE, DARK -> {
                stack.translate(0, this.ySize * 0.45, 0);
                stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
                stack.mulPose(Vector3f.YP.rotationDegrees(180));
                var t = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(this.getTextureLocation(entity));
                this.textureBuilder.setUV(t.getU0(), t.getV0());
                this.textureBuilder.setUVLength(t.getU1() - t.getU0(), t.getV1() - t.getV0());
                RenderUtils.renderTexture(stack, buffer.getBuffer(this.getRenderType(entity, t.atlas().location())), 1, 2, this.textureBuilder);
            }
            case WATER -> this.renderBlockModel(this.ice, stack, buffer);
            case EARTH -> this.renderBlockModel(this.dirt, stack, buffer);
        }
        stack.popPose();
    }

    private void renderBlockModel(BlockState state, PoseStack stack, MultiBufferSource buffer) {
        stack.translate(-0.5, -0.5, -0.5);
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModel(state);
        dispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), state, model, 0.0f, 0.0f, 0.0f, 16711935, OverlayTexture.NO_OVERLAY);
    }
}
