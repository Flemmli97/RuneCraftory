package com.flemmli97.runecraftory.common.items.misc;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.items.IModelRegister;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.utils.ItemNBT;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFeathers extends Item implements IModelRegister
{
    private String[] feathers;
    
    public ItemFeathers() {
        this.feathers = new String[] { "feather", "black", "thunder", "yellow", "fin" };
        this.setCreativeTab(RuneCraftory.upgradeItems);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "feathers"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + this.feathers[stack.getMetadata()];
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == RuneCraftory.upgradeItems) {
            for (int i = 0; i < 5; ++i) {
                ItemStack stack = new ItemStack(this, 1, i);
                ItemNBT.initNBT(stack);
                items.add(stack);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (int meta = 0; meta < 5; ++meta) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(this.getRegistryName() + "_" + this.feathers[meta], "inventory"));
        }
    }
}
