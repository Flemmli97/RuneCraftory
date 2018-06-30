package com.flemmli97.runecraftory.compat.waila;

import net.minecraft.entity.player.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import mcp.mobius.waila.api.*;
import com.flemmli97.runecraftory.common.entity.*;
import com.flemmli97.runecraftory.common.init.*;
import com.flemmli97.runecraftory.common.core.handler.config.*;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.*;
import com.flemmli97.runecraftory.common.lib.*;
import net.minecraft.client.resources.*;
import java.util.*;

public class GateStatsWaila implements IWailaEntityProvider
{
	@Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity ent, NBTTagCompound tag, World world) {
        return tag;
    }
    
    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (entity instanceof EntityGate) 
        {
            currenttip.clear();
            if (accessor.getPlayer().getHeldItemMainhand().getItem() == ModItems.debug || accessor.getPlayer().capabilities.isCreativeMode || ConfigHandler.debugMode) 
            {
                EntityGate mob = (EntityGate)entity;
                for (IAttributeInstance a : mob.getAttributeMap().getAllAttributes()) 
                {
                    if (a.getAttribute().getClass() == RangedAttribute.class) 
                    {
                        if (a.getAttribute() == SharedMonsterAttributes.MAX_HEALTH) 
                        	currenttip.add("Health: " + Math.round(mob.getHealth() / LibConstants.DAMAGESCALE) + " / " + Math.round(a.getAttributeValue() / LibConstants.DAMAGESCALE));
                    }
                    else 
                    {
                        currenttip.add(I18n.format(a.getAttribute().getName()) + ": " + a.getAttributeValue());
                    }
                }
            }
        }
        return currenttip;
    }
    
    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        if (entity instanceof EntityGate && (accessor.getPlayer().getHeldItemMainhand().getItem() == ModItems.debug || accessor.getPlayer().capabilities.isCreativeMode||ConfigHandler.debugMode)) 
        {
            currenttip.set(0, currenttip.get(0) + " (Level: " + ((EntityGate)entity).level() + ")");
        }
        return currenttip;
    }
    
    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }
    
    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) {
        return null;
    }
}
