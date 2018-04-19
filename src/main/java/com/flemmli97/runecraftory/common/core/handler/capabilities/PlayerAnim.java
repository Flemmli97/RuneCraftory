package com.flemmli97.runecraftory.common.core.handler.capabilities;

import net.minecraft.util.EnumHand;

public class PlayerAnim implements IPlayerAnim{

	private int ticker = 0;
	private int spearUse = 0;
	private int offHandTick;
	private EnumHand prevHand = EnumHand.MAIN_HAND;
	private WeaponSwing weapon;
	private int swings, timeSinceLastSwing;
	@Override
	public int animationTick() {
		return this.ticker;
	}
	
	@Override
	public void startAnimation(int tick)
	{
		this.ticker=tick;
	}
	
	@Override
	public void update() {
		this.ticker=Math.max(this.ticker--, 0);
		this.timeSinceLastSwing=Math.max(this.timeSinceLastSwing--, 0);
		if(this.timeSinceLastSwing==0)
			this.swings=0;
		this.spearTicker=Math.max(this.spearTicker--, 0);
		this.offHandTick=Math.max(this.offHandTick--, 0);
	}

	//----Spear Use Handling
	private int spearTicker = 0;
	@Override
	public boolean canUseSpear()
	{
		if(this.spearTicker>0 && spearUse++<20)
			return true;
		this.spearUse=0;
		this.spearTicker=0;
		return false;
	}
	@Override
	public void startSpear()
	{
		this.spearTicker=60;
	}
	@Override
	public int getSpearTick()
	{
		return this.spearTicker;
	}

	@Override
	public void disableOffHand() {
		this.offHandTick=100;
	}

	@Override
	public boolean canUseOffHand() {
		return this.offHandTick==0;
	}

	@Override
	public EnumHand getPrevSwung() {
		return prevHand;
	}

	@Override
	public void setPrevSwung(EnumHand hand) {
		prevHand = hand;
	}

	@Override
	public void startWeaponSwing(WeaponSwing swing, int delay) {
		if(this.weapon!=swing)
			this.swings=0;
		this.swings++;
		this.timeSinceLastSwing=delay;
		this.weapon=swing;
	}

	@Override
	public boolean isAtUltimate() {
		return this.weapon.getMaxSwing()==this.swings;
	}
}
