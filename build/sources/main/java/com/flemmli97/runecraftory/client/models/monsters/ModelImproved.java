package com.flemmli97.runecraftory.client.models.monsters;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public abstract class ModelImproved extends ModelBase {

	/**
	 * Every modelpart with added rotation in setRotationAngles should be reseted here
	 */
	public abstract void startingPosition();
	/**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    public static float toRadians(float angdeg) {
        return (float) (angdeg / 180.0 * Math.PI);
    }
}
