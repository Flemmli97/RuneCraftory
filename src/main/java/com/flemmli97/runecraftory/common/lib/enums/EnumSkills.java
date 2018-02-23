package com.flemmli97.runecraftory.common.lib.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnumSkills {

	SHORTSWORD("Short Sword"),
	LONGSWORD("Long Sword"),
	SPEAR("Spear"),
	HAMMERAXE("Hammer/Axe"),
	DUAL("Dual Sword"),
	FIST("Fist"),
	FIRE("Fire"),
	WATER("Water"),
	EARTH("Earth"),
	WIND("Wind"),
	DARK("Dark"),
	LIGHT("Light"),
	LOVE("Love"),
	FARMING("Farming"),
	LOGGING("Logging"),
	MINING("Mining"),
	FISHING("Fishing"),
	COOKING("Cooking"),
	FORGING("Forging"),
	CHEMISTRY("Chemistry"),
	CRAFTING("Craftting"),
	SLEEPING("Sleeping"),
	EATING("Eating"),
	DEFENCE("Defence"),
	RESPOISON("Poison-Res"),
	RESSEAL("Seal-Res"),
	RESPARA("Para-Res"),
	RESSLEEP("Sleep-Res"),
	RESFAT("Fatigue-Res"),
	RESCOLD("Cold-Res"),
	BATH("Bathing"),
	TAMING("Taming"),
	LEADER("Leading");
	
	private static final Map<String, EnumSkills> skillMap = new HashMap<String, EnumSkills>();
	
	private String identifier;
	EnumSkills(String identifier)
	{
		this.identifier = identifier;
	}
	
	public String getIdentifier()
	{
		return this.identifier;
	}
	
	public static EnumSkills getFromString(String name)
	{
		return skillMap.get(name);
	}
	
	static
    {
        for (EnumSkills skill : values())
        {
        		skillMap.put(skill.identifier, skill);
        }
    }
}
