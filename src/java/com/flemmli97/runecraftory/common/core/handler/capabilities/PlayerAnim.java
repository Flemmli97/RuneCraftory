package com.flemmli97.runecraftory.common.core.handler.capabilities;

public class PlayerAnim implements IPlayerAnim{

	private int ticker = 0;
	private int spearUse = 0;
	@Override
	public int animationTick() {
		return this.ticker;
	}
	
	public void startAnimation(int tick)
	{
		this.ticker=tick;
	}
	
	public boolean canUseSpear(boolean flag)
	{
		if(spearUse> 0 || flag && spearUse++<20)
			return true;
		spearUse=0;
		return false;
	}

}
