package com.flemmli97.runecraftory.api.mappings;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.flemmli97.runecraftory.common.core.handler.quests.ObjectiveKill;
import com.flemmli97.tenshilib.api.config.SimpleItemStackWrapper;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Quests
{
    private static final List<SimpleItemStackWrapper> tier1 = Lists.newArrayList();
    private static final List<SimpleItemStackWrapper> tier2 = Lists.newArrayList();
    private static final List<SimpleItemStackWrapper> tier3 = Lists.newArrayList();
    private static final List<String> killObjectives = Lists.newArrayList();
    private static final List<String> harvestObjectives = Lists.newArrayList();
    private static final List<String> bringObjectives = Lists.newArrayList();
    private static final List<String> shipObjectives = Lists.newArrayList();
    
    @SuppressWarnings("unchecked")
	public static ObjectiveKill randomKillObjective(int tier) 
    {
        Random rand = new Random();
        String[] parts = Quests.killObjectives.get(rand.nextInt(Quests.killObjectives.size())).split(";");
        Class<? extends Entity> entity = (Class<? extends Entity>)EntityList.getClass(new ResourceLocation(parts[0]));
        if (entity.isAssignableFrom(EntityLiving.class)) 
        {
            int min = Integer.parseInt(parts[1]);
            int max = Integer.parseInt(parts[2]);
            List<ItemStack> list = new LinkedList<ItemStack>();
            int chance = 0;
            if (chance <= 0) 
            {
                addItemToList(list, tier, rand);
            }
            else 
            {
                while (rand.nextInt(chance) == 0) 
                {
                    addItemToList(list, tier, rand);
                }
            }
            return new ObjectiveKill((Class<? extends EntityCreature>)entity, rand.nextInt(max - min) + min, 0, list);
        }
        return null;
    }
    
    private static void addItemToList(List<ItemStack> list, int tier, Random rand) 
    {
        switch (tier) 
        {
            case 0:
                SimpleItemStackWrapper[] props = Quests.tier1.toArray(new SimpleItemStackWrapper[0]);
                list.add(props[rand.nextInt(props.length)].getStack());
                break;
            case 1:
                SimpleItemStackWrapper[] props2 = Quests.tier2.toArray(new SimpleItemStackWrapper[0]);
                list.add(props2[rand.nextInt(props2.length)].getStack());
                break;
            case 2:
                SimpleItemStackWrapper[] props3 = Quests.tier3.toArray(new SimpleItemStackWrapper[0]);
                list.add(props3[rand.nextInt(props3.length)].getStack());
                break;
        }
    }
}
