package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ObjectiveHarvest extends ObjectiveItems{

	public ObjectiveHarvest(){}
	
	public ObjectiveHarvest(List<TaskTracker<ItemStack>> items, int moneyReward, List<ItemStack> rewards)
	{
		super(items, moneyReward, rewards);
	}

	@Override
	public ObjectiveType type() {
		return ObjectiveType.HARVEST;
	}

	@Override
	public String objDesc()
	{
		return "Harvest "+super.objDesc();
	}
}
