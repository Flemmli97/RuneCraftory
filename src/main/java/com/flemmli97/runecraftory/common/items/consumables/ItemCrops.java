package com.flemmli97.runecraftory.common.items.consumables;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemCrops extends ItemFood{

	private String oreDict;
	private boolean isGiant;
	
	public ItemCrops(String name, String oreDictName, boolean isGiant) {
		super(0, 0, false);
		this.oreDict=oreDictName;
		this.isGiant=isGiant;
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "crop_"+name+(this.isGiant?"_giant":"")));
        this.setUnlocalizedName(this.getRegistryName().toString());
		if(!this.isGiant)
		{
			this.setCreativeTab(RuneCraftory.crops);
			CropMap.addCrop(oreDictName, this);
		}
		else
		{
			CropMap.addGiantCrop(oreDictName, this);
			this.setCreativeTab(null);
		}
	}
	
	public String cropSpecificOreDict()
	{
		return this.oreDict;
	}
    
    public boolean isGiant(ItemStack stack)
    {
    	return this.isGiant;
    }
    
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return this.isGiant?(int)(1.5*super.getMaxItemUseDuration(stack)):super.getMaxItemUseDuration(stack);
	}
}
