package com.flemmli97.runecraftory.common.core.handler.capabilities;

import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.client.render.ArmPosePlus;
import com.flemmli97.runecraftory.client.render.EnumToolCharge;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class PlayerAnim implements IPlayerAnim{

	private int ticker = 0;
	private int spearUse = 0;
	private int offHandTick;
	private EnumHand prevHand = EnumHand.MAIN_HAND;
	private WeaponSwing weapon;
	private int swings, timeSinceLastSwing;
	private ArmPosePlus armPose = ArmPosePlus.DEFAULT;
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
	public void update(EntityPlayer player) {
		this.ticker=Math.max(this.ticker--, 0);
		this.timeSinceLastSwing=Math.max(this.timeSinceLastSwing--, 0);
		if(this.timeSinceLastSwing==0)
			this.swings=0;
		this.spearTicker=Math.max(this.spearTicker--, 0);
		this.offHandTick=Math.max(this.offHandTick--, 0);
		if(player.world.isRemote)
		{
			ItemStack heldMain = player.getHeldItemMainhand();
			if (heldMain.getItem() instanceof IRpUseItem)
	        {
	            if (player.getItemInUseCount() > 0)
	            {
	                EnumToolCharge action = ((IRpUseItem)heldMain.getItem()).chargeType(heldMain);
	                switch(action)
	                {
						case CHARGECAN:armPose=ArmPosePlus.CHARGECAN;
							break;
						case CHARGEFISHING:armPose=ArmPosePlus.CHARGEFISHING;
							break;
						case CHARGEFIST:armPose=ArmPosePlus.CHARGEFIST;
							break;
						case CHARGELONG:armPose=ArmPosePlus.CHARGELONG;
							break;
						case CHARGESICKLE:armPose=ArmPosePlus.CHARGESICKLE;
							break;
						case CHARGESPEAR:armPose=ArmPosePlus.CHARGESPEAR;
							break;
						case CHARGESWORD:armPose=ArmPosePlus.CHARGESWORD;
							break;
						case CHARGEUPTOOL:armPose=ArmPosePlus.CHARGEUPTOOL;
							break;
						case CHARGEUPWEAPON:armPose=ArmPosePlus.CHARGEUPWEAPON;
							break;
	                }
	            }
	            else
	            	armPose=ArmPosePlus.DEFAULT;
	        }
			else
            	armPose=ArmPosePlus.DEFAULT;
		}
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
	@Override
	public ArmPosePlus currentArmPose()
	{
		return this.armPose;
	}
	@Override
	public void setArmPose(ArmPosePlus armPose)
	{
		this.armPose=armPose;
	}
}
