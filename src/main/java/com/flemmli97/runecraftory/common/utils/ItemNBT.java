package com.flemmli97.runecraftory.common.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.api.items.IItemUsable;
import com.flemmli97.runecraftory.api.items.IItemWearable;
import com.flemmli97.runecraftory.api.items.ItemStat;
import com.flemmli97.runecraftory.api.items.ItemStatAttributes;
import com.flemmli97.runecraftory.api.mappings.ItemStatMap;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumElement;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

public class ItemNBT
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    
    public static int itemLevel(ItemStack stack) 
    {
        NBTTagCompound tag = getItemNBT(stack);
        return tag != null ? tag.getInteger("ItemLevel") : 1;
    }
    
    public static boolean addItemLevel(ItemStack stack) 
    {
        if (itemLevel(stack) < 10) 
        {
            NBTTagCompound tag = getItemNBT(stack);
            if (tag != null) 
            {
                tag.setInteger("ItemLevel", tag.getInteger("ItemLevel") + 1);
                return true;
            }
        }
        return false;
    }
    
    public static Map<IAttribute, Integer> statIncrease(ItemStack stack) 
    {
        Map<IAttribute, Integer> map = Maps.newLinkedHashMap();
        if (stack.getItem() instanceof IItemUsable) 
        {
            NBTTagCompound compound = getItemNBT(stack);
            if (compound != null) 
            {
                NBTTagCompound tag = compound.getCompoundTag("ItemStats");
                List<ItemStatAttributes> ordered = new LinkedList<ItemStatAttributes>();
                for (String attName : tag.getKeySet()) 
                {
                	//No check for health since not allowed
                    ordered.add(ItemStatAttributes.ATTRIBUTESTRINGMAP.get(attName));
                }
                ordered.sort(new ItemStatAttributes.Sort());
                for (ItemStatAttributes att : ordered) 
                {
                    map.put(att, tag.getInteger(att.getName()));
                }
            }
        }
        else 
        {
            ItemStat stat = ItemStatMap.get(stack);
            if (stat != null) 
            {
            	return stat.itemStats();
            }
        }
        return map;
    }
    
    public static void updateStatIncrease(IAttribute attribute, int amount, NBTTagCompound tag) 
    {
        int oldValue = tag.getCompoundTag("ItemStats").getInteger(attribute.getName());
        tag.getCompoundTag("ItemStats").setInteger(attribute.getName(), oldValue += amount);
    }
    
    public static void setElement(EnumElement element, ItemStack stack) 
    {
        NBTTagCompound tag = getItemNBT(stack);
        if (tag != null) 
        {
            if (EnumElement.fromName(tag.getString("Element")) == EnumElement.NONE) 
            {
                tag.setString("Element", element.getName());
            }
            else 
            {
                tag.setString("Element", EnumElement.NONE.getName());
            }
        }
    }
    
    public static EnumElement getElement(ItemStack stack) 
    {
        NBTTagCompound tag = getItemNBT(stack);
        if (tag != null) 
        {
            return EnumElement.fromName(tag.getString("Element"));
        }
        return EnumElement.NONE;
    }
    
    public static List<ItemStack> upgradeItems(ItemStack stack) 
    {
        NBTTagCompound tag = getItemNBT(stack);
        List<ItemStack> list = new ArrayList<ItemStack>();
        if (tag != null) 
        {
            NBTTagList nbtList = tag.getTagList("Upgrades", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < nbtList.tagCount(); ++i) 
            {
                NBTTagCompound nbttagcompound = nbtList.getCompoundTagAt(i);
                list.add(new ItemStack(nbttagcompound));
            }
        }
        return list;
    }
    
    public static void addUpgradeItem(ItemStack stack, ItemStack stackToAdd) 
    {
        NBTTagCompound tag = getItemNBT(stack);
        if (tag != null) 
        {
            float efficiency = 1.0f;
            float similar = 1.0f;
            NBTTagList nbtList = tag.getTagList("Upgrades", Constants.NBT.TAG_COMPOUND);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            //Searches for items, which are already applied to the itemstack. Reduces the efficiency for each identical item found.
            if (nbtList.tagCount() > 0) {
                for (int i = 0; i < nbtList.tagCount(); ++i) 
                {
                    ItemStack read = new ItemStack(nbtList.getCompoundTagAt(i));
                    if (ItemStack.areItemsEqual(read, stackToAdd)) 
                    {
                        efficiency = (float)Math.max(0.0, efficiency - Math.max(0.25, 0.5 / similar));
                        ++similar;
                    }
                }
            }
            nbtList.appendTag(writeItemNBTRaw(stackToAdd, nbttagcompound));
            tag.setTag("Upgrades", nbtList);
            if (!(stackToAdd.getItem() instanceof IItemWearable)) 
            {
                ItemStat stat = ItemStatMap.get(stackToAdd);
                if (stat != null) 
                {
                    for (Entry<IAttribute, Integer> entry : stat.itemStats().entrySet()) 
                    {
                        updateStatIncrease(entry.getKey(), Math.round(entry.getValue() * efficiency), tag);
                    }
                }
            }
        }
    }
    
    /**
     * Only writes item and meta to nbt.
     */
    public static NBTTagCompound writeItemNBTRaw(ItemStack stack, NBTTagCompound nbt) 
    {
        ResourceLocation resourcelocation = Item.REGISTRY.getNameForObject(stack.getItem());
        nbt.setString("id", (resourcelocation == null) ? "minecraft:air" : resourcelocation.toString());
        nbt.setByte("Count", (byte)1);
        nbt.setShort("Damage", (short)stack.getMetadata());
        return nbt;
    }
    
    /**
     * Gets an nbttagcompound from this itemstack if possible used for various stuffs from this mod
     */
    @Nullable
    public static NBTTagCompound getItemNBT(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(LibReference.MODID)) {
            return stack.getTagCompound().getCompoundTag(LibReference.MODID);
        }
        if (initNBT(stack)) {
            return stack.getTagCompound().getCompoundTag(LibReference.MODID);
        }
        return null;
    }
    
    public static boolean initNBT(ItemStack stack) 
    {
        return initNBT(stack, false);
    }
    
    public static boolean initNBT(ItemStack stack, boolean forced) 
    {
    	 ItemStat stat = ItemStatMap.get(stack);
         if (stat != null || forced) 
         {
             NBTTagCompound stackTag = stack.getTagCompound();
             if (stackTag == null) 
             {
                 stackTag = new NBTTagCompound();
             }
             NBTTagCompound compound = new NBTTagCompound();
             compound.setInteger("ItemLevel", 1);
             if (stack.getItem() instanceof IItemWearable) 
             {
                 compound.setTag("Upgrades", new NBTTagList());
                 if(stat!=null)
                 {
	                 NBTTagCompound stats = new NBTTagCompound();
	                 for (Entry<IAttribute, Integer> entry : stat.itemStats().entrySet()) 
	                 {
	                     stats.setInteger(entry.getKey().getName(), entry.getValue());
	                 }
	                 compound.setTag("ItemStats", stats);
	                 if (stack.getItem() instanceof IItemUsable) 
	                 {
	                     compound.setString("Element", stat.element().getName());
	                 }
                 }
             }
             stackTag.setTag(LibReference.MODID, compound);
             stack.setTagCompound(stackTag);
             return true;
         }
         return false;
    }
    /**
     * From Forges CraftingHandler, but returns the itemstacks stackTagCompound
     */
    public NBTTagCompound stackCompoundFromJson(JsonObject json) 
    {
        if (json.has("nbt")) 
        {
            try 
            {
                JsonElement element = json.get("nbt");
                NBTTagCompound nbt;
                if (element.isJsonObject()) 
                {
                    nbt = JsonToNBT.getTagFromJson(ItemNBT.GSON.toJson(element));
                }
                else 
                {
                    nbt = JsonToNBT.getTagFromJson(element.getAsString());
                }
                NBTTagCompound tmp = new NBTTagCompound();
                if (nbt.hasKey("ForgeCaps")) 
                {
                    tmp.setTag("ForgeCaps", nbt.getTag("ForgeCaps"));
                    nbt.removeTag("ForgeCaps");
                }
                tmp.setTag("tag", nbt);
                return tmp;
            }
            catch (NBTException e) {
                throw new JsonSyntaxException("Invalid NBT Entry: " + e.toString());
            }
        }
        return null;
    }
}
