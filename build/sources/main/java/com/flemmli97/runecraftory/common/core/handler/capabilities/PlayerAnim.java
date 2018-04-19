package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.util.EnumHand;

public class PlayerAnim implements IPlayerAnim{

	private int ticker = 0;
	private int spearUse = 0;
	private int offHandTick;
	private EnumHand prevHand = EnumHand.MAIN_HAND;
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

	@Override
	public void disableOffHand() {
		this.offHandTick=100;
	}

	@Override
	public int offHandCooldown() {
		return this.offHandTick;
	}

	@Override
	public void updateOffHandTick() {
		this.offHandTick--;
	}

	@Override
	public EnumHand getPrevSwung() {
		return prevHand;
	}

	@Override
	public void setPrevSwung(EnumHand hand) {
		prevHand = hand;
	}
}
