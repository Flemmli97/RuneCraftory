package com.flemmli97.runecraftory.api.entities;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IShop
{
	public NonNullList<ItemStack> shopItems();
    
    public void updateShopItems(NonNullList<ItemStack> list);
    
    public EntityNPCShopOwner shopOwner();
    
    public String purchase(EnumShopResult result);
}
