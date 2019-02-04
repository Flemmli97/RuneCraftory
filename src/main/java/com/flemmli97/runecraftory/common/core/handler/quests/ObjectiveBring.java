package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import net.minecraft.item.ItemStack;

public class ObjectiveBring extends ObjectiveItems{

	public ObjectiveBring(){}
	
	public ObjectiveBring(List<TaskTracker<ItemStack>> items, int moneyReward, List<ItemStack> rewards)
	{
		super(items, moneyReward, rewards);
	}

	@Override
	public ObjectiveType type() {
		return ObjectiveType.BRING;
	}

	@Override
	public String objDesc()
	{
		return "Bring "+super.objDesc();
	}
}
