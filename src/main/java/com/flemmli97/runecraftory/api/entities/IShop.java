package com.flemmli97.runecraftory.api.entities;

import com.flemmli97.runecraftory.common.entity.npc.EntityNPCShopOwner;
import com.flemmli97.runecraftory.common.lib.enums.EnumShopResult;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IShop
{
	public NonNullList<ItemStack> shopItems();
    
	/**
	 * Used in a packet update
	 */
    @SideOnly(Side.CLIENT)
    public void updateShopItems(NonNullList<ItemStack> list);
    
    public EntityNPCShopOwner shopOwner();
    
    public String purchase(EnumShopResult result);
}
