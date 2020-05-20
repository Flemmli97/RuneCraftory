package com.flemmli97.runecraftory.common.lib.enums;

import java.util.Random;

import net.minecraft.util.IStringSerializable;

public enum EnumMineralTier implements IStringSerializable 
{
	IRON("iron"),
	BRONZE("bronze"),
	SILVER("silver"),
	GOLD("gold"),
	DIAMOND("diamond"),
	PLATINUM("platinum"),
	ORICHALCUM("orichalcum"),
	DRAGONIC("dragonic"),
	WATER("water"),
	EARTH("earth"),
	FIRE("fire"),
	WIND("wind");

	private final String name;

    private EnumMineralTier(String name)
    {
        this.name = name;
    }

    @Override
	public String toString()
    {
        return this.name;
    }

    @Override
	public String getName()
    {
        return this.name;
    }
    
    public static EnumMineralTier randomNonElemental(Random rand)
    {
    	return values()[rand.nextInt(5)];
    }
    
    public static boolean isElemental(EnumMineralTier tier)
    {
        return tier == EnumMineralTier.FIRE || tier == EnumMineralTier.WIND || tier == EnumMineralTier.EARTH || tier == EnumMineralTier.WATER;
    }
	
}