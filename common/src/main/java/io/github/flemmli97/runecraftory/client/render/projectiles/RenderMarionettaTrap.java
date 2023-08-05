package io.github.flemmli97.runecraftory.client.render.projectiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.model.misc.ModelChest;
import io.github.flemmli97.runecraftory.common.entities.misc.EntityMarionettaTrap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RenderMarionettaTrap extends EntityRenderer<EntityMarionettaTrap> {

    protected static final ResourceLocation common = new ResourceLocation(RuneCraftory.MODID, "textures/entity/chest.png");
    protected static final ResourceLocation rare = new ResourceLocation(RuneCraftory.MODID, "textures/entity/rare_chest.png");

    protected final EntityModel<EntityMarionettaTrap> model;

    private final ItemStack sword = new ItemStack(Items.IRON_SWORD);

    public RenderMarionettaTrap(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ModelChest<>(ctx.bakeLayer(ModelChest.LAYER_LOCATION));
    }

    @Override
    public void render(EntityMarionettaTrap entity, float rotation, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
        stack.pushPose();
        stack.scale(1.5f, 1.5f, 1.5f);
        float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        float partialLivingTicks = (float) entity.tickCount + partialTicks;
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F + yaw));
        stack.scale(-1.0f, -1.0f, 1.0f);
        stack.translate(0.0, -1.501f, 0.0);
        this.model.prepareMobModel(entity, 0.0F, 0.0F, partialTicks);
        this.model.setupAnim(entity, 0.0F, 0.0F, partialLivingTicks, yaw, pitch);
        VertexConsumer ivertexbuilder = buffer.getBuffer(this.model.renderType(this.getTextureLocation(entity)));
        this.model.renderToBuffer(stack, ivertexbuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
        if (entity.getTickLeft() < 50) {
            float subX = 1 / 6f;
            for (int i = 0; i < 6; i++) {
                float x = Math.max(0, 5 - Math.max(0, (-entity.getTickLeft() + 15 + i * 3)) * subX * 4);
                if (x <= 0)
                    continue;
                stack.pushPose();
                stack.translate(0, 2 - Math.max(0, (-entity.getTickLeft() + 15 + i * 3)) * subX * 1.6, 0);
                stack.mulPose(Vector3f.YP.rotationDegrees((entity.tickCount + partialTicks) * 4 + 72 * i));
                stack.translate(x, 0, 0);
                stack.mulPose(Vector3f.YP.rotationDegrees(90));
                stack.mulPose(Vector3f.XP.rotationDegrees(-50));
                Minecraft.getInstance().getItemRenderer().renderStatic(this.sword, ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND,
                        0xff00ff, OverlayTexture.NO_OVERLAY, stack, buffer, 0);
                stack.popPose();
            }
        }
        super.render(entity, rotation, partialTicks, stack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(EntityMarionettaTrap entity) {
        return common;
    }
}
