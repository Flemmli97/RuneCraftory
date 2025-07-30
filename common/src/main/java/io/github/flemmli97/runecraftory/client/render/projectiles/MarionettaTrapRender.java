package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ChestModel;
import io.github.flemmli97.runecraftory.common.entities.misc.MarionettaTrapEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class MarionettaTrapRender extends EntityRenderer<MarionettaTrapEntity> {

    protected static final ResourceLocation COMMON = RuneCraftory.modRes("textures/entity/chest.png");
    private static final float SWORD_OFFSET = 360f / MarionettaTrapEntity.SWORDS;

    protected final EntityModel<MarionettaTrapEntity> model;

    private final ItemStack sword = new ItemStack(Items.IRON_SWORD);

    public MarionettaTrapRender(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ChestModel<>();
    }

    @Override
    public void render(MarionettaTrapEntity entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.5f, 1.5f, 1.5f);
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float partialLivingTicks = (float) entity.tickCount + partialTicks;
        stack.mulPose(Axis.YP.rotationDegrees(180.0F + yaw));
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.translate(0.0, -1.5, 0.0);
        this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
        this.model.setupAnim(entity, 0.0F, 0.0F, partialLivingTicks, yaw, pitch);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, CommonColors.WHITE);
        stack.popPose();
        for (int i = 0; i < MarionettaTrapEntity.SWORDS; i++) {
            float rotationSword = entity.getSpinProgress(partialTicks) * 480 - SWORD_OFFSET * i;
            if (rotationSword <= 0)
                return;
            entity.playSpawnSound(i);
            this.renderSwords(stack, entity, buffer, rotationSword, entity.getAttackProgress(i, partialTicks));
        }
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    private void renderSwords(PoseStack stack, MarionettaTrapEntity entity, MultiBufferSource buffer,
                              float rotation, float attackProgress) {
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(rotation));
        stack.mulPose(Axis.XP.rotationDegrees(-15));
        stack.translate(0, 0, 5 - (5 * attackProgress));
        stack.mulPose(Axis.XP.rotationDegrees(-12));
        Minecraft.getInstance().getItemRenderer().renderStatic(this.sword, ItemDisplayContext.FIRST_PERSON_LEFT_HAND,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, stack, buffer, entity.level(), entity.getId());
        stack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(MarionettaTrapEntity entity) {
        return COMMON;
    }
}
