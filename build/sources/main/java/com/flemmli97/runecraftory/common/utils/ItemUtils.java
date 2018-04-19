package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.common.init.ModItems;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUtils {
	
	public static void starterItems(EntityPlayer player)
	{
		ItemStack broadSword = new ItemStack(ModItems.broadSword);
		ItemStack hammer = new ItemStack(ModItems.hammerScrap);
		spawnItemAtEntity(player, broadSword);
		spawnItemAtEntity(player, hammer);
	}

	public static void spawnItemAtEntity(EntityLivingBase entity, ItemStack stack)
	{
		spawnItemAt(entity.world, entity.getPosition(), stack);
	}
	
	public static void spawnItemAt(World world, BlockPos pos, ItemStack stack)
	{
		if(!world.isRemote)
		{
			if(stack.getItem() instanceof IItemBase)
				ItemNBT.initItemNBT(stack, ((IItemBase)stack.getItem()).defaultNBTStats(stack));
			EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);
			item.setPickupDelay(0);
			world.spawnEntity(item);
		}
	}
	
	public static void spawnLeveledItem(EntityLivingBase entity, ItemStack stack, int level)
	{
		if(!entity.world.isRemote)
		{
			if(stack.getItem() instanceof IItemBase)
			{
				ItemNBT.initItemNBT(stack, ((IItemBase)stack.getItem()).defaultNBTStats(stack));
				if(stack.hasTagCompound())
					stack.getTagCompound().setInteger("ItemLevel", level);
			}
			EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack);
			item.setPickupDelay(0);
			entity.world.spawnEntity(item);
		} 
	}
}
