package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.util.EnumHand;

public interface IPlayerAnim {
	
	public int animationTick();
	
	public void startAnimation(int tick);
	
	public void update();
	
	public boolean canUseSpear();
	
	public void spearTicker();

	public void startSpear();
	
	public int getSpearTick();
	
	public void disableOffHand();
	
	public int offHandCooldown();
	
	public void updateOffHandTick();
	
	public EnumHand getPrevSwung();
	
	public void setPrevSwung(EnumHand hand);
}
