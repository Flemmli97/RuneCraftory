package com.flemmli97.runecraftory.common.items;

import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemDebug extends Item{
			
	public ItemDebug()
    {
		super();
		setUnlocalizedName("debug_item");
		this.setRegistryName(new ResourceLocation(RFReference.MODID, "debug_item"));
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {

		return super.onItemRightClick(world, player, handIn);
	}
	
	
}