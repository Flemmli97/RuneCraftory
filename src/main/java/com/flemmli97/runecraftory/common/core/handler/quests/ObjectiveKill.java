package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import com.flemmli97.runecraftory.common.core.handler.quests.TaskTracker.NBTParser;
import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class ObjectiveKill implements IObjective<EntityLivingBase>{

	private List<TaskTracker<ResourceLocation>> mobs;
	private int money;
	private List<ItemStack> rewards;
	
	public ObjectiveKill(){}
	
	public ObjectiveKill(List<TaskTracker<ResourceLocation>> entities, int money, List<ItemStack> stack)
	{
		this.mobs=entities;
		this.money=money;
		this.rewards=stack;
	}
	
	@Override
	public boolean matches(EntityLivingBase entity)
	{
		for(TaskTracker<ResourceLocation> e : this.mobs)
		{
			if(e.getTask().equals(EntityList.getKey(entity)))
				return true;
		}
		return false;
	}
	
	@Override
	public void updateProgress(EntityPlayer player, EntityLivingBase entity) {
		for(TaskTracker<ResourceLocation> e : this.mobs)
		{
			if(e.getTask().equals(EntityList.getKey(entity)))
				e.update();
		}
	}
	@Override
	public boolean isFinished()
	{
		for(TaskTracker<ResourceLocation> t : this.mobs)
		{
			if(!t.finished())
				return false;
		}
		return true;
	}

	@Override
	public String currentProgress() {
		String s = "";
		boolean first = true;
		for(TaskTracker<ResourceLocation> res : this.mobs)
		{
			s+=(first?"":", ")+I18n.format("entity." + EntityList.getTranslationName(res.getTask())+".name")+" ("+res.progress()+"/"+res.getAmount()+")";
			first=false;
		}
		return s;
	}

	@Override
	public List<ItemStack> rewards() {
		System.out.println(this.rewards);

		return this.rewards;
	}

	@Override
	public int moneyReward() {
		return this.money;
	}
	
	@Override
	public ObjectiveType type() {
		return ObjectiveType.KILL;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("Mobs", Constants.NBT.TAG_COMPOUND);
		if(this.mobs==null)
			this.mobs = Lists.newArrayList();
		list.forEach(tag->{
			this.mobs.add(new TaskTracker<ResourceLocation>((NBTTagCompound)tag, NBTParser.id));

		});
		this.money=compound.getInteger("Money");
		NBTTagList list2 = compound.getTagList("Rewards", Constants.NBT.TAG_COMPOUND);
		if(this.rewards==null)
			this.rewards = Lists.newArrayList();
		list2.forEach(tag->{
			this.rewards.add(new ItemStack((NBTTagCompound)tag));

		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList list = new NBTTagList();
		this.mobs.forEach(track->{
			list.appendTag(track.writeToNBT(NBTParser.id));
		});
		compound.setTag("Mobs", list);
		compound.setInteger("Money", this.money);
		NBTTagList list2 = new NBTTagList();
		this.rewards.forEach(stack->{
			list2.appendTag(stack.writeToNBT(new NBTTagCompound()));
		});
		compound.setTag("Rewards", list2);
		return compound;
	}
	@Override
	public String objDesc()
	{
		String s = "";
		boolean first = true;
		for(TaskTracker<ResourceLocation> res : this.mobs)
		{
			s+=(first?"":", ")+I18n.format("entity." + EntityList.getTranslationName(res.getTask())+".name")+" ("+res.getAmount()+")";
			first=false;
		}
		return "Defeat " + s;
	}
	
	@Override
	public String rewardDesc()
	{
		String s = "";
		boolean first = true;
		for(ItemStack res : this.rewards)
		{
			s+=(first?"":", ")+I18n.format(res.getUnlocalizedName()+".name")+" ("+res.getCount()+")";
			first=false;
		}
		return "Rewards: " + s;
	}
	
	@Override
	public String toString()
	{
		return this.objDesc()+";"+this.rewardDesc();
	}
}
