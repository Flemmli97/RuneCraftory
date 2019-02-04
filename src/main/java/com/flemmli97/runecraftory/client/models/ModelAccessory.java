package com.flemmli97.runecraftory.client.models;

import com.flemmli97.runecraftory.common.items.equipment.ItemAccessoireBase;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

public class ModelAccessory extends ModelBiped{

	public static final ModelAccessory model = new ModelAccessory(0.5f);
	private ItemAccessoireBase item;

    private ModelAccessory(float modelSize)
    {
        super(modelSize, 0.0F, 64, 32);
    }
    
    public ModelAccessory setItem(ItemAccessoireBase item)
    {
    	this.item=item;
    	return this;
    }
    
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if(this.item!=null)
    	{
    		if(this.item.customModel()!=null)
    		{
    			this.item.customModel().render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    			return;
    		}
    		this.setModelSlotVisible(this, this.item.modelType());
    	}
        super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
    }
    
    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
    	if(this.item!=null && this.item.customModel()!=null)
		{
			this.item.customModel().setModelAttributes(this);
			this.item.customModel().setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		}
    	else
    		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
    }

    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
    	if(this.item!=null && this.item.customModel()!=null)
		{
			this.item.customModel().setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
		}
    	else
    		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }
    
    @SuppressWarnings("incomplete-switch")
	protected void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slot)
    {
        this.setVisible(false);
        switch (slot)
        {
            case HEAD:
                model.bipedHead.showModel = true;
                model.bipedHeadwear.showModel = true;
                break;
            case CHEST:
                model.bipedBody.showModel = true;
                model.bipedRightArm.showModel = true;
                model.bipedLeftArm.showModel = true;
                break;
            case LEGS:
                model.bipedBody.showModel = true;
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
                break;
            case FEET:
                model.bipedRightLeg.showModel = true;
                model.bipedLeftLeg.showModel = true;
        }
    }
}
