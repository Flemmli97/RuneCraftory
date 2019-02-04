package com.flemmli97.runecraftory.api.entities;

import java.util.List;

import net.minecraft.entity.ai.attributes.IAttribute;

public interface IRFNpc extends IEntityBase
{
	public List<String> talkingMessage();
    
	public List<String> greetingMessage();
    
	public int getFriendHearts();
    
	public boolean isEngaged();
    
	public boolean isMarried();
	
	public int getAttributeValue(IAttribute att);

}
