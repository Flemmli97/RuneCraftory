package com.flemmli97.runecraftory.common.core.handler.capabilities;

import com.flemmli97.runecraftory.client.render.ArmPosePlus;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;

public interface IPlayerAnim {
	
	public int animationTick();
	
	public void startAnimation(int tick);
	
	public void update(EntityPlayer player);
	
	public boolean canUseSpear();
	
	public void startSpear();
	
	public int getSpearTick();
	
	public void disableOffHand();
	
	public boolean canUseOffHand();
		
	public EnumHand getPrevSwung();
	
	public void setPrevSwung(EnumHand hand);
	
	public void startWeaponSwing(WeaponSwing swing, int delay);
	
	public boolean isAtUltimate();
	
	public ArmPosePlus currentArmPose();
	
	public void setArmPose(ArmPosePlus armPose);

	public boolean startGlove(EntityPlayer player);

	public enum WeaponSwing
	{
		SHORT(5),
		LONG(5),
		SPEAR(5),
		HAXE(5),
		DUAL(5),
		GLOVE(5);
		
		private int swingAmount;
		WeaponSwing(int swingAmount)
		{
			this.swingAmount=swingAmount;
		}
		
		public int getMaxSwing()
		{
			return this.swingAmount;
		}
	}
}
