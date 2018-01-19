package com.flemmli97.runecraftory.api.enums;

public enum EnumToolTier {

	SCRAP(0,"scrap"),
	IRON(1,"iron"),
	SILVER(2,"silver"),
	GOLD(3,"gold"),
	PLATINUM(4,"platinum");
	
	private String id;
	private int level;
	EnumToolTier(int level, String id)
	{
		this.id = id;
		this.level=level;
	}
	public String getName() {
		return this.id;
	}
	public int getTierLevel()
	{
		return this.level;
	}
}
