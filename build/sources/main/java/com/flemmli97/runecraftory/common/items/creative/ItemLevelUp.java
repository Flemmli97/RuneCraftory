package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.RFCalculations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemLevelUp extends Item{
			
	public ItemLevelUp()
    {
		super();
		setUnlocalizedName("level_item");
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "level_item"));
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
		if(!world.isRemote)
		{
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			capSync.addXp(player, RFCalculations.xpAmountForLevelUp(capSync.getPlayerLevel()[0]));
		}
		return super.onItemRightClick(world, player, handIn);
	}
	
	
}