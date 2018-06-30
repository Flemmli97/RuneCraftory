package com.flemmli97.runecraftory.common.items.creative;

import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIcon extends Item implements IModelRegister{
	public ItemIcon()
    {
		super();
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "icon"));
		this.setUnlocalizedName(this.getRegistryName().toString());
		this.setHasSubtypes(true);
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 3;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+"_"+meta, "inventory"));
	}
}
