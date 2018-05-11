package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ObjectiveBring implements IObjective{

	private ItemStack crop;
	private int amount, progress, money;
	private List<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public ObjectiveBring()
	{

	}
	
	public ObjectiveBring(ItemStack crop, int harvestAmount, int moneyReward, ItemStack... rewards)
	{
		this.crop=crop;
		this.amount=harvestAmount;
		this.money=moneyReward;
		for(ItemStack stack : rewards)
			this.rewards.add(stack);
	}
	@Override
	public void updateProgress(EntityPlayer player) {
		this.progress=Math.min(++this.progress,this.amount);		
	}
	
	@Override
	public String objGoalID()
	{
		return this.crop.getItem().getRegistryName().toString()+"@"+this.crop.getMetadata();
	}
	@Override
	public boolean isFinished()
	{
		return this.progress==this.amount;
	}

	@Override
	public int currentProgress() {
		return this.progress;
	}

	@Override
	public List<ItemStack> rewards() {
		return rewards;
	}

	@Override
	public int moneyReward() {
		return money;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		
		this.crop = new ItemStack(compound.getCompoundTag("Bring"));
		this.money=compound.getInteger("Money");
		this.amount=compound.getInteger("Amount");
		this.progress=compound.getInteger("Progress");
		NBTTagList list = compound.getTagList("Rewards", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			this.rewards.add(new ItemStack(list.getCompoundTagAt(i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Bring", this.crop.writeToNBT(new NBTTagCompound()));
		compound.setInteger("Money", this.money);
		compound.setInteger("Amount", this.amount);
		compound.setInteger("Progress", this.progress);
		NBTTagList list = new NBTTagList();
		for(ItemStack stack : this.rewards)
		{
			list.appendTag(stack.writeToNBT(new NBTTagCompound()));
		}
		compound.setTag("Rewards", list);
		return compound;
	}
	@Override
	public String objDesc()
	{
		return "Bring " + this.amount + " " + I18n.format(this.crop.getItem().getUnlocalizedName());
	}
}
