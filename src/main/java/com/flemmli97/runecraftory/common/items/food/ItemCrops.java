package com.flemmli97.runecraftory.common.items.food;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.api.mappings.CropMap;
import com.flemmli97.runecraftory.common.core.handler.capabilities.CapabilityProvider;
import com.flemmli97.runecraftory.common.core.handler.capabilities.IPlayer;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/**
 * If metadata = 1 -> Giant crop
 */
public class ItemCrops extends ItemFood implements IModelRegister{

	private int hp, rp;
	private String oreDict;
	public ItemCrops(String name, int hpIncrease, int rpIncrease, String oreDictName) {
		super(0, 0, false);
		this.rp=rpIncrease;
		this.hp=hpIncrease;
		this.oreDict=oreDictName;
		this.setCreativeTab(RuneCraftory.crops);
		this.setRegistryName(new ResourceLocation(LibReference.MODID, "crop_"+name));
        this.setUnlocalizedName(this.getRegistryName().toString());
        CropMap.addCrop(oreDictName, this);
	}
	
	public String cropSpecificOreDict()
	{
		return this.oreDict;
	}
	
	@Override
	public String getUnlocalizedName() {
		return this.getRegistryName().toString();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return this.getRegistryName().toString();
	}
	
	@Override
    protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player)
    {
    	super.onFoodEaten(stack, worldIn, player);
    	IPlayer cap = player.getCapability(CapabilityProvider.PlayerCapProvider.PlayerCap, null);
    	cap.setRunePoints(player, cap.getRunePoints()+this.rp);
    	cap.regenHealth(player, this.hp);
    }
    
    public boolean isGiant(ItemStack stack)
    {
    	return stack.getMetadata()==1;
    }
	
	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ModelLoader.setCustomModelResourceLocation(this, 1, new ModelResourceLocation(getRegistryName()+"_giant", "inventory"));		
	}
}
