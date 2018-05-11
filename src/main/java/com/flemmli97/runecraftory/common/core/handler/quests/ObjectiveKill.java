package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;

public class ObjectiveKill implements IObjective{

	private String mob;
	private int amount, progress, money;
	private List<ItemStack> rewards = new ArrayList<ItemStack>();
	
	public ObjectiveKill()
	{

	}
	
	public ObjectiveKill(Class<? extends EntityCreature> mob, int amount, int money, ItemStack... rewards)
	{
		this.mob=EntityList.getKey(mob).toString();
		this.amount=amount;
		this.money=money;
		for(ItemStack stack : rewards)
			this.rewards.add(stack);
	}
	@Override
	public String objGoalID()
	{
		return this.mob;
	}
	@Override
	public void updateProgress(EntityPlayer player) {
		this.progress++;
		if(this.progress==this.amount)
			player.sendMessage(new TextComponentString(TextFormatting.GOLD + "Quest complete"));
	}
	@Override
	public boolean isFinished()
	{
		return this.progress>=this.amount;
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
		this.mob=compound.getString("Entity");
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
		compound.setString("Entity", this.mob);
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
		return "Defeat " + this.amount + " " + I18n.format("entity." + EntityList.getTranslationName(new ResourceLocation(this.mob)) + ".name");
	}
}
