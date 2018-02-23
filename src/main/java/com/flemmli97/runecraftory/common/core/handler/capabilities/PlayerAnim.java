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
	
	@Override
	public void update() {
		this.ticker--;
	}

	//----Spear Use Handling
	private int spearTicker = 0;
	public boolean canUseSpear()
	{
		if(this.spearTicker>0 && spearUse++<20)
			return true;
		this.spearUse=0;
		this.spearTicker=0;
		return false;
	}
	
	public void spearTicker()
	{
		this.spearTicker--;
	}
	public void startSpear()
	{
		this.spearTicker=60;
	}
	
	public int getSpearTick()
	{
		return this.spearTicker;
	}
}
