package com.flemmli97.runecraftory.common.items.creative;

import java.util.Random;

import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.world.StructureLoader;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;

public class ItemDebug extends Item{
			
	public ItemDebug()
    {
		super();
		setUnlocalizedName("debug_item");
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "debug_item"));
    }

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand handIn) {
		if(!world.isRemote)
		{
			/*ItemStack stack = new ItemStack(ModItems.recipe);
			IPlayer capSync = player.getCapability(PlayerCapProvider.PlayerCap, null);
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("Recipe", CraftingHandler.randomRecipeToExclude(EnumCrafting.FORGE, 0, capSync.getSkillLevel(EnumSkills.FORGING)[0], 0));
			stack.setItemDamage(EnumCrafting.FORGE.getID());
			ItemUtils.spawnItemAtEntity(player, stack);*/

		}
		return super.onItemRightClick(world, player, handIn);
	}
	
	
}