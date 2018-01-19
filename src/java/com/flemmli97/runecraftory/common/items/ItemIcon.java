package com.flemmli97.runecraftory.common.items;

import com.flemmli97.runecraftory.common.lib.RFReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemIcon extends Item{
	public ItemIcon()
    {
		super();
		setUnlocalizedName("icon");
		this.setRegistryName(new ResourceLocation(RFReference.MODID, "icon"));
    		this.setHasSubtypes(true);
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		for(int meta = 0; meta < 6;meta++)
			ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(getRegistryName()+":"+meta, "inventory"));		
	}
}
