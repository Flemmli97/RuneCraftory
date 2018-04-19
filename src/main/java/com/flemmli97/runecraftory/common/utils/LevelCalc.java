package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.common.lib.LibConstants;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LevelCalc {

	public static int xpAmountForLevelUp(int level)
	{
		return level^2+level+50;
	}

	public static int xpAmountForSkills(int level)
	{
		return level*10+15;
	}

	public static float tamingMultiplerOnLevel(int levelDiff)
	{
		if(levelDiff<=0)
			levelDiff=1;
		float f = (float) (1.5/(Math.sqrt(levelDiff)*levelDiff));///level;
		return Math.min(f, 1);
	}
	
	public static int xpAmountForLevelUpMobs(int level)
	{
		return level^2+level*2+50;
	}
	
	public static float scaleForVanilla(float damage)
	{
		if(damage<=100)
			damage=damage*0.03F+2;
		else if(damage<=1000)
			damage=damage*0.012F+5;
		else if(damage<=10000)
			damage=(float) (Math.sqrt((damage-1000)/10.0F)+17);
		else
			damage=(float) (Math.sqrt((damage-10000)/29.7517355371900826F)+47);
		return damage;
	}
	
	public static int xpFromLevel(int base, int level)
	{
		return (int) (base + base/10.0F*level);
	}

	public static int moneyFromLevel(int base, int level)
	{
		return Math.round(base + base*level/100.0F);
	}

	public static int levelFromDistSpawn(World world, BlockPos currentPos)
	{
		int level = LibConstants.baseLevel;
		BlockPos spawn = world.getSpawnPoint();
		double dis =1;
		if(world.provider.getDimension()==0)
			dis= Math.sqrt(spawn.distanceSq(currentPos.getX(), spawn.getY(), currentPos.getZ()));
		else
			dis=Math.sqrt(spawn.distanceSq(currentPos.getX(), spawn.getY(), currentPos.getZ()))+200;
			dis*=1.2;
		return Math.round((float) (level+Math.max(1, (dis-250)*0.1F) ));
	}
	
	
	public static double initStatIncreaseLevel(double baseValue, int level, boolean isBoss, boolean isHealth)
	{
		float scale = isBoss?1.3F:1.0F;
		double newValue = baseValue+baseValue/2.0*Math.max(0, level-LibConstants.baseLevel)*scale;
		if(isHealth)
		{
			int dec = (int) (newValue/LibConstants.DAMAGESCALE);
			newValue = dec*LibConstants.DAMAGESCALE;
		}
		else
		{
			int dec = (int) (newValue*10);
			newValue = dec/10.0;
		}
		return newValue;
	}
	
	public static double onEntityLevelUp(double baseValue, double currentValue, boolean isBoss, boolean isHealth)
	{
		float scale = isBoss?1.3F:1.0F;
		double newValue = currentValue +baseValue/2.0*scale;
		if(isHealth)
		{
			int dec = (int) (newValue/LibConstants.DAMAGESCALE);
			newValue = dec*LibConstants.DAMAGESCALE;
		}
		else
		{
			int dec = (int) (newValue*10);
			newValue = dec/10.0;
		}
		return newValue;
	}
}
