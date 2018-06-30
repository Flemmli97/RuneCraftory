package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class ObjectiveShip implements IObjective{

	private ItemStack crop;
	private int shipAmount, shipProgress, money;
	private List<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public ObjectiveShip()
	{

	}
	
	public ObjectiveShip(ItemStack crop, int shipAmount, int shipReward, ItemStack... rewards)
	{
		this.crop=crop;
		this.shipAmount=shipAmount;
		this.money=shipReward;
		for(ItemStack stack : rewards)
			this.rewards.add(stack);
	}
	@Override
	public void updateProgress(EntityPlayer player) {
		this.shipProgress=Math.min(++this.shipProgress,this.shipAmount);		
	}
	
	@Override
	public String objGoalID()
	{
		return this.crop.getItem().getRegistryName().toString()+"@"+this.crop.getMetadata();
	}
	@Override
	public boolean isFinished()
	{
		return this.shipProgress==this.shipAmount;
	}

	@Override
	public int currentProgress() {
		return this.shipProgress;
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
		
		this.crop = new ItemStack(compound.getCompoundTag("Ship"));
		this.money=compound.getInteger("Money");
		this.shipAmount=compound.getInteger("Amount");
		this.shipProgress=compound.getInteger("Progress");
		NBTTagList list = compound.getTagList("Rewards", Constants.NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); i++)
		{
			this.rewards.add(new ItemStack(list.getCompoundTagAt(i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("Ship", this.crop.writeToNBT(new NBTTagCompound()));
		compound.setInteger("Money", this.money);
		compound.setInteger("Amount", this.shipAmount);
		compound.setInteger("Progress", this.shipProgress);
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
		return "Ship " + this.shipAmount + " " + I18n.format(this.crop.getItem().getUnlocalizedName());
	}
}
