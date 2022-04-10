package com.flemmli97.runecraftory.common.items.special;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.ISpells;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class ItemCast extends Item implements ISpells{
	
	public ItemCast(String name) {
		super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.cast);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));	
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public void update(ItemStack stack, EntityPlayer player) {}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		if(player.getCooldownTracker().getCooldown(this, 0)<=0 && this.use(world, player, player.getHeldItem(hand)))
		{
			this.levelSkill(player);
			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
	}
}