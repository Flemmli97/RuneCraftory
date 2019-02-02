package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.init.ModItems;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.utils.ItemUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ItemDebug extends Item{
			
	public ItemDebug()
    {
		super();
		this.setUnlocalizedName("debug_item");
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "debug_item"));
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
		if(!world.isRemote)
		{
			ItemStack stack = new ItemStack(ModItems.recipe);
			//IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Recipe", CraftingHandler.randomRecipeToExclude(EnumCrafting.FORGE, 0, 100));
			stack.setItemDamage(EnumCrafting.FORGE.getID());
			ItemUtils.spawnItemAtEntity(player, stack);
		}
		return super.onItemRightClick(world, player, handIn);
	}
}