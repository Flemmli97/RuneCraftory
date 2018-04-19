package com.flemmli97.runecraftory.asm;

import com.flemmli97.runecraftory.common.items.weapons.DualBladeBase;
import com.flemmli97.runecraftory.common.items.weapons.GloveBase;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
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
	
	public static ModelPlayer playerAnim(AbstractClientPlayer player, ModelPlayer model)
	{
		ItemStack heldMain = player.getHeldItemMainhand();
        if (heldMain.getItem() instanceof DualBladeBase || heldMain.getItem() instanceof GloveBase)
        {
        		model.leftArmPose=ModelBiped.ArmPose.ITEM;
        		model.rightArmPose=ModelBiped.ArmPose.ITEM;
        }
        {
            //EnumAction enumaction = heldMain.getItemUseAction();
        }
        return model;
	}

}
