package com.flemmli97.runecraftory.api.entities;

import java.util.List;

public interface IRFNpc extends IEntityBase{

	public List<String> talkingMessage();
	
	public List<String> greetingMessage();
	
	public int getFriendHearts();
	
	public boolean isEngaged();
	
	public boolean isMarried();
}
