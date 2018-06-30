package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.entity.EntityMobBase;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemEntityLevelUp extends Item implements IModelRegister{
			
	public ItemEntityLevelUp()
    {
		super();
		setUnlocalizedName("entity_level_item");
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "entity_level_item"));
    }
	
	@Override
    public boolean itemInteractionForEntity(ItemStack itemstack, net.minecraft.entity.player.EntityPlayer player, EntityLivingBase entity, net.minecraft.util.EnumHand hand)
    {
		if (entity.world.isRemote)
        {
            return true;
        }
		if(entity instanceof EntityMobBase)
		{
			((EntityMobBase)entity).increaseLevel();
			player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, 1, 1);
			return true;
		}
		return false;
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}