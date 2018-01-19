package com.flemmli97.runecraftory.api.enums;

public enum EnumElement {

	NONE("white", "none"),
	WATER("dark_blue", "water"),
	EARTH("yellow",  "earth"),
	WIND("green", "wind"),
	FIRE("dark_red", "fire"),
	LIGHT("white", "light"),
	DARK("purple", "dark"),
	LOVE("pink" , "love");
	
	private String name;
	private String color;
	
	EnumElement(String color, String name)
	{
		this.color=color;
		this.name=name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getColor()
	{
		return this.color;
	}
}
