package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;

import com.flemmli97.runecraftory.common.core.handler.quests.TaskTracker.NBTParser;
import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public abstract class ObjectiveItems implements IObjective<ItemStack>{

	private List<TaskTracker<ItemStack>> items = Lists.newArrayList();
	private int money;
	private List<ItemStack> rewards = Lists.newArrayList();
	
	public ObjectiveItems()
	{

	}
	
	public ObjectiveItems(List<TaskTracker<ItemStack>> items, int moneyReward, List<ItemStack> rewards)
	{
		this.items=items;
		this.money=moneyReward;
		this.rewards=rewards;
	}
	
	@Override
	public boolean matches(ItemStack stack)
	{
		for(TaskTracker<ItemStack> e : this.items)
		{
			if(ItemStack.areItemsEqual(stack, e.getTask()))
				return true;
		}
		return false;
	}
	
	@Override
	public void updateProgress(EntityPlayer player, ItemStack stack) {
		for(TaskTracker<ItemStack> e : this.items)
		{
			if(ItemStack.areItemsEqual(stack, e.getTask()))
				e.update();
		}
	}
	@Override
	public boolean isFinished()
	{
		for(TaskTracker<ItemStack> t : this.items)
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
		for(TaskTracker<ItemStack> t : this.items)
		{
			s+=(first?"":", ")+I18n.format(t.getTask().getUnlocalizedName())+" ("+t.progress()+"/"+t.getAmount()+")";
			first=false;
		}
		return s;
	}

	@Override
	public List<ItemStack> rewards() {
		return this.rewards;
	}

	@Override
	public int moneyReward() {
		return this.money;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList list = compound.getTagList("Items", Constants.NBT.TAG_COMPOUND);
		if(this.items==null)
			this.items = Lists.newArrayList();
		list.forEach(tag->{
			this.items.add(new TaskTracker<ItemStack>((NBTTagCompound)tag, NBTParser.itemstack));

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
		this.items.forEach(track->{
			list.appendTag(track.writeToNBT(NBTParser.itemstack));
		});
		compound.setTag("Items", list);
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
		for(TaskTracker<ItemStack> t : this.items)
		{
			s+=(first?"":", ")+I18n.format(t.getTask().getUnlocalizedName()+".name")+" ("+t.getAmount()+")";
			first=false;
		}
		return s;
	}
	
	@Override
	public String rewardDesc()
	{
		String s = "";
		boolean first = true;
		for(ItemStack stack : this.rewards)
		{
			s+=(first?"":", ")+I18n.format(stack.getUnlocalizedName()+".name")+" ("+stack.getCount()+")";
			first=false;
		}
		return "Rewards: "+s;
	}

	@Override
	public String toString()
	{
		return this.objDesc()+";"+this.rewardDesc();
	}
}
