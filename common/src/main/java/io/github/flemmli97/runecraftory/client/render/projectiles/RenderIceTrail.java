package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityIceTrail;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class RenderIceTrail extends EntityRenderer<EntityIceTrail> {

    private final BlockState ice = Blocks.ICE.defaultBlockState();

    public RenderIceTrail(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(EntityIceTrail entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.2f, 1.2f, 1.2f);
        this.renderBlockModel(this.ice, stack, buffer, 0xff00ff);
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityIceTrail entity) {
        return null;
    }

    private void renderBlockModel(BlockState state, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.translate(-0.5, 0, -0.5);
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BakedModel model = dispatcher.getBlockModel(state);
        dispatcher.getModelRenderer().renderModel(stack.last(), buffer.getBuffer(Sheets.solidBlockSheet()), state, model, 0.0f, 0.0f, 0.0f, packedLight, OverlayTexture.NO_OVERLAY);
    }

}