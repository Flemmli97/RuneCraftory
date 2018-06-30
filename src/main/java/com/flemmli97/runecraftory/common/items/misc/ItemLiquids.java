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

public class ItemLiquids extends Item implements IModelRegister
{
    private String[] liquids;
    
    public ItemLiquids() {
        this.liquids = new String[] { "glue", "blood", "para", "poison" };
        this.setCreativeTab(RuneCraftory.upgradeItems);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "liquid"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + this.liquids[stack.getMetadata()];
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == RuneCraftory.upgradeItems) {
            for (int i = 0; i < 4; ++i) {
                ItemStack stack = new ItemStack(this, 1, i);
                ItemNBT.initNBT(stack);
                items.add(stack);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (int meta = 0; meta < 4; ++meta) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(this.getRegistryName() + "_" + this.liquids[meta], "inventory"));
        }
    }
}
