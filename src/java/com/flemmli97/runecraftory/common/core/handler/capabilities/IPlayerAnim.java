package com.flemmli97.runecraftory.common.core.handler.capabilities;

public interface IPlayerAnim {
	
	public int animationTick();
	
	public void startAnimation(int tick);
	
	public boolean canUseSpear(boolean flag);

}
