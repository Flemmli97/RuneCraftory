package com.flemmli97.runecraftory.common.core.handler.capabilities;

public interface IPlayerAnim {
	
	public int animationTick();
	
	public void startAnimation(int tick);
	
	public void update();
	
	public boolean canUseSpear();
	
	public void spearTicker();

	public void startSpear();
	
	public int getSpearTick();
}
