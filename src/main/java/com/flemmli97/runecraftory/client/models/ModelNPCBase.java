package com.flemmli97.runecraftory.client.models;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class ModelNPCBase extends ModelBiped
{
    public ModelRenderer npcHead;
    public ModelRenderer npcHeadOverlay;
    public ModelRenderer npcBody;
    public ModelRenderer npcBodyOverlay;
    public ModelRenderer npcRightArmUp;
    public ModelRenderer npcRightArmUpOverlay;
    public ModelRenderer npcRightArmJoint;
    public ModelRenderer npcRightArmOverlayJoint;
    public ModelRenderer npcRightArmDown;
    public ModelRenderer npcRightArmDownOverlay;
    public ModelRenderer npcLeftArmUp;
    public ModelRenderer npcLeftArmUpOverlay;
    public ModelRenderer npcLeftArmJoint;
    public ModelRenderer npcLeftArmOverlayJoint;
    public ModelRenderer npcLeftArmDown;
    public ModelRenderer npcLeftArmDownOverlay;
    public ModelRenderer npcRightLegUp;
    public ModelRenderer npcRightLegUpOverlay;
    public ModelRenderer npcRightLegDown;
    public ModelRenderer npcRightLegDownOverlay;
    public ModelRenderer npcLeftLegUp;
    public ModelRenderer npcLeftLegUpOverlay;
    public ModelRenderer npcLeftLegDown;
    public ModelRenderer npcLeftLegDownOverlay;

    public int heldItemMain;
    public int heldItemOff;
    
    public ModelNPCBase() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        (this.npcHead = new ModelRenderer((ModelBase)this, 0, 0)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.0f);
        this.npcHead.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.npcHeadOverlay = new ModelRenderer((ModelBase)this, 32, 0)).addBox(-4.0f, -8.0f, -4.0f, 8, 8, 8, 0.5f);
        this.npcHeadOverlay.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.npcBody = new ModelRenderer((ModelBase)this, 16, 16)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, 0.0f);
        this.npcBody.setRotationPoint(0.0f, 0.0f, 0.0f);
        (this.npcBodyOverlay = new ModelRenderer((ModelBase)this, 16, 32)).addBox(-4.0f, 0.0f, -2.0f, 8, 12, 4, 0.25f);
        this.npcBodyOverlay.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.npcLeftArmUp = new ModelRenderer((ModelBase)this, 40, 16);
        this.npcLeftArmUp.mirror = true;
        this.npcLeftArmUp.addBox(-1.0f, -2.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcLeftArmUp.setRotationPoint(5.0f, 2.0f, 0.0f);
        this.npcLeftArmUpOverlay = new ModelRenderer((ModelBase)this, 40, 32);
        this.npcLeftArmUpOverlay.mirror = true;
        this.npcLeftArmUpOverlay.addBox(-1.0f, -2.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcLeftArmUpOverlay.setRotationPoint(5.0f, 2.0f, 0.0f);
        (this.npcLeftArmJoint = new ModelRenderer((ModelBase)this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.npcLeftArmJoint.setRotationPoint(3.0f, 4.0f, 0.0f);
        this.npcLeftArmUp.addChild(this.npcLeftArmJoint);
        (this.npcLeftArmOverlayJoint = new ModelRenderer((ModelBase)this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.npcLeftArmOverlayJoint.setRotationPoint(3.0f, 4.0f, 0.0f);
        this.npcLeftArmUpOverlay.addChild(this.npcLeftArmOverlayJoint);
        this.npcLeftArmDown = new ModelRenderer((ModelBase)this, 32, 54);
        this.npcLeftArmDown.mirror = true;
        this.npcLeftArmDown.addBox(-4.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcLeftArmDown.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.npcLeftArmJoint.addChild(this.npcLeftArmDown);
        this.npcLeftArmDownOverlay = new ModelRenderer((ModelBase)this, 48, 54);
        this.npcLeftArmDownOverlay.mirror = true;
        this.npcLeftArmDownOverlay.addBox(-4.0f, 0.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcLeftArmDownOverlay.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.npcLeftArmOverlayJoint.addChild(this.npcLeftArmDownOverlay);
        (this.npcRightArmUp = new ModelRenderer((ModelBase)this, 40, 16)).addBox(-3.0f, -2.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcRightArmUp.setRotationPoint(-5.0f, 2.0f, 0.0f);
        (this.npcRightArmUpOverlay = new ModelRenderer((ModelBase)this, 40, 32)).addBox(-3.0f, -2.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcRightArmUpOverlay.setRotationPoint(-5.0f, 2.0f, 0.0f);
        (this.npcRightArmJoint = new ModelRenderer((ModelBase)this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.npcRightArmJoint.setRotationPoint(-3.0f, 4.0f, 0.0f);
        this.npcRightArmUp.addChild(this.npcRightArmJoint);
        (this.npcRightArmOverlayJoint = new ModelRenderer((ModelBase)this, 0, 0)).addBox(0.0f, 0.0f, 0.0f, 0, 0, 0);
        this.npcRightArmOverlayJoint.setRotationPoint(-3.0f, 4.0f, 0.0f);
        this.npcRightArmUpOverlay.addChild(this.npcRightArmOverlayJoint);
        (this.npcRightArmDown = new ModelRenderer((ModelBase)this, 32, 54)).addBox(0.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcRightArmDown.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.npcRightArmJoint.addChild(this.npcRightArmDown);
        (this.npcRightArmDownOverlay = new ModelRenderer((ModelBase)this, 48, 54)).addBox(0.0f, 0.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcRightArmDownOverlay.setRotationPoint(0.0f, 0.0f, 0.0f);
        this.npcRightArmOverlayJoint.addChild(this.npcRightArmDownOverlay);
        this.npcLeftLegUp = new ModelRenderer((ModelBase)this, 0, 16);
        this.npcLeftLegUp.mirror = true;
        this.npcLeftLegUp.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcLeftLegUp.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.npcLeftLegUpOverlay = new ModelRenderer((ModelBase)this, 0, 32);
        this.npcLeftLegUpOverlay.mirror = true;
        this.npcLeftLegUpOverlay.addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcLeftLegUpOverlay.setRotationPoint(1.9f, 12.0f, 0.0f);
        this.npcLeftLegDown = new ModelRenderer((ModelBase)this, 16, 54);
        this.npcLeftLegDown.mirror = true;
        this.npcLeftLegDown.addBox(-2.0f, 0.0f, 0.0f, 4, 6, 4, 0.0f);
        this.npcLeftLegDown.setRotationPoint(0.0f, 6.0f, -2.0f);
        this.npcLeftLegUp.addChild(this.npcLeftLegDown);
        this.npcLeftLegDownOverlay = new ModelRenderer((ModelBase)this, 0, 54);
        this.npcLeftLegDownOverlay.mirror = true;
        this.npcLeftLegDownOverlay.addBox(-2.0f, 0.0f, 0.0f, 4, 6, 4, 0.25f);
        this.npcLeftLegDownOverlay.setRotationPoint(0.0f, 6.0f, -2.0f);
        this.npcLeftLegUpOverlay.addChild(this.npcLeftLegDownOverlay);
        (this.npcRightLegUp = new ModelRenderer((ModelBase)this, 0, 16)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.0f);
        this.npcRightLegUp.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.npcRightLegUpOverlay = new ModelRenderer((ModelBase)this, 0, 32)).addBox(-2.0f, 0.0f, -2.0f, 4, 6, 4, 0.25f);
        this.npcRightLegUpOverlay.setRotationPoint(-1.9f, 12.0f, 0.0f);
        (this.npcRightLegDown = new ModelRenderer((ModelBase)this, 16, 54)).addBox(-2.0f, 0.0f, 0.0f, 4, 6, 4, 0.0f);
        this.npcRightLegDown.setRotationPoint(0.0f, 6.0f, -2.0f);
        this.npcRightLegUp.addChild(this.npcRightLegDown);
        (this.npcRightLegDownOverlay = new ModelRenderer((ModelBase)this, 0, 54)).addBox(-2.0f, 0.0f, 0.0f, 4, 6, 4, 0.25f);
        this.npcRightLegDownOverlay.setRotationPoint(0.0f, 6.0f, -2.0f);
        this.npcRightLegUpOverlay.addChild(this.npcRightLegDownOverlay);
        this.npcBody.addChild(this.npcHead);
        this.npcBody.addChild(this.npcRightArmUp);
        this.npcBody.addChild(this.npcLeftArmUp);
        this.npcBody.addChild(this.npcRightLegUp);
        this.npcBody.addChild(this.npcLeftLegUp);
        this.npcBodyOverlay.addChild(this.npcHeadOverlay);
        this.npcBodyOverlay.addChild(this.npcRightArmUpOverlay);
        this.npcBodyOverlay.addChild(this.npcLeftArmUpOverlay);
        this.npcBodyOverlay.addChild(this.npcRightLegUpOverlay);
        this.npcBodyOverlay.addChild(this.npcLeftLegUpOverlay);
    }
    
    public void render(final Entity entity, final float f0, final float f1, final float f2, final float f3, final float f4, final float scale) {
        this.setRotationAngles(f0, f1, f2, f3, f4, scale, entity);
        this.npcBody.render(scale);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        this.npcBodyOverlay.render(scale);
        GL11.glDisable(3042);
    }
    
    public void setRotationAngles(final float f1, final float f2, final float f3, final float f4, final float f5, final float f6, final Entity entity) {
        this.setRotationAnglesPre(f1, f2, f3, f4, f5, f6, entity);
        this.syncOverlay();
    }
    
    public void setRotationAnglesPre(final float armSwingTime, final float armSwingReach, final float partialTick, final float f4, final float f5, final float scale, final Entity entity) {
        this.npcHead.rotateAngleY = f4 / 57.295776f;
        this.npcHead.rotateAngleX = f5 / 57.295776f;
        this.npcHeadOverlay.rotateAngleY = this.npcHead.rotateAngleY;
        this.npcHeadOverlay.rotateAngleX = this.npcHead.rotateAngleX;
        this.npcRightArmUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662f + 3.1415927f) * 2.0f * armSwingReach * 0.5f;
        this.npcLeftArmUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662f) * 2.0f * armSwingReach * 0.5f;
        this.npcRightArmUp.rotateAngleZ = 0.0f;
        this.npcLeftArmUp.rotateAngleZ = 0.0f;
        this.npcRightLegUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662f) * 1.4f * armSwingReach;
        this.npcLeftLegUp.rotateAngleX = MathHelper.cos(armSwingTime * 0.6662f + 3.1415927f) * 1.4f * armSwingReach;
        this.npcRightLegUp.rotateAngleY = 0.0f;
        this.npcLeftLegUp.rotateAngleY = 0.0f;
        if (this.isRiding) {
            final ModelRenderer npcRightArmUp = this.npcRightArmUp;
            npcRightArmUp.rotateAngleX -= 0.62831855f;
            final ModelRenderer npcLeftArmUp = this.npcLeftArmUp;
            npcLeftArmUp.rotateAngleX -= 0.62831855f;
            this.npcRightLegUp.rotateAngleX = -1.2566371f;
            this.npcLeftLegUp.rotateAngleX = -1.2566371f;
            this.npcRightLegUp.rotateAngleY = 0.31415927f;
            this.npcLeftLegUp.rotateAngleY = -0.31415927f;
        }
        if (this.heldItemOff == 1) {
            this.npcLeftArmUp.rotateAngleX = this.npcLeftArmUp.rotateAngleX * 0.5f - 0.31415927f;
        }
        if (this.heldItemMain == 1) {
            this.npcRightArmUp.rotateAngleX = this.npcRightArmUp.rotateAngleX * 0.5f - 0.31415927f;
        }
        this.npcRightArmUp.rotateAngleY = 0.0f;
        this.npcLeftArmUp.rotateAngleY = 0.0f;
        if (this.swingProgress > -9990.0f) {
            float var8 = this.swingProgress;
            this.npcBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(var8) * 3.1415927f * 2.0f) * 0.2f;
            this.npcRightArmUp.rotationPointZ = MathHelper.sin(this.npcBody.rotateAngleY) * 5.0f;
            this.npcRightArmUp.rotationPointX = -MathHelper.cos(this.npcBody.rotateAngleY) * 5.0f;
            this.npcLeftArmUp.rotationPointZ = -MathHelper.sin(this.npcBody.rotateAngleY) * 5.0f;
            this.npcLeftArmUp.rotationPointX = MathHelper.cos(this.npcBody.rotateAngleY) * 5.0f;
            final ModelRenderer npcRightArmUp2 = this.npcRightArmUp;
            npcRightArmUp2.rotateAngleY += this.npcBody.rotateAngleY;
            final ModelRenderer npcLeftArmUp2 = this.npcLeftArmUp;
            npcLeftArmUp2.rotateAngleY += this.npcBody.rotateAngleY;
            final ModelRenderer npcLeftArmUp3 = this.npcLeftArmUp;
            npcLeftArmUp3.rotateAngleX += this.npcBody.rotateAngleY;
            var8 = 1.0f - this.swingProgress;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0f - var8;
            final float var9 = MathHelper.sin(var8 * 3.1415927f);
            final float var10 = MathHelper.sin(this.swingProgress * 3.1415927f) * -(this.npcHead.rotateAngleX - 0.7f) * 0.75f;
            this.npcRightArmUp.rotateAngleX -= (float)(var9 * 1.2 + var10);
            final ModelRenderer npcRightArmUp3 = this.npcRightArmUp;
            npcRightArmUp3.rotateAngleY += this.npcBody.rotateAngleY * 2.0f;
            this.npcRightArmUp.rotateAngleZ = MathHelper.sin(this.swingProgress * 3.1415927f) * -0.4f;
        }
        this.npcBody.rotateAngleX = 0.0f;
        this.npcRightLegUp.rotationPointZ = 0.1f;
        this.npcLeftLegUp.rotationPointZ = 0.1f;
        this.npcRightLegUp.rotationPointY = 12.0f;
        this.npcLeftLegUp.rotationPointY = 12.0f;
        this.npcHead.rotationPointY = 0.0f;
        this.npcHeadOverlay.rotationPointY = 0.0f;
        final ModelRenderer npcRightArmUp4 = this.npcRightArmUp;
        npcRightArmUp4.rotateAngleZ += MathHelper.cos(partialTick * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer npcLeftArmUp4 = this.npcLeftArmUp;
        npcLeftArmUp4.rotateAngleZ -= MathHelper.cos(partialTick * 0.09f) * 0.05f + 0.05f;
        final ModelRenderer npcRightArmUp5 = this.npcRightArmUp;
        npcRightArmUp5.rotateAngleX += MathHelper.sin(partialTick * 0.067f) * 0.05f;
        final ModelRenderer npcLeftArmUp5 = this.npcLeftArmUp;
        npcLeftArmUp5.rotateAngleX -= MathHelper.sin(partialTick * 0.067f) * 0.05f;
    }
    
    public void syncOverlay() {
        copyModelAngles(this.npcLeftLegUp, this.npcLeftLegUpOverlay);
        copyModelAngles(this.npcRightLegUp, this.npcRightLegUpOverlay);
        copyModelAngles(this.npcLeftArmUp, this.npcLeftArmUpOverlay);
        copyModelAngles(this.npcRightArmUp, this.npcRightArmUpOverlay);
        copyModelAngles(this.npcRightArmJoint, this.npcRightArmOverlayJoint);
        copyModelAngles(this.npcLeftArmJoint, this.npcLeftArmOverlayJoint);
        copyModelAngles(this.npcRightArmDown, this.npcRightArmDownOverlay);
        copyModelAngles(this.npcLeftArmDown, this.npcLeftArmDownOverlay);
        copyModelAngles(this.npcLeftLegDown, this.npcLeftLegDownOverlay);
        copyModelAngles(this.npcRightLegDown, this.npcRightLegDownOverlay);
        copyModelAngles(this.npcHead, this.npcHeadOverlay);
        copyModelAngles(this.npcBody, this.npcBodyOverlay);
    }
    
    public float degToRad(final float degree) {
        final float radiant = degree / 57.295776f;
        return radiant;
    }
    
    public void resetModel() {
        this.npcRightArmJoint.rotateAngleX = this.degToRad(0.0f);
        this.npcLeftArmJoint.rotateAngleX = this.degToRad(0.0f);
        this.npcRightArmJoint.rotateAngleY = this.degToRad(0.0f);
        this.npcLeftArmJoint.rotateAngleY = this.degToRad(0.0f);
        this.npcRightArmJoint.rotateAngleZ = this.degToRad(0.0f);
        this.npcLeftArmJoint.rotateAngleZ = this.degToRad(0.0f);
        this.npcRightArmDown.rotateAngleX = this.degToRad(0.0f);
        this.npcLeftArmDown.rotateAngleX = this.degToRad(0.0f);
        this.npcRightArmDown.rotateAngleY = this.degToRad(0.0f);
        this.npcLeftArmDown.rotateAngleY = this.degToRad(0.0f);
        this.npcRightArmDown.rotateAngleZ = this.degToRad(0.0f);
        this.npcLeftArmDown.rotateAngleZ = this.degToRad(0.0f);
        this.npcRightLegDown.rotateAngleX = this.degToRad(0.0f);
        this.npcLeftLegDown.rotateAngleX = this.degToRad(0.0f);
        this.npcRightLegDown.rotateAngleY = this.degToRad(0.0f);
        this.npcLeftLegDown.rotateAngleY = this.degToRad(0.0f);
        this.npcRightLegDown.rotateAngleZ = this.degToRad(0.0f);
        this.npcLeftLegDown.rotateAngleZ = this.degToRad(0.0f);
        this.syncOverlay();
    }
    
    public void setLivingAnimations(final EntityLivingBase livingBase, final float armSwingTime, final float armSwingReach, final float partialTick) {
        super.setLivingAnimations(livingBase, armSwingTime, armSwingReach, partialTick);
    }
    
    public void postRenderArm(final float scale, final EnumHandSide side) {
        if (side == EnumHandSide.LEFT) {
            this.npcLeftArmUp.postRender(scale);
            this.npcLeftArmJoint.postRender(0.0f);
            this.npcLeftArmDown.postRender(scale);
        }
        else if (side == EnumHandSide.RIGHT) {
            this.npcRightArmUp.postRender(scale);
            this.npcRightArmJoint.postRender(0.0f);
            this.npcRightArmDown.postRender(scale);
        }
    }
}
