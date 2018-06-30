package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ObjectiveHarvest implements IObjective{

	private ItemStack crop;
	private int harvestAmount, harvestProgress, money;
	private List<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public ObjectiveHarvest()
	{

	}
	
	public ObjectiveHarvest(ItemStack crop, int harvestAmount, int moneyReward, ItemStack... rewards)
	{
		this.crop=crop;
		this.harvestAmount=harvestAmount;
		this.money=moneyReward;
		for(ItemStack stack : rewards)
			this.rewards.add(stack);
	}
	@Override
	public void updateProgress(EntityPlayer player) {
		this.harvestProgress=Math.min(++this.harvestProgress,this.harvestAmount);		
	}
	
	@Override
	public String objGoalID()
	{
		return this.crop.getItem().getRegistryName().toString()+"@"+this.crop.getMetadata();
	}
	@Override
	public boolean isFinished()
	{
		return this.harvestProgress==this.harvestAmount;
	}

	@Override
	public int currentProgress() {
		return this.harvestProgress;
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
		
		this.crop = new ItemStack(compound.getCompoundTag("Harvest"));
		this.money=compound.getInteger("Money");
		this.harvestAmount=compound.getInteger("Amount");
		this.harvestProgress=compound.getInteger("Progress");
		NBTTagList list = compound.getTagList("Rewards", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			this.rewards.add(new ItemStack(list.getCompoundTagAt(i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Harvest", this.crop.writeToNBT(new NBTTagCompound()));
		compound.setInteger("Money", this.money);
		compound.setInteger("Amount", this.harvestAmount);
		compound.setInteger("Progress", this.harvestProgress);
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
		return "Harvest " + this.harvestAmount + " " + I18n.format(this.crop.getItem().getUnlocalizedName());
	}
}
