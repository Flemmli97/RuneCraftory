package io.github.flemmli97.runecraftory.client.model.monster;

import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.github.flemmli97.runecraftory.common.entities.monster.EntityWooly;
import net.minecraft.client.renderer.entity.model.EntityModel;

public class ModelWoolyWool<T extends EntityWooly> extends EntityModel<T> {
    public ModelRendererPlus bodyCenter;
    public ModelRendererPlus body;
    public ModelRendererPlus bodyUp;
    public ModelRendererPlus armLeftBase;
    public ModelRendererPlus armRightBase;
    public ModelRendererPlus feetLeftBase;
    public ModelRendererPlus feetRightBase;

    public ModelWoolyWool() {
        this.textureWidth = 64;
        this.textureHeight = 62;

        this.bodyCenter = new ModelRendererPlus(this);
        this.bodyCenter.setDefaultRotPoint(0.0F, 17.75F, 0.0F);

        this.body = new ModelRendererPlus(this);
        this.body.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.bodyCenter.addChild(this.body);
        this.body.setTextureOffset(0, 31).addBox(-3.5F, -7.0F, -4.5F, 7, 13, 9, 0.25F, true);
        this.body.setTextureOffset(32, 30).addBox(-4.5F, -7.0F, -3.5F, 1, 13, 7, 0.25F, true);
        this.body.setTextureOffset(32, 30).addBox(3.5F, -7.0F, -3.5F, 1, 13, 7, 0.25F, false);

        this.bodyUp = new ModelRendererPlus(this);
        this.bodyUp.setDefaultRotPoint(0.0F, -8.0F, 0.0F);
        this.body.addChild(this.bodyUp);
        this.bodyUp.setTextureOffset(32, 14).addBox(-2.5F, -1.0F, -3.5F, 5, 2, 7, 0.25F, true);
        this.bodyUp.setTextureOffset(28, 23).addBox(2.5F, -1.0F, -2.5F, 1, 2, 5, 0.25F, false);
        this.bodyUp.setTextureOffset(28, 23).addBox(-3.5F, -1.0F, -2.5F, 1, 2, 5, 0.25F, true);

        this.armLeftBase = new ModelRendererPlus(this);
        this.armLeftBase.setDefaultRotPoint(3.75F, -3.0F, 0.0F);
        this.body.addChild(this.armLeftBase);
        this.setRotationAngle(this.armLeftBase, 0.1745F, 0.0F, 0.0F);
        this.armLeftBase.setTextureOffset(34, 50).addBox(0.25F, -1.0F, -1.0F, 2, 2, 2, 0.25F, false);

        this.armRightBase = new ModelRendererPlus(this);
        this.armRightBase.setDefaultRotPoint(-3.75F, -3.0F, 0.0F);
        this.body.addChild(this.armRightBase);
        this.setRotationAngle(this.armRightBase, 0.1745F, 0.0F, 0.0F);
        this.armRightBase.setTextureOffset(34, 50).addBox(-2.25F, -1.0F, -1.0F, 2, 2, 2, 0.25F, false);

        this.feetLeftBase = new ModelRendererPlus(this);
        this.feetLeftBase.setDefaultRotPoint(4.0F, 4.75F, 0.0F);
        this.body.addChild(this.feetLeftBase);
        this.feetLeftBase.setTextureOffset(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3, 7, 5, 0.25F, false);

        this.feetRightBase = new ModelRendererPlus(this);
        this.feetRightBase.setDefaultRotPoint(-4.0F, 4.75F, 0.0F);
        this.body.addChild(this.feetRightBase);
        this.feetRightBase.setTextureOffset(42, 50).addBox(-1.5F, -5.5F, -2.5F, 3, 7, 5, 0.25F, true);
    }

    @Override
    public void setRotationAngles(T wooly, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bodyCenter.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRendererPlus modelRenderer, float x, float y, float z) {
        modelRenderer.setDefaultRotAngle(x, y, z);
    }

    private void sync(ModelRendererPlus model, ModelRendererPlus other) {
        model.rotateAngleX = other.rotateAngleX;
        model.rotateAngleY = other.rotateAngleY;
        model.rotateAngleZ = other.rotateAngleZ;
        model.rotationPointX = other.rotationPointX;
        model.rotationPointY = other.rotationPointY;
        model.rotationPointZ = other.rotationPointZ;
        model.scaleX = other.scaleX;
        model.scaleY = other.scaleY;
        model.scaleZ = other.scaleZ;
    }

    public void syncModel(ModelWooly<T> model) {
        this.sync(this.bodyCenter, model.bodyCenter);
        this.sync(this.body, model.body);
        this.sync(this.bodyUp, model.bodyUp);
        this.sync(this.armLeftBase, model.armLeftBase);
        this.sync(this.armRightBase, model.armRightBase);
        this.sync(this.feetLeftBase, model.feetLeftBase);
        this.sync(this.feetRightBase, model.feetRightBase);
    }
}