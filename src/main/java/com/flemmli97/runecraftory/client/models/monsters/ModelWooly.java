package com.flemmli97.runecraftory.client.models.monsters;

import com.flemmli97.runecraftory.common.entity.monster.EntityWooly;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.client.model.TabulaAnimation;
import com.flemmli97.tenshilib.client.model.IResetModel;
import com.flemmli97.tenshilib.client.model.ModelRendererPlus;
import com.flemmli97.tenshilib.common.javahelper.MathUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

/**
 * woolyNew - Undefined
 * Created using Tabula 7.0.0
 */
public class ModelWooly extends ModelBase implements IResetModel{
    public ModelRendererPlus body;
    public ModelRendererPlus bodyLeft;
    public ModelRendererPlus bodyRight;
    public ModelRendererPlus bodyUp;
    public ModelRendererPlus armLeftBase;
    public ModelRendererPlus armRightBase;
    public ModelRendererPlus feetLeftBase;
    public ModelRendererPlus feetRightBase;
    public ModelRendererPlus tail;
    public ModelRendererPlus bodyUpLeft;
    public ModelRendererPlus bodyUpRight;
    public ModelRendererPlus neck;
    public ModelRendererPlus head;
    public ModelRendererPlus headLeft;
    public ModelRendererPlus headRight;
    public ModelRendererPlus headUp;
    public ModelRendererPlus earLeft;
    public ModelRendererPlus earRight;
    public ModelRendererPlus armLeftUp;
    public ModelRendererPlus leftArmDown;
    public ModelRendererPlus armRightUp;
    public ModelRendererPlus armRightDown;
    public ModelRendererPlus feetLeft;
    public ModelRendererPlus feetRight;

    //20
    public TabulaAnimation walk;
    
