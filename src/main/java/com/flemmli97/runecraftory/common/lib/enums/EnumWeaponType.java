package com.flemmli97.runecraftory.common.lib.enums;

public enum EnumWeaponType {
	
	//3 = normal
	FARM("farm", 3.0F, 0.0F),
	SHORTSWORD("short", 2.5F, 1.0F),
	LONGSWORD("long", 4.0F, 2.0F),
	SPEAR("spear", 5.0F, 0.5F),
	HAXE("haxe", 4.5F, 2.0F),
	DUAL("dual", 3.0F, 1.0F),
	GLOVE("glove", 3.0F, 0.5F),
	STAFF("staff", 3.5F, 0.0F);
	
	private String id;
	private float range;
	private float aoe;
	EnumWeaponType(String identifier, float range, float aoe)
	{
		this.id=identifier;
		this.range = range;
		this.aoe=aoe;
	}
	
	public String getName()
	{
		return this.id;
	}
	public float getRange()
	{
		return this.range;
	}
	public float getAOE()
	{
		return this.aoe;
	}
}
