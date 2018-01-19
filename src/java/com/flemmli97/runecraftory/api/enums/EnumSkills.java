package com.flemmli97.runecraftory.api.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnumSkills {

	SHORTSWORD("short"),
	LONGSWORD("long"),
	SPEAR("spear"),
	HAMMERAXE("haxe"),
	DUAL("dual"),
	FIST("fist"),
	FIRE("fire"),
	WATER("water"),
	EARTH("earth"),
	WIND("wind"),
	DARK("dark"),
	LIGHT("light"),
	LOVE("love"),
	FARMING("farming"),
	LOGGING("logging"),
	MINING("mining"),
	FISHING("fishing"),
	COOKING("cooking"),
	FORGING("forging"),
	CHEMISTRY("chem"),
	CRAFTING("craft"),
	SEARCHING("search"),
	WALKING("walk"),
	SLEEPING("sleep"),
	EATING("eat"),
	DEFENCE("def"),
	RESPOISON("poison"),
	RESSEAL("seal"),
	RESPARA("para"),
	RESSLEEP("resSleep"),
	RESFAT("fat"),
	RESCOLD("cold"),
	BATH("bath"),
	TAMING("taming"),
	THROW("throw"),
	LEADER("leader"),
	BARTERING("bart");
	
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
