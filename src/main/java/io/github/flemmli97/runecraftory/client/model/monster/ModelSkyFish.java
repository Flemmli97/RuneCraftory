package io.github.flemmli97.runecraftory.client.model.monster;// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.flemmli97.tenshilib.api.entity.AnimatedAction;
import com.flemmli97.tenshilib.client.model.BlockBenchAnimations;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.monster.EntitySkyFish;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;

public class ModelSkyFish<T extends EntitySkyFish> extends EntityModel<T> implements IResetModel {

    public ModelRendererPlus body;
    public ModelRendererPlus head;
    public ModelRendererPlus tail;
    public ModelRendererPlus tailTip;
    public ModelRendererPlus caudalFin;
    public ModelRendererPlus dorsalFin;
    public ModelRendererPlus adiposeFin;
    public ModelRendererPlus leftPectoralFin;
    public ModelRendererPlus rightPectoralFin;
    public ModelRendererPlus leftPelvicFin;
    public ModelRendererPlus rightPelvicFin;

    public BlockBenchAnimations animations;

    public ModelSkyFish() {
        this.textureWidth = 16;
        this.textureHeight = 16;

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.5F, 18.0F, 1.0F);
        this.body.setTextureOffset(0, 0).addBox(-3.5F, -5.0F, -9.0F, 6.0F, 10.0F, 17.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(2.5F, -3.0F, -8.0F, 1.0F, 6.0F, 16.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-4.5F, -3.0F, -8.0F, 1.0F, 6.0F, 16.0F, 0.0F, false);
        this.body.setTextureOffset(0, 0).addBox(-2.5F, -6.0F, -7.0F, 4.0F, 1.0F, 14.0F, 0.0F, false);

        this.head = new ModelRendererPlus(this);
        this.head.setDefaultRotPoint(-0.5F, -2.0F, -8.5F);
        this.body.addChild(this.head);
        this.head.setTextureOffset(0, 0).addBox(-2.0F, -0.5F, -3.0F, 4.0F, 5.0F, 1.0F, 0.0F, false);
        this.head.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 8.0F, 2.0F, 0.0F, false);

        this.tail = new ModelRendererPlus(this);
        this.tail.setDefaultRotPoint(-0.5F, -1.5F, 7.0F);
        this.body.addChild(this.tail);
        this.tail.setTextureOffset(0, 0).addBox(-2.5F, -2.0F, 0.0F, 5.0F, 7.0F, 5.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(2.5F, -0.5F, 0.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);
        this.tail.setTextureOffset(0, 0).addBox(-3.5F, -0.5F, 0.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

        this.tailTip = new ModelRendererPlus(this);
        this.tailTip.setDefaultRotPoint(0.0F, 0.0F, 4.0F);
        this.tail.addChild(this.tailTip);
        this.tailTip.setTextureOffset(0, 0).addBox(-2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 5.0F, 0.0F, false);

        this.caudalFin = new ModelRendererPlus(this);
        this.caudalFin.setDefaultRotPoint(0.0F, 0.0F, 5.0F);
        this.tailTip.addChild(this.caudalFin);
        this.caudalFin.setTextureOffset(0, 0).addBox(0.0F, -6.0F, 0.0F, 0.0F, 14.0F, 9.0F, 0.0F, false);

        this.dorsalFin = new ModelRendererPlus(this);
        this.dorsalFin.setDefaultRotPoint(0.0F, -8.0F, -7.0F);
        this.body.addChild(this.dorsalFin);
        this.dorsalFin.setTextureOffset(0, 0).addBox(-0.5F, -1.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, false);

        this.adiposeFin = new ModelRendererPlus(this);
        this.adiposeFin.setDefaultRotPoint(-0.5F, -8.0F, 3.0F);
        this.body.addChild(this.adiposeFin);
        this.adiposeFin.setTextureOffset(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 4.0F, 0.0F, false);

        this.leftPectoralFin = new ModelRendererPlus(this);
        this.leftPectoralFin.setDefaultRotPoint(3.5F, 0.0F, -5.0F);
        this.body.addChild(this.leftPectoralFin);
        this.setRotationAngle(this.leftPectoralFin, -0.3054F, 0.4363F, -0.1309F);
        this.leftPectoralFin.setTextureOffset(0, 0).addBox(-0.2366F, 0.9243F, 0.491F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        this.rightPectoralFin = new ModelRendererPlus(this);
        this.rightPectoralFin.setDefaultRotPoint(-4.5F, 0.0F, -5.0F);
        this.body.addChild(this.rightPectoralFin);
        this.setRotationAngle(this.rightPectoralFin, -0.3054F, -0.4363F, 0.1309F);
        this.rightPectoralFin.setTextureOffset(0, 0).addBox(0.2366F, 0.9243F, 0.491F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        this.leftPelvicFin = new ModelRendererPlus(this);
        this.leftPelvicFin.setDefaultRotPoint(2.5F, 2.0F, 4.0F);
        this.body.addChild(this.leftPelvicFin);
        this.setRotationAngle(this.leftPelvicFin, -0.1745F, 0.3927F, 0.0F);
        this.leftPelvicFin.setTextureOffset(0, 0).addBox(0.0F, 0.9696F, 0.3473F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        this.rightPelvicFin = new ModelRendererPlus(this);
        this.rightPelvicFin.setDefaultRotPoint(-3.5F, 2.0F, 4.0F);
        this.body.addChild(this.rightPelvicFin);
        this.setRotationAngle(this.rightPelvicFin, -0.1745F, -0.3927F, 0.0F);
        this.rightPelvicFin.setTextureOffset(0, 0).addBox(0.0F, 0.9696F, 0.3473F, 0.0F, 2.0F, 4.0F, 0.0F, false);

        this.animations = new BlockBenchAnimations(this, new ResourceLocation(RuneCraftory.MODID, "models/entity/animation/sky_fish.json"));
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.body.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    @Override
    public void setRotationAngles(T fish, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.resetModel();
        AnimatedAction anim = fish.getAnimationHandler().getAnimation().orElse(null);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        if (anim == null) {
            this.animations.doAnimation("swim", fish.ticksExisted, partialTicks);
        } else
            this.animations.doAnimation(anim.getAnimationClient(), anim.getTick(), partialTicks);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.body.reset();
        this.resetChild(this.body);
    }
}