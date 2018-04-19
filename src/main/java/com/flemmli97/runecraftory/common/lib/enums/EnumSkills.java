package com.flemmli97.runecraftory.common.lib.enums;

import java.util.HashMap;
import java.util.Map;

public enum EnumSkills {

	SHORTSWORD("Short Sword", 1.5F, 1, 0.5F, 0.5F, 0),
	LONGSWORD("Long Sword", 1.5F, 1, 1, 0, 0),
	SPEAR("Spear", 1.5F, 1, 1, 0, 0),
	HAMMERAXE("Hammer/Axe", 1.5F, 1, 1, 0, 0),
	DUAL("Dual Sword", 1.5F, 1, 1, 0.5F, 0),
	FIST("Fist", 1, 2, 1, 0.5F, 0),
	FIRE("Fire", 1, 2, 0, 0, 1),
	WATER("Water", 1, 2, 0, 0, 1),
	EARTH("Earth", 1, 2, 0, 0, 1),
	WIND("Wind", 1, 2, 0, 0, 1),
	DARK("Dark", 1, 2, 0, 0, 1),
	LIGHT("Light", 1, 2, 0, 0, 1),
	LOVE("Love", 1, 2, 0, 0, 1),
	FARMING("Farming", 0, 3, 0.5F, 0.5F, 0),
	LOGGING("Logging", 0, 4, 1.5F, 0.5F, 0),
	MINING("Mining", 0, 2, 1.5F, 0.5F, 0),
	FISHING("Fishing", 0, 5, 0F, 0.5F, 1),
	COOKING("Cooking", 1, 2, 0F, 0.5F, 1),
	FORGING("Forging", 1, 2, 1F, 0.5F, 1),
	CHEMISTRY("Chemistry", 1, 2, 0F, 0.5F, 1.5F),
	CRAFTING("Crafting", 1, 2, 0.5F, 0.5F, 1),
	SLEEPING("Sleeping", 7, 5, 0, 2.5F, 0),
	EATING("Eating", 2, 3, 0, 0.5F, 0),
	DEFENCE("Defence", 5, 3, 0, 1, 0),
	RESPOISON("Poison-Res", 10, 2, 0, 0.5F, 0.5F),
	RESSEAL("Seal-Res", 5, 6, 0, 0.5F, 0.5F),
	RESPARA("Para-Res", 3, 3, 0, 0.5F, 0.5F),
	RESSLEEP("Sleep-Res", 5, 2, 0, 0.5F, 0.5F),
	RESFAT("Fatigue-Res", 10, 6, 0, 0.5F, 0.5F),
	RESCOLD("Cold-Res", 12, 3, 0, 0.5F, 0.5F),
	BATH("Bathing", 3, 4, 0, 1, 0),
	TAMING("Taming", 3, 4, 1, 1, 1),
	LEADER("Leading", 7, 5, 1, 1, 1);
	
	private static final Map<String, EnumSkills> skillMap = new HashMap<String, EnumSkills>();
	
	private String identifier;
	private float hp, str, vit, intel;
	private int rp;
	EnumSkills(String identifier,float healthIncrease, int rpIncrease, float strIncrease, float vitIncrease, float intelIncrease)
	{
		this.identifier = identifier;
		this.hp=healthIncrease;
		this.rp=rpIncrease;
		this.str=strIncrease;
		this.vit=vitIncrease;
		this.intel=intelIncrease;
	}
	
	public String getIdentifier()
	{
		return this.identifier;
	}
	
	public static EnumSkills getFromString(String name)
	{
		return skillMap.get(name);
	}
	
	public float getHealthIncrease()
	{
		return this.hp;
	}
	
	public int getRPIncrease()
	{
		return this.rp;
	}
	
	public float getStrIncrease()
	{
		return this.str;
	}
	
	public float getVitIncrease()
	{
		return this.vit;
	}
	
	public float getIntelIncrease()
	{
		return this.intel;
	}
	
	static
    {
        for (EnumSkills skill : values())
        {
        		skillMap.put(skill.identifier, skill);
        }
    }
}
