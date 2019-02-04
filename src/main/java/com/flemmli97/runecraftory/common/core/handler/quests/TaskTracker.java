package com.flemmli97.runecraftory.common.core.handler.quests;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class TaskTracker<T> {

	private T t;
	private int amount;
	private int progress;
	
	public TaskTracker(T t, int amount)
	{
		this.t=t;
		this.amount=amount;
	}
	
	public TaskTracker(NBTTagCompound tag, NBTParser<T> parser) {
		this.readFromNBT(tag, parser);
	}

	public int getAmount()
	{
		return this.amount;
	}
	
	public int progress()
	{
		return this.progress;
	}
	
	public void update()
	{
		this.progress++;
	}
	
	public T getTask()
	{
		return this.t;
	}
	
	public boolean finished()
	{
		return this.progress>=this.amount;
	}
	
	public NBTTagCompound writeToNBT(NBTParser<T> parser)
	{
		NBTTagCompound tag = new NBTTagCompound();
		parser.addToNBT(this.t, tag);
		tag.setInteger("Amount", this.amount);
		tag.setInteger("Progress", this.progress);
		return tag;
	}
	
	public void readFromNBT(NBTTagCompound compound, NBTParser<T> parser)
	{
		this.t=parser.fromNBT(compound);
		this.amount=compound.getInteger("Amount");
		this.progress=compound.getInteger("Progress");
	}
	
	@Override
	public String toString()
	{
		return "Task:" + this.t + ",Amount:"+this.amount+",Progress"+this.progress;
	}
	
	public static interface NBTParser<T>
	{
		public static final NBTParser<ResourceLocation> id = new NBTParser<ResourceLocation>() {
			@Override
			public void addToNBT(ResourceLocation res, NBTTagCompound compound) {compound.setString("ID", res.toString());}
			@Override
			public ResourceLocation fromNBT(NBTTagCompound compound) {return new ResourceLocation(compound.getString("ID"));}};
			
		public static final NBTParser<ItemStack> itemstack = new NBTParser<ItemStack>() {
			@Override
			public void addToNBT(ItemStack stack, NBTTagCompound compound) {compound.setTag("Stack", stack.writeToNBT(new NBTTagCompound()));}
			@Override
			public ItemStack fromNBT(NBTTagCompound compound) {return new ItemStack(compound.getCompoundTag("Stack"));}};
		
		public void addToNBT(T t, NBTTagCompound compound);
		
		public T fromNBT(NBTTagCompound compound);
	}
}
