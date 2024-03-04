package io.github.flemmli97.runecraftory.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ArmorSimpleItemModel extends EntityModel<Entity> {

    public static TriConsumer<LivingEntity, PoseStack, ModelPart> translateToHead = (entity, poseStack, part) -> {
        boolean villager = entity instanceof Villager;
        if (entity.isBaby() && !villager) {
            poseStack.translate(0.0, 0.03125, 0.0);
            poseStack.scale(0.7f, 0.7f, 0.7f);
            poseStack.translate(0.0, 1.0, 0.0);
        }
        part.translateAndRotate(poseStack);
        CustomHeadLayer.translateToHead(poseStack, villager);
    };

    private LivingEntity entity;
    private ItemStack stack;
    @Nullable
    private ModelPart part;
    private TriConsumer<LivingEntity, PoseStack, ModelPart> translate;

    public void setProperties(LivingEntity entity, ItemStack stack, ModelPart part, TriConsumer<LivingEntity, PoseStack, ModelPart> translate) {
        this.entity = entity;
        this.stack = stack;
        this.part = part;
        this.translate = translate;
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (this.stack == null || this.stack.isEmpty() || this.entity == null)
            return;
        poseStack.pushPose();
        if (this.translate != null)
            this.translate.accept(this.entity, poseStack, this.part);
        Minecraft.getInstance().getItemInHandRenderer().renderItem(this.entity, this.stack, ItemTransforms.TransformType.HEAD, false, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), packedLight);
        poseStack.popPose();
    }

    public interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}
