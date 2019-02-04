package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.Map;

import com.google.common.collect.Maps;

public class Objectives {

	private static final Map<String, Class<? extends IObjective<?>>> nameObjectives = Maps.newHashMap();
	private static final Map<Class<? extends IObjective<?>>, String> objectivesName = Maps.newHashMap();

	public static void registerObjectiveType(String name, Class<? extends IObjective<?>> clss)
	{
		nameObjectives.put(name, clss);
		objectivesName.put(clss, name);
	}
	
	public static Class<? extends IObjective<?>> getObjective(String name)
	{
		return nameObjectives.get(name);
	}
	
	public static String getName(Class<? extends IObjective<?>> name)
	{
		return objectivesName.get(name);
	}
	
	static
	{
		registerObjectiveType("ObjHarv", ObjectiveHarvest.class);
		registerObjectiveType("ObjShip", ObjectiveShip.class);
		registerObjectiveType("ObjBring", ObjectiveBring.class);
		registerObjectiveType("ObjKill", ObjectiveKill.class);
	}
}
