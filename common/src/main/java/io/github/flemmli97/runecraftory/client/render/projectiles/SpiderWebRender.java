package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.common.entities.misc.SpiderWebEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpiderWebRender extends EntityRenderer<SpiderWebEntity> {

    private final ItemRenderer itemRenderer;
    private final ItemStack stack = new ItemStack(Items.COBWEB);

    public SpiderWebRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(SpiderWebEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1, 1, 1);
        stack.mulPose(Axis.YP.rotationDegrees(entity.yRotO + 45));
        this.itemRenderer.renderStatic(this.stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
        stack.mulPose(Axis.YP.rotationDegrees(90));
        this.itemRenderer.renderStatic(this.stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SpiderWebEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
