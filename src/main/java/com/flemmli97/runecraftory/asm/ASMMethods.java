package com.flemmli97.runecraftory.asm;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ASMMethods
{   
    /**
	 * Sets up the rotations based on the properties; modify x and y to keep living animation
	 * @param model
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ageInTicks
	 * @param netHeadYaw
	 * @param headPitch
	 * @param scaleFactor
	 * @param entity
	 */
    public static void modelBiped(ModelBiped model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        if (model instanceof ModelPlayer) 
        {
            ModelPlayer mopl = (ModelPlayer)model;
            IPlayer cap = entity.getCapability(PlayerCapProvider.PlayerCap, null);
            switch (cap.currentArmPose()) 
            {
                case CHARGECAN:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-45.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(35.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(-45.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-35.0);
                    break;
                case CHARGEFISHING:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-105.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(20.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(-105.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-20.0);
                    mopl.bipedBody.rotateAngleX = (float)Math.toRadians(-5.0);
                    mopl.bipedHead.rotateAngleX = (float)Math.toRadians(-5.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(5.0);
                    mopl.bipedRightLeg.rotateAngleZ += (float)Math.toRadians(5.0);
                    mopl.bipedRightLeg.rotationPointZ = -1.0f;
                    mopl.bipedLeftLeg.rotateAngleX += (float)Math.toRadians(5.0);
                    mopl.bipedLeftLeg.rotateAngleZ += (float)Math.toRadians(-5.0);
                    mopl.bipedLeftLeg.rotationPointZ = -1.0f;
                    break;
                case CHARGEFIST:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-40.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(10.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(-40.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-10.0);
                    mopl.bipedBody.rotateAngleX = (float)Math.toRadians(-5.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(15.0);
                    mopl.bipedRightLeg.rotateAngleZ += (float)Math.toRadians(15.0);
                    mopl.bipedRightLeg.rotationPointZ = 1.0f;
                    ModelRenderer bipedLeftLeg3 = mopl.bipedLeftLeg;
                    bipedLeftLeg3.rotateAngleX += (float)Math.toRadians(-15.0);
                    ModelRenderer bipedLeftLeg4 = mopl.bipedLeftLeg;
                    bipedLeftLeg4.rotateAngleZ += (float)Math.toRadians(-15.0);
                    mopl.bipedLeftLeg.rotationPointZ = 1.0f;
                    break;
                case CHARGELONG: 
                	break;
                case CHARGESPEAR:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-40.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(70.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(30.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-5.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(10.0);
                    mopl.bipedRightLeg.rotateAngleZ += (float)Math.toRadians(10.0);
                    mopl.bipedLeftLeg.rotateAngleX += (float)Math.toRadians(-10.0);
                    mopl.bipedLeftLeg.rotateAngleZ += (float)Math.toRadians(-10.0);
                    break;
                case CHARGESWORD:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-40.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(30.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(5.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(20.0);
                    mopl.bipedRightArm.rotateAngleZ = (float)Math.toRadians(30.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(25.0);
                    mopl.bipedRightLeg.rotateAngleZ += (float)Math.toRadians(15.0);
                    mopl.bipedLeftLeg.rotateAngleX += (float)Math.toRadians(-25.0);
                    mopl.bipedLeftLeg.rotateAngleZ += (float)Math.toRadians(-15.0);
                    break;
                case CHARGEUPTOOL:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-105.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(45.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(-105.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-5.0);
                    mopl.bipedBody.rotateAngleX = (float)Math.toRadians(-5.0);
                    mopl.bipedHead.rotateAngleX = (float)Math.toRadians(-5.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(10.0);
                    mopl.bipedRightLeg.rotationPointZ = -1.0f;
                    mopl.bipedLeftLeg.rotateAngleX += (float)Math.toRadians(-10.0);
                    mopl.bipedLeftLeg.rotationPointZ = -1.0f;
                    break;
                case CHARGEUPWEAPON:
                    mopl.bipedLeftArm.rotateAngleX = (float)Math.toRadians(-115.0);
                    mopl.bipedLeftArm.rotateAngleY = (float)Math.toRadians(45.0);
                    mopl.bipedRightArm.rotateAngleX = (float)Math.toRadians(-115.0);
                    mopl.bipedRightArm.rotateAngleY = (float)Math.toRadians(-5.0);
                    mopl.bipedRightLeg.rotateAngleX += (float)Math.toRadians(15.0);
                    mopl.bipedRightLeg.rotateAngleZ += (float)Math.toRadians(10.0);
                    mopl.bipedLeftLeg.rotateAngleX += (float)Math.toRadians(-15.0);
                    mopl.bipedLeftLeg.rotateAngleZ += (float)Math.toRadians(-10.0);
                    break;
				case CHARGESICKLE:
					break;
				case DEFAULT:
					break;
            }
        }
    }
}
