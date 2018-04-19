package com.flemmli97.runecraftory.compat.waila;

import java.util.List;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibConstants;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityStatsWaila implements IWailaEntityProvider{

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
		return tag;
	}

	@Override
	public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
			IWailaConfigHandler config) {
		if(entity instanceof EntityMobBase)
		{
			currenttip.clear();
			if(accessor.getPlayer().getHeldItemMainhand().getItem()==ModItems.debug|| accessor.getPlayer().capabilities.isCreativeMode)
			{
				EntityMobBase mob = (EntityMobBase) entity;
				for(IAttributeInstance a : mob.getAttributeMap().getAllAttributes())
				{
					if(a.getAttribute().getClass()==RangedAttribute.class)
					{
						if(a.getAttribute()==SharedMonsterAttributes.MAX_HEALTH)
							currenttip.add("Health: " + Math.round(mob.getHealth()/LibConstants.DAMAGESCALE) + " / " + Math.round(a.getAttributeValue()/LibConstants.DAMAGESCALE));
					}
					else
						currenttip.add(I18n.format(a.getAttribute().getName()) + ": " + a.getAttributeValue());
				}
			}
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
			IWailaConfigHandler config) {
		if(entity instanceof EntityMobBase)
		{
			if(accessor.getPlayer().getHeldItemMainhand().getItem()==ModItems.debug || accessor.getPlayer().capabilities.isCreativeMode)
			{
				currenttip.set(0, currenttip.get(0) + " (Level: " + ((EntityMobBase)entity).level() + ")");
			}
		}
		return currenttip;
	}

	@Override
	public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor,
			IWailaConfigHandler config) {
		return null;
	}

}
