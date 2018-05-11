package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemInstaTame extends Item implements IModelRegister{
	public ItemInstaTame()
    {
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "insta_tame"));
		this.setUnlocalizedName(this.getRegistryName().toString());
    	this.setHasSubtypes(true);
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
			ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
