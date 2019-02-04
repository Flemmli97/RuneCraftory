package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ObjectiveShip extends ObjectiveItems{

	public ObjectiveShip(){}
	
	public ObjectiveShip(List<TaskTracker<ItemStack>> items, int moneyReward, List<ItemStack> rewards)
	{
		super(items, moneyReward, rewards);
	}

	@Override
	public ObjectiveType type() {
		return ObjectiveType.SHIP;
	}

	@Override
	public String objDesc()
	{
		return "Ship "+super.objDesc();
	}
}
