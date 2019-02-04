package com.flemmli97.runecraftory.api.mappings;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.core.handler.quests.LevelFunction;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveBring;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveHarvest;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveKill;
import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveShip;
import com.flemmli97.runecraftory.common.core.handler.quests.TaskTracker;
import com.flemmli97.runecraftory.common.core.handler.quests.WeightedTable.Difficulty;
import com.flemmli97.runecraftory.common.core.handler.quests.WeightedTable.ValueHelper;
import com.flemmli97.runecraftory.common.core.handler.quests.WeightedTable.WeightedItemStackTable;
import com.flemmli97.runecraftory.common.core.handler.quests.WeightedTable.WeightedResourceTable;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.tenshilib.api.config.ExtendedItemStackWrapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;

public class Quests
{
    private static WeightedItemStackTable killRewards = new WeightedItemStackTable();
    private static WeightedItemStackTable harvestRewards = new WeightedItemStackTable();
    private static WeightedItemStackTable bringRewards = new WeightedItemStackTable();
    private static WeightedItemStackTable shipRewards = new WeightedItemStackTable();
    
    private static WeightedResourceTable killTask = new WeightedResourceTable();
    private static WeightedItemStackTable harvestTask = new WeightedItemStackTable();
    private static WeightedItemStackTable bringTask = new WeightedItemStackTable();
    private static WeightedItemStackTable shipTask = new WeightedItemStackTable();

	public static void loadQuests(Gson gson, JsonObject obj)
    {
    	JsonObject killReward = obj.get("killQuestRewards").getAsJsonObject();
    	killRewards = gson.fromJson(killReward, WeightedItemStackTable.class);
    	JsonObject killQuests = obj.get("killQuests").getAsJsonObject();
    	killTask = gson.fromJson(killQuests, WeightedResourceTable.class);
    	JsonObject harvestReward = obj.get("harvestQuestRewards").getAsJsonObject();
    	harvestRewards = gson.fromJson(harvestReward, WeightedItemStackTable.class);
    	JsonObject harvestQuests = obj.get("harvestQuests").getAsJsonObject();
    	harvestTask = gson.fromJson(harvestQuests, WeightedItemStackTable.class);
    	JsonObject deliverReward = obj.get("deliverQuestRewards").getAsJsonObject();
    	bringRewards = gson.fromJson(deliverReward, WeightedItemStackTable.class);
    	JsonObject deliverQuests = obj.get("deliverQuests").getAsJsonObject();
    	bringTask = gson.fromJson(deliverQuests, WeightedItemStackTable.class);
    	JsonObject shippingReward = obj.get("shippingQuestRewards").getAsJsonObject();
    	shipRewards = gson.fromJson(shippingReward, WeightedItemStackTable.class);
    	JsonObject shippingQuests = obj.get("shippingQuests").getAsJsonObject();
    	shipTask = gson.fromJson(shippingQuests, WeightedItemStackTable.class);
    	int quests = killTask.quests()+harvestTask.quests()+bringTask.quests()+shipTask.quests();
    	int rewards = killRewards.quests()+harvestRewards.quests()+bringRewards.quests()+shipRewards.quests();
		LibReference.logger.info("Loaded {} quests and {} rewards", quests, rewards);
    }
    
    @Nullable
	public static ObjectiveKill randomKillObjective(Random rand, WorldServer world, Difficulty difficulty) 
    {
    	List<TaskTracker<ResourceLocation>> entities = Lists.newArrayList();
    	int money = 0;
    	for(ValueHelper<ResourceLocation> val : killTask.get(rand, world, difficulty))
    	{	
            Class<? extends Entity> entity = EntityList.getClass(val.get());
            if (EntityLiving.class.isAssignableFrom(entity)) 
            {
            	entities.add(new TaskTracker<ResourceLocation>(val.get(), val.amount()));
            	money+=val.money();
            }
    	}
    	List<ItemStack> reward = Lists.newArrayList();
    	LootContext ctx = new LootContext.Builder(world).build();
    	for(ValueHelper<ExtendedItemStackWrapper> val : killRewards.get(rand, world, difficulty))
    	{
    		ItemStack stack = val.get().getStack();
    		if(val.getFunctions()!=null)
    			for(LootFunction f : val.getFunctions())
    				f.apply(stack, rand, ctx);
    		reward.add(stack);
    		
    	}
    	if(!entities.isEmpty())
    		return new ObjectiveKill(entities, money, reward);
    	return null;
    }
    
