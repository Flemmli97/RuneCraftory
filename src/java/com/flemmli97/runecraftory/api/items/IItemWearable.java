package com.flemmli97.runecraftory.api.items;

import java.util.Map;

import com.flemmli97.runecraftory.api.enums.EnumElement;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.Item;

public interface IItemWearable {

	public Map<IAttribute, Integer> statIncrease();
	
	public void updateStatIncrease(IAttribute attribute, int amount);

	public void setElement(EnumElement element);
	
	public EnumElement getElement();
	
	public Item[] upgradeItems();
	
	public void addUpgradeItem(Item stack);
}
