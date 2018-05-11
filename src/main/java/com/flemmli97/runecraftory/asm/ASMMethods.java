package com.flemmli97.runecraftory.asm;

import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayerAnim;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class ASMMethods {
	
	public static ItemStack renderDualWeapons(EntityLivingBase entity, ItemStack off)
	{
        boolean flag = entity.getPrimaryHand() == EnumHandSide.LEFT;
        ItemStack itemstack = flag ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        if (!itemstack.isEmpty() && (itemstack.getItem() instanceof DualBladeBase || itemstack.getItem() instanceof GloveBase))
        {
            return itemstack;
        }
        return off;
	}
	
	public static EntityLivingBase ignoreGloveModel(EntityLivingBase entity, ItemStack stack)
	{
		if(stack.getItem() instanceof GloveBase && entity instanceof EntityPlayer)
		{
			return null;
		}		
		return entity;
	}
	
	/**
	 * Sets up model properties for rendering
	 * @param player
	 * @param model
	 * @return
	 */
	public static ModelPlayer playerAnim(AbstractClientPlayer player, ModelPlayer model)
	{
		ItemStack heldMain = player.getHeldItemMainhand();
		boolean rightHand = player.getPrimaryHand() == EnumHandSide.RIGHT;
        
		if(heldMain.getItem() instanceof IRpUseItem)
		{
			ModelBiped.ArmPose off = rightHand?model.leftArmPose:model.rightArmPose;
			ModelBiped.ArmPose main = ModelBiped.ArmPose.ITEM;
	        if (heldMain.getItem() instanceof DualBladeBase || heldMain.getItem() instanceof GloveBase)
	        {
	    		off=ModelBiped.ArmPose.ITEM;
	        }
	        if(rightHand)
	        {
	        	model.rightArmPose = main;
	        	model.leftArmPose=off;
	        }
	        else
	        {
	        	model.leftArmPose = main;
	        	model.rightArmPose=off;
	        }
		}
        return model;
	}
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
	public static void modelBiped(ModelBiped model, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, 
			float headPitch, float scaleFactor, Entity entity)
	{
		if(model instanceof ModelPlayer)
		{
			ModelPlayer mopl = (ModelPlayer) model;
			IPlayerAnim anim = entity.getCapability(PlayerCapProvider.PlayerAnim, null);
			
			if(anim!=null)
				switch(anim.currentArmPose())
				{
					case CHARGECAN:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-45);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(35);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(-45);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-35);
						break;
					case CHARGEFISHING:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-105);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(20);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(-105);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-20);
						mopl.bipedBody.rotateAngleX = (float) Math.toRadians(-5);
						mopl.bipedHead.rotateAngleX = (float) Math.toRadians(-5);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(5);
						mopl.bipedRightLeg.rotateAngleZ += (float) Math.toRadians(5);
						mopl.bipedRightLeg.rotationPointZ=-1;
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(5);
						mopl.bipedLeftLeg.rotateAngleZ += (float) Math.toRadians(-5);
						mopl.bipedLeftLeg.rotationPointZ=-1;
						break;
					case CHARGEFIST:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-40);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(10);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(-40);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-10);
						mopl.bipedBody.rotateAngleX = (float) Math.toRadians(-5);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(15);
						mopl.bipedRightLeg.rotateAngleZ += (float) Math.toRadians(15);
						mopl.bipedRightLeg.rotationPointZ=1;
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(-15);
						mopl.bipedLeftLeg.rotateAngleZ += (float) Math.toRadians(-15);
						mopl.bipedLeftLeg.rotationPointZ=1;
						break;
					case CHARGELONG:
						break;
					case CHARGESICKLE:
						break;
					case CHARGESPEAR:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-40);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(70);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(30);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-5);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(10);
						mopl.bipedRightLeg.rotateAngleZ += (float) Math.toRadians(10);
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(-10);
						mopl.bipedLeftLeg.rotateAngleZ += (float) Math.toRadians(-10);
						break;
					case CHARGESWORD:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-40);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(30);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(5);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(20);
						mopl.bipedRightArm.rotateAngleZ=(float) Math.toRadians(30);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(25);
						mopl.bipedRightLeg.rotateAngleZ += (float) Math.toRadians(15);
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(-25);
						mopl.bipedLeftLeg.rotateAngleZ += (float) Math.toRadians(-15);
						break;
					case CHARGEUPTOOL:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-105);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(45);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(-105);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-5);
						mopl.bipedBody.rotateAngleX = (float) Math.toRadians(-5);
						mopl.bipedHead.rotateAngleX = (float) Math.toRadians(-5);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(10);
						mopl.bipedRightLeg.rotationPointZ=-1;
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(-10);
						mopl.bipedLeftLeg.rotationPointZ=-1;
						break;
					case CHARGEUPWEAPON:
						mopl.bipedLeftArm.rotateAngleX=(float) Math.toRadians(-115);
						mopl.bipedLeftArm.rotateAngleY=(float) Math.toRadians(45);
						mopl.bipedRightArm.rotateAngleX=(float) Math.toRadians(-115);
						mopl.bipedRightArm.rotateAngleY=(float) Math.toRadians(-5);
						mopl.bipedRightLeg.rotateAngleX += (float) Math.toRadians(15);
						mopl.bipedRightLeg.rotateAngleZ += (float) Math.toRadians(10);
						mopl.bipedLeftLeg.rotateAngleX += (float) Math.toRadians(-15);
						mopl.bipedLeftLeg.rotateAngleZ += (float) Math.toRadians(-10);
						break;
					default:break;
				}
		}
	}
}
