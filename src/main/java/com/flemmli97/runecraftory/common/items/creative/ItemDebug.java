package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDebug extends Item implements IModelRegister{
			
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
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}