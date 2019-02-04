package com.flemmli97.runecraftory.common.core.handler.quests;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.tenshilib.api.config.ExtendedItemStackWrapper;
import com.flemmli97.tenshilib.common.javahelper.ArrayUtils;
import com.google.common.collect.Lists;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.functions.LootFunction;

public class WeightedTable<T> {

	private List<Value<T>> easy = Lists.newArrayList();
	private List<Value<T>> normal = Lists.newArrayList();
	private List<Value<T>> hard = Lists.newArrayList();
	
	public List<ValueHelper<T>> get(Random rand, WorldServer world, Difficulty diff)
	{
		List<ValueHelper<T>> l = Lists.newArrayList();
		List<Value<T>> check = null;
		switch(diff)
		{
			case EASY:
				check=easy;
				break;
			case HARD:
				check=hard;
				break;
			case NORMAL:
				check=normal;
				break;
		}
		int rolls = 1;
		int maxWeight = 0;
		for(Value<T> val : check)
			maxWeight+=val.weight;
		if(maxWeight>0)
			for(int i = 0; i < rolls; i++)
			{
				int weight = rand.nextInt(maxWeight);
				for(Value<T> val : check)
				{
					weight-=val.weight;
					if(weight<0)
						rolls+=val.get(l, rand);
				}
			}
		return l;
	}
	
	public int quests()
	{
		return this.easy.size()+this.normal.size()+this.hard.size();
	}
	
	@Override
	public String toString()
	{
		return "Easy: " + this.easy+System.lineSeparator()+"Normal: " + this.normal + System.lineSeparator() + "Hard: " + this.hard;
	}
	
	public static class Value<T>
	{
	    private T object;
	    private RandomValueRange amount;
		private int weight;
		private RandomValueRange money;
		private RandomValueRange additionalRolls;
	    private LootFunction[] functions = new LootFunction[0];

		private int get(List<ValueHelper<T>> l, Random rand)
		{
			l.add(new ValueHelper<T>(this.object, this.amount.generateInt(rand), this.money.generateInt(rand), this.functions));
			return this.additionalRolls.generateInt(rand);
		}

		@Override
		public String toString()
		{
			return "Value:" + this.object + ",Amount:"+this.amount+",Money:"+this.money+",Functions:"+ArrayUtils.arrayToString(this.functions);
		}
	}
	
	public static class ValueHelper<T>
	{
		private T t;
		private int amount;
		private int money;
		
	    protected LootFunction[] functions;

		private ValueHelper(T t, int amount, int money, LootFunction[] functions)
		{
			this.t=t;
			this.amount=amount;
			this.money=money;
			this.functions=functions;
		}
		
		public T get()
		{
			return t;
		}
		
		public int amount()
		{
			return amount;
		}
		
		public int money() {
			return money;
		}
		
		@Nullable
		public LootFunction[] getFunctions()
		{
			return this.functions;
		}
		
		@Override
		public String toString()
		{
			return "Value:" + this.t + ",Amount:"+this.amount+",Money:"+this.money+",Functions:"+ArrayUtils.arrayToString(this.functions);
		}
	}
	
	/**
	 * Since json doesnt accept generics
	 */
	public static class WeightedResourceTable extends WeightedTable<ResourceLocation>
	{
		
	}
	
	public static class WeightedItemStackTable extends WeightedTable<ExtendedItemStackWrapper>
	{
		
	}

	public static enum Difficulty
	{
		EASY,
		NORMAL,
		HARD;
	}
}
