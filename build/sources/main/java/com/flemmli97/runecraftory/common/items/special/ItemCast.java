package com.flemmli97.runecraftory.common.items.special;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.items.ISpells;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumSkills;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemCast extends Item implements ISpells{
	
	public ItemCast(String name) {
		super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.cast);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, name));	
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill) {
		
	}

	@Override
	public int getUpgradeDifficulty() {
		return 0;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		return this.use(world, player) ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand)):new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}
}
