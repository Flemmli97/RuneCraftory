package com.flemmli97.runecraftory.common.items.special.spells;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.entity.magic.EntityWaterLaser;
import com.flemmli97.runecraftory.common.items.special.ItemCast;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class ItemWaterLaserCast extends ItemCast{

	private int runeCost = 10;

	public ItemWaterLaserCast() {
		super("water_laser");
	}
	
	@Override
	public void update(ItemStack stack, EntityPlayer player) {
	}

	@Override
	public boolean use(World world, EntityPlayer player, ItemStack stack) {
		if(!world.isRemote)
		{
			IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
			if(cap.getRunePoints()>=this.runeCost)
			{
				cap.decreaseRunePoints(player, this.runeCost);
				EntityWaterLaser laser = new EntityWaterLaser(world, player);
				world.spawnEntity(laser);
				player.getCooldownTracker().setCooldown(stack.getItem(), this.coolDown());
				player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:slowness"), this.coolDown(), 10));
				player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("minecraft:jump_boost"), this.coolDown(), 128));

				return true;
			}
		}
		return false;
	}

	@Override
	public int coolDown() {
		return EntityWaterLaser.livingTickMax;
	}

	@Override
	public void levelSkill(EntityPlayer player) {
		IPlayer cap = player.getCapability(PlayerCapProvider.PlayerCap, null);
		cap.increaseSkill(EnumSkills.WATER, player, 3);
	}

}
