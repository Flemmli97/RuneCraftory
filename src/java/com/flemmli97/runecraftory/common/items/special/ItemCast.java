package com.flemmli97.runecraftory.common.items.special;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.enums.EnumSkills;
import com.flemmli97.runecraftory.api.enums.EnumWeaponType;
import com.flemmli97.runecraftory.api.items.IItemBase;
import com.flemmli97.runecraftory.api.items.IRpUseItem;
import com.flemmli97.runecraftory.common.lib.RFReference;

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

public abstract class ItemCast extends Item implements IItemBase, IRpUseItem{

	private int level=1;
	
	public ItemCast(String name) {
		super();
        this.setMaxStackSize(1);
        this.setCreativeTab(RuneCraftory.cast);
        this.setRegistryName(new ResourceLocation(RFReference.MODID, name));	
        this.setUnlocalizedName(this.getRegistryName().toString());
	}
	
	@Override
	public void levelSkill(EntityPlayer player, int amount, EnumSkills skill) {
		
	}

	@Override
	public int[] getChargeTime() {
		return null;
	}

	@Override
	public EnumWeaponType getWeaponType() {
		return EnumWeaponType.CAST;
	}

	@Override
	public int itemCoolDownTicks() {
		return 0;
	}

	@Override
	public int getUpgradeDifficulty(ItemStack stack) {
		return 0;
	}

	@Override
	public int itemLevel() {
		return level;
	}

	@Override
	public void addItemLevel() {
		if(level<10)
			level++;
	}
	
	protected abstract boolean cast(World world, EntityPlayer player, EnumHand hand);

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		
		return this.cast(world, player, hand) ? new ActionResult<ItemStack>(EnumActionResult.SUCCESS, player.getHeldItem(hand)):new ActionResult<ItemStack>(EnumActionResult.FAIL, player.getHeldItem(hand));
	}
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));		
	}
}