    @Nullable
	public static ObjectiveHarvest randomHarvestObjective(Random rand, WorldServer world, Difficulty difficulty) 
    {
    	List<TaskTracker<ItemStack>> entities = Lists.newArrayList();
    	int money = 0;
    	for(ValueHelper<ExtendedItemStackWrapper> val : harvestTask.get(rand, world, difficulty))
    	{	
        	entities.add(new TaskTracker<ItemStack>(val.get().getStack(), val.amount()));
        	money+=val.money();
    	}
    	List<ItemStack> reward = Lists.newArrayList();
    	LootContext ctx = new LootContext.Builder(world).build();
    	for(ValueHelper<ExtendedItemStackWrapper> val : harvestRewards.get(rand, world, difficulty))
    	{
    		ItemStack stack = val.get().getStack();
    		if(val.getFunctions()!=null)
    			for(LootFunction f : val.getFunctions())
    				f.apply(stack, rand, ctx);
    		reward.add(stack);
    		
    	}
    	if(!entities.isEmpty())
    		return new ObjectiveHarvest(entities, money, reward);
    	return null;
    }
    
    @Nullable
	public static ObjectiveShip randomShippingObjective(Random rand, WorldServer world, Difficulty difficulty) 
    {
    	List<TaskTracker<ItemStack>> entities = Lists.newArrayList();
    	int money = 0;
    	for(ValueHelper<ExtendedItemStackWrapper> val : shipTask.get(rand, world, difficulty))
    	{	
        	entities.add(new TaskTracker<ItemStack>(val.get().getStack(), val.amount()));
        	money+=val.money();
    	}
    	List<ItemStack> reward = Lists.newArrayList();
    	LootContext ctx = new LootContext.Builder(world).build();
    	for(ValueHelper<ExtendedItemStackWrapper> val : shipRewards.get(rand, world, difficulty))
    	{
    		ItemStack stack = val.get().getStack();
    		if(val.getFunctions()!=null)
    			for(LootFunction f : val.getFunctions())
    				f.apply(stack, rand, ctx);
    		reward.add(stack);
    		
    	}
    	if(!entities.isEmpty())
    		return new ObjectiveShip(entities, money, reward);
    	return null;
    }
    
    @Nullable
	public static ObjectiveBring randomDeliverObjective(Random rand, WorldServer world, Difficulty difficulty) 
    {
    	List<TaskTracker<ItemStack>> entities = Lists.newArrayList();
    	int money = 0;
    	for(ValueHelper<ExtendedItemStackWrapper> val : bringTask.get(rand, world, difficulty))
    	{	
        	entities.add(new TaskTracker<ItemStack>(val.get().getStack(), val.amount()));
        	money+=val.money();
    	}
    	List<ItemStack> reward = Lists.newArrayList();
    	LootContext ctx = new LootContext.Builder(world).build();
    	for(ValueHelper<ExtendedItemStackWrapper> val : bringRewards.get(rand, world, difficulty))
    	{
    		ItemStack stack = val.get().getStack();
    		if(val.getFunctions()!=null)
    			for(LootFunction f : val.getFunctions())
    				f.apply(stack, rand, ctx);
    		reward.add(stack);
    		
    	}
    	if(!entities.isEmpty())
    		return new ObjectiveBring(entities, money, reward);
    	return null;
    }
	
	static
	{
		LootFunctionManager.registerFunction(new LevelFunction.Serializer());
	}
}
