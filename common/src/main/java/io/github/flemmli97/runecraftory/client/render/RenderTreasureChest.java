package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelChest;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityTreasureChest;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderTreasureChest extends EntityRenderer<EntityTreasureChest> {

    protected static final ResourceLocation common = new ResourceLocation(RuneCraftory.MODID, "textures/entity/chest.png");
    protected static final ResourceLocation rare = new ResourceLocation(RuneCraftory.MODID, "textures/entity/rare_chest.png");
    protected static final ResourceLocation quest = new ResourceLocation(RuneCraftory.MODID, "textures/entity/quest_chest.png");

    protected final EntityModel<EntityTreasureChest> model;

    public RenderTreasureChest(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelChest<>(ctx.bakeLayer(ModelChest.LAYER_LOCATION));
    }

    @Override
    public void render(EntityTreasureChest entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float partialLivingTicks = (float) entity.tickCount + partialTicks;
        this.translate(entity, stack, pitch, yaw, partialTicks);
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.translate(0.0, -1.501f, 0.0);
        this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
        this.model.setupAnim(entity, 0.0F, 0.0F, partialLivingTicks, yaw, pitch);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityTreasureChest entity) {
        int tier = entity.tier();
        if (tier == 2 || tier == 3)
            return rare;
        if (tier == 4)
            return quest;
        return common;
    }

    public void translate(EntityTreasureChest entity, PoseStack stack, float pitch, float yaw, float partialTicks) {
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F + yaw));
        stack.mulPose(Vector3f.XP.rotationDegrees(pitch));
    }
}
