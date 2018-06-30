package com.flemmli97.runecraftory.common.utils;

import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemUtils
{
	/**
	 * Gives a player the "starter kit"
	 */
    public static void starterItems(EntityPlayer player) 
    {
        ItemStack broadSword = new ItemStack(ModItems.broadSword);
        ItemStack hammer = new ItemStack(ModItems.hammerScrap);
        spawnItemAtEntity(player, broadSword);
        spawnItemAtEntity(player, hammer);
    }
    
    public static void spawnItemAtEntity(EntityLivingBase entity, ItemStack stack) {
        spawnItemAt(entity.world, entity.getPosition(), stack);
    }
    
    public static void spawnItemAt(World world, BlockPos pos, ItemStack stack) {
        if (!world.isRemote) {
            ItemNBT.initNBT(stack);
            EntityItem item = new EntityItem(world, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), stack);
            item.setPickupDelay(0);
            world.spawnEntity((Entity)item);
        }
    }
    
    public static void spawnLeveledItem(EntityLivingBase entity, ItemStack stack, int level) {
        if (!entity.world.isRemote) {
            NBTTagCompound compound = ItemNBT.getItemNBT(stack);
            if (compound != null) {
                compound.setInteger("ItemLevel", level);
            }
            EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY, entity.posZ, stack);
            item.setPickupDelay(0);
            entity.world.spawnEntity((Entity)item);
        }
    }
    
    public static int getSellPrice(ItemStack stack) {
        ItemStat price = ItemStatMap.get(stack);
        if (price != null) {
            return price.getSell() * ItemNBT.itemLevel(stack);
        }
        return 0;
    }
    
    public static int getBuyPrice(ItemStack stack) {
        ItemStat price = ItemStatMap.get(stack);
        if (price != null) {
            return price.getBuy();
        }
        return 0;
    }
    
    public static EnumShopResult buyItem(EntityPlayer player, ItemStack stack) {
        IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
        if(cap!=null)
        {
	        if (!hasSpace(player, stack)) {
	            return EnumShopResult.NOSPACE;
	        }
	        int price = getBuyPrice(stack) * stack.getCount();
	        if (cap.useMoney(player, price)) {
	            player.playSound(SoundEvents.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
	            while (stack.getCount() > 0) {
	                ItemStack copy = stack.copy();
	                int count = (stack.getCount() > stack.getMaxStackSize()) ? stack.getMaxStackSize() : stack.getCount();
	                copy.setCount(count);
	                spawnItemAtEntity((EntityLivingBase)player, copy);
	                stack.setCount(stack.getCount() - count);
	            }
	            return EnumShopResult.SUCCESS;
	        }
        }
        return EnumShopResult.NOMONEY;
    }
    
    /**
     * Tests, if the players inventory has enough space for the itemstack without actually adding it to the inventory
     */
    public static boolean hasSpace(EntityPlayer player, ItemStack stack) 
    {
        if (stack.isEmpty()) 
        {
            return false;
        }
        InventoryPlayer inv = player.inventory;
        stack = stack.copy();
        for (ItemStack invStack : inv.mainInventory) 
        {
            if (invStack.isEmpty()) 
            {
                stack.setCount(stack.getCount() - stack.getMaxStackSize());
            }
            else if (invStack.getCount() < invStack.getMaxStackSize() && areItemsStackable(stack, invStack)) 
            {
                int sub = invStack.getMaxStackSize() - invStack.getCount();
                stack.setCount(stack.getCount() - sub);
            }
            if (stack.getCount() <= 0) 
            {
                break;
            }
        }
        return stack.getCount() <= 0;
    }
    
    public static boolean areItemsStackable(ItemStack stack1, ItemStack stack2) 
    {
        return (stack1.isEmpty() && stack2.isEmpty()) || (!stack1.isEmpty() && !stack2.isEmpty() && stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata() && ((!stack1.hasTagCompound() && !stack2.hasTagCompound()) || (stack1.hasTagCompound() && (stack1.getTagCompound().equals((Object)stack2.getTagCompound()) && stack1.areCapsCompatible(stack2)))));
    }
}