    public ModelWooly() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.armRightUp = new ModelRendererPlus(this, 28, 45);
        this.armRightUp.mirror = true;
        this.armRightUp.setDefaultRotPoint(-1.0F, 0.0F, -0.5F);
        this.armRightUp.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.armRightUp, 0.2617993877991494F, -0.2617993877991494F, 0.0F);
        this.body = new ModelRendererPlus(this, 0, 0);
        this.body.setDefaultRotPoint(0.0F, 17.75F, 0.0F);
        this.body.addBox(-3.0F, -6.0F, -4.0F, 6, 12, 8, 0.0F);
        this.headRight = new ModelRendererPlus(this, 28, 16);
        this.headRight.mirror = true;
        this.headRight.setDefaultRotPoint(-3.5F, -3.5F, 0.0F);
        this.headRight.addBox(-1.0F, -3.5F, -3.5F, 1, 7, 7, 0.0F);
        this.earRight = new ModelRendererPlus(this, 28, 38);
        this.earRight.mirror = true;
        this.earRight.setDefaultRotPoint(3.5F, -4.0F, 0.0F);
        this.earRight.addBox(0.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F);
        this.setRotateAngle(this.earRight, 0.0F, 0.0F, 0.3490658503988659F);
        this.feetRightBase = new ModelRendererPlus(this, 42, 38);
        this.feetRightBase.mirror = true;
        this.feetRightBase.setDefaultRotPoint(-3.0F, 4.75F, 0.0F);
        this.feetRightBase.addBox(-1.5F, -3.5F, -2.0F, 3, 5, 4, 0.0F);
        this.head = new ModelRendererPlus(this, 28, 0);
        this.head.setDefaultRotPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-3.5F, -7.0F, -4.5F, 7, 7, 9, 0.0F);
        this.earLeft = new ModelRendererPlus(this, 28, 38);
        this.earLeft.setDefaultRotPoint(-4.0F, -4.0F, 0.0F);
        this.earLeft.addBox(-5.0F, -0.5F, -1.0F, 5, 1, 2, 0.0F);
        this.setRotateAngle(this.earLeft, 0.0F, 0.0F, -0.3490658503988659F);
        this.armLeftBase = new ModelRendererPlus(this, 28, 41);
        this.armLeftBase.setDefaultRotPoint(3.75F, -3.0F, 0.0F);
        this.armLeftBase.addBox(0.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(this.armLeftBase, 0.17453292519943295F, 0.0F, 0.0F);
        this.armRightDown = new ModelRendererPlus(this, 28, 49);
        this.armRightDown.mirror = true;
        this.armRightDown.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
        this.armRightDown.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.armRightDown, 0.08726646259971647F, -0.5235987755982988F, 0.0F);
        this.feetLeftBase = new ModelRendererPlus(this, 42, 38);
        this.feetLeftBase.setDefaultRotPoint(3.0F, 4.75F, 0.0F);
        this.feetLeftBase.addBox(-1.5F, -3.5F, -2.0F, 3, 5, 4, 0.0F);
        this.bodyUp = new ModelRendererPlus(this, 0, 38);
        this.bodyUp.setDefaultRotPoint(0.0F, -7.0F, 0.0F);
        this.bodyUp.addBox(-2.5F, -1.0F, -3.5F, 5, 2, 7, 0.0F);
        this.headLeft = new ModelRendererPlus(this, 28, 16);
        this.headLeft.setDefaultRotPoint(3.5F, -3.5F, 0.0F);
        this.headLeft.addBox(0.0F, -3.5F, -3.0F, 1, 7, 7, 0.0F);
        this.headUp = new ModelRendererPlus(this, 28, 30);
        this.headUp.setDefaultRotPoint(0.0F, -7.0F, 0.0F);
        this.headUp.addBox(-3.5F, -1.0F, -3.5F, 7, 1, 7, 0.0F);
        this.bodyUpRight = new ModelRendererPlus(this, 0, 47);
        this.bodyUpRight.mirror = true;
        this.bodyUpRight.setDefaultRotPoint(-2.5F, 0.0F, 0.0F);
        this.bodyUpRight.addBox(-1.0F, -1.0F, -2.5F, 1, 2, 5, 0.0F);
        this.setRotateAngle(this.bodyUpRight, 0.0F, 0.0F, 0.012042771838760872F);
        this.leftArmDown = new ModelRendererPlus(this, 28, 49);
        this.leftArmDown.setDefaultRotPoint(0.0F, 0.0F, -3.0F);
        this.leftArmDown.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.leftArmDown, 0.08726646259971647F, 0.5235987755982988F, 0.0F);
        this.bodyRight = new ModelRendererPlus(this, 0, 20);
        this.bodyRight.mirror = true;
        this.bodyRight.setDefaultRotPoint(-3.0F, 0.0F, 0.0F);
        this.bodyRight.addBox(-1.0F, -6.0F, -3.0F, 1, 12, 6, 0.0F);
        this.setRotateAngle(this.bodyRight, 0.0F, 0.0F, 0.012042771838760872F);
        this.armRightBase = new ModelRendererPlus(this, 28, 41);
        this.armRightBase.mirror = true;
        this.armRightBase.setDefaultRotPoint(-3.75F, -3.0F, 0.0F);
        this.armRightBase.addBox(-2.0F, -1.0F, -1.0F, 2, 2, 2, 0.0F);
        this.setRotateAngle(this.armRightBase, 0.17453292519943295F, 0.0F, 0.0F);
        this.bodyLeft = new ModelRendererPlus(this, 0, 20);
        this.bodyLeft.setDefaultRotPoint(3.0F, 0.0F, 0.0F);
        this.bodyLeft.addBox(0.0F, -6.0F, -3.0F, 1, 12, 6, 0.0F);
        this.setRotateAngle(this.bodyLeft, 0.0F, 0.0F, 0.012042771838760872F);
        this.armLeftUp = new ModelRendererPlus(this, 28, 45);
        this.armLeftUp.setDefaultRotPoint(1.0F, 0.0F, -0.5F);
        this.armLeftUp.addBox(-0.5F, -0.5F, -3.0F, 1, 1, 3, 0.0F);
        this.setRotateAngle(this.armLeftUp, 0.2617993877991494F, 0.2617993877991494F, 0.0F);
        this.tail = new ModelRendererPlus(this, 44, 16);
        this.tail.setDefaultRotPoint(0.0F, 3.0F, 4.0F);
        this.tail.addBox(-1.5F, -1.5F, 0.0F, 3, 3, 3, 0.0F);
        this.feetRight = new ModelRendererPlus(this, 42, 47);
        this.feetRight.mirror = true;
        this.feetRight.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
        this.feetRight.addBox(-1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F);
        this.neck = new ModelRendererPlus(this, 0, 54);
        this.neck.setDefaultRotPoint(0.0F, -2.5F, 0.0F);
        this.neck.addBox(-2.5F, 0.0F, -2.5F, 5, 2, 5, 0.0F);
        this.bodyUpLeft = new ModelRendererPlus(this, 0, 47);
        this.bodyUpLeft.setDefaultRotPoint(2.5F, 0.0F, 0.0F);
        this.bodyUpLeft.addBox(0.0F, -1.0F, -2.5F, 1, 2, 5, 0.0F);
        this.setRotateAngle(this.bodyUpLeft, 0.0F, 0.0F, 0.012042771838760872F);
        this.feetLeft = new ModelRendererPlus(this, 42, 47);
        this.feetLeft.setDefaultRotPoint(0.0F, 0.5F, 0.0F);
        this.feetLeft.addBox(-1.0F, 0.0F, -7.0F, 2, 1, 7, 0.0F);
        this.armRightBase.addChild(this.armRightUp);
        this.head.addChild(this.headRight);
        this.head.addChild(this.earRight);
        this.body.addChild(this.feetRightBase);
        this.neck.addChild(this.head);
        this.head.addChild(this.earLeft);
        this.body.addChild(this.armLeftBase);
        this.armRightUp.addChild(this.armRightDown);
        this.body.addChild(this.feetLeftBase);
        this.body.addChild(this.bodyUp);
        this.head.addChild(this.headLeft);
        this.head.addChild(this.headUp);
        this.bodyUp.addChild(this.bodyUpRight);
        this.armLeftUp.addChild(this.leftArmDown);
        this.body.addChild(this.bodyRight);
        this.body.addChild(this.armRightBase);
        this.body.addChild(this.bodyLeft);
        this.armLeftBase.addChild(this.armLeftUp);
        this.body.addChild(this.tail);
        this.feetRightBase.addChild(this.feetRight);
        this.bodyUp.addChild(this.neck);
        this.bodyUp.addChild(this.bodyUpLeft);
        this.feetLeftBase.addChild(this.feetLeft);
        
        this.walk = new TabulaAnimation(this, new ResourceLocation(LibReference.MODID, "models/entity/animation/wooly_walk.json"), false);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.body.render(f5);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scaleFactor, Entity entity) {
        if(!(entity instanceof EntityWooly))
            return;
        this.resetModel();
        EntityWooly wooly = (EntityWooly) entity;
        this.head.rotateAngleY = netHeadYaw * 0.008453292F;
        this.head.rotateAngleX = headPitch * 0.012453292F;
        this.earLeft.rotateAngleZ+=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);
        this.earRight.rotateAngleZ-=Math.abs(MathHelper.sin(ageInTicks*0.04F)*0.1735987755982989F);
            
        //this.armLeftUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 0.4F * limbSwingAmount ;
        //this.armRightUp.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 0.4F * limbSwingAmount;
            
        this.tail.rotateAngleY+= MathHelper.cos(ageInTicks * 0.6662F) * MathUtils.degToRad(10);
        this.armRightUp.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armLeftUp.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
        this.armRightUp.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        this.armLeftUp.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
        
        //this.feetRight.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.0F * limbSwingAmount;
        //this.feetLeft.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.0F * limbSwingAmount;
        
        float partialTicks = Minecraft.getMinecraft().getRenderPartialTicks();
        if(wooly.isMoving()) {
            this.resetModel();
            this.body.rotateAngleX=MathUtils.degToRad(20);
            this.walk.animate(wooly.ticksExisted, partialTicks);
        }
        /*AnimatedAction anim = wooly.getAnimation();
        if(anim!=null)
        {
            if(anim.getID().equals("walk")) {
                this.resetModel();
                this.body.rotateAngleX=MathUtils.degToRad(20);
                this.walk.animate(anim.getTick(), partialTicks);
            }
        }*/
    }
    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRendererPlus modelRendererPlus, float x, float y, float z) {
        modelRendererPlus.setDefaultRotAngle(x, y, z);
    }

    @Override
    public void resetModel() {
        this.body.reset();
        this.resetChild(this.body);
    }
}
