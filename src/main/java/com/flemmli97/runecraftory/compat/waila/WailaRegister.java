package com.flemmli97.runecraftory.compat.waila;

import com.flemmli97.runecraftory.common.entity.EntityGate;
import com.flemmli97.runecraftory.common.entity.EntityMobBase;

import mcp.mobius.waila.api.IWailaRegistrar;

public class WailaRegister {
	
	public static void init(IWailaRegistrar r) {
		r.registerHeadProvider(new EntityStatsWaila(), EntityMobBase.class);
		r.registerBodyProvider(new EntityStatsWaila(), EntityMobBase.class);
		r.registerHeadProvider(new GateStatsWaila(), EntityGate.class);
		r.registerBodyProvider(new GateStatsWaila(), EntityGate.class);
	}
}
