package io.github.flemmli97.runecraftory.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ChestModel;
import io.github.flemmli97.runecraftory.common.entities.misc.TreasureChestEntity;
import io.github.flemmli97.runecraftory.common.items.creative.TreasureChestSpawnegg;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderTreasureChest extends EntityRenderer<TreasureChestEntity> {

    protected static final ResourceLocation COMMON = RuneCraftory.modRes("textures/entity/chest.png");
    protected static final ResourceLocation RARE = RuneCraftory.modRes("textures/entity/rare_chest.png");
    protected static final ResourceLocation QUEST = RuneCraftory.modRes("textures/entity/quest_chest.png");

    protected final EntityModel<TreasureChestEntity> model;

    public RenderTreasureChest(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.shadowRadius = 0.6f;
        this.model = new ChestModel<>();
    }

    @Override
    public void render(TreasureChestEntity entity, float rotation, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        float yaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        float partialLivingTicks = (float) entity.tickCount + partialTick;
        this.translate(entity, stack, pitch, yaw, partialTick);
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.translate(0.0, -1.501f, 0.0);
        this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTick);
        this.model.setupAnim(entity, 0.0F, 0.0F, partialLivingTicks, yaw, pitch);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY);
        stack.popPose();
        super.render(entity, rotation, partialTick, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TreasureChestEntity entity) {
        TreasureChestSpawnegg.ChestTier tier = entity.tier();
        if (tier == TreasureChestSpawnegg.ChestTier.RARE || tier == TreasureChestSpawnegg.ChestTier.EPIC)
            return RARE;
        if (tier == TreasureChestSpawnegg.ChestTier.QUEST)
            return QUEST;
        return COMMON;
    }

    public void translate(TreasureChestEntity entity, PoseStack stack, float pitch, float yaw, float partialTick) {
        stack.mulPose(Axis.YP.rotationDegrees(180.0F + yaw));
        stack.mulPose(Axis.XP.rotationDegrees(pitch));
    }
}
