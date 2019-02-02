package com.flemmli97.runecraftory.common.items.special.spells;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.entity.magic.EntityFireBall;
import com.flemmli97.runecraftory.common.items.special.ItemCast;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemFireballCast extends ItemCast{

	private int runeCost = 10;
	/**
	 * So those variables are global and you cant switch between items to fire more spells
	 */
	private int ticker, summon;
	public ItemFireballCast() {
		super("fireball");
	}
	
	@Override
	public void update(ItemStack stack, EntityPlayer player) {
		this.ticker=Math.max(--this.ticker, 0);
		if(this.ticker==0 && this.summon>0)
		{
			this.summon--;
			this.ticker=15;
		}
	}

	@Override
	public boolean use(World world, EntityPlayer player, ItemStack stack) {
		if(!world.isRemote)
		{
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(cap.getRunePoints()>=this.runeCost)
			{
				cap.decreaseRunePoints(player, this.runeCost);
				EntityFireBall ball = new EntityFireBall(world, player);
				ball.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1, 0);
				world.spawnEntity(ball);
				this.summon++;
				this.ticker=15;
				if(this.summon==3)
				{
					this.summon=0;
					player.getCooldownTracker().setCooldown(stack.getItem(), this.coolDown());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public int coolDown() {
		return 20;
	}

	@Override
	public void levelSkill(EntityPlayer player) {
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		cap.increaseSkill(EnumSkills.FIRE, player, 3);
	}

}
