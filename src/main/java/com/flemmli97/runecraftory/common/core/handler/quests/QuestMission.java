package com.flemmli97.runecraftory.common.core.handler.quests;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCBase;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.nbt.NBTTagCompound;

public class QuestMission {
	
	private IObjective obj;
	private String ownerUUID;
	private String ownerName;
	public QuestMission(NBTTagCompound compound)
	{
		this.readFromNBT(compound);
	}
	
	public QuestMission(IObjective obj, @Nullable EntityNPCBase questOwner)
	{
		this.obj=obj;
		if(questOwner!=null)
		{
			if(questOwner.hasCustomName())
				this.ownerName=questOwner.getCustomNameTag();
			else 
				this.ownerName=questOwner.getName();
			this.ownerUUID=questOwner.getCachedUniqueIdString();
		}
	}

	@Nullable
	public String questOwner()
	{
		return this.ownerUUID;
	}
	
	@Nullable
	public String questOwnerName()
	{
		return this.ownerName;
	}
	
	public IObjective questObjective()
	{
		return this.obj;
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		this.ownerUUID=compound.getString("OwnerUUID");
		this.ownerName=compound.getString("OwnerName");
		try {
			IObjective obj = Objectives.getObjective(compound.getString("ObjectiveName")).newInstance();
			obj.readFromNBT(compound.getCompoundTag("Objective"));
			this.obj=obj;
		} catch (InstantiationException | IllegalAccessException e) {
			LibReference.logger.error("Error reading mission from nbt");
		}
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setString("ObjectiveName", Objectives.getName(this.obj.getClass()));
		compound.setTag("Objective", this.obj.writeToNBT(new NBTTagCompound()));
		if(this.ownerUUID!=null)
			compound.setString("OwnerUUID", this.ownerUUID);
		if(this.ownerName!=null)
			compound.setString("OwnerName", this.ownerName);
		return compound;
	}
	
	public String questDescription()
	{
		return this.obj.objDesc();
	}
	
	public String questAcceptMsg()
	{
		return "Accepted quest: " + this.obj.objDesc() + (this.ownerName!=null?" from "+ this.ownerName:"");
	}
}
