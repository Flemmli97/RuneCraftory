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

public class ItemCloths extends Item implements IModelRegister
{
    private String[] cloth;
    
    public ItemCloths() {
        this.cloth = new String[] { "cheap", "quality", "h_quality", "silk", "ghost", "giant", "blue", "insect", "pretty", "ancient" };
        this.setCreativeTab(RuneCraftory.upgradeItems);
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "cloth"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + this.cloth[stack.getMetadata()];
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (tab == RuneCraftory.upgradeItems) {
            for (int i = 0; i < 10; ++i) {
                ItemStack stack = new ItemStack(this, 1, i);
                ItemNBT.initNBT(stack);
                items.add(stack);
            }
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void initModel() {
        for (int meta = 0; meta < 10; ++meta) {
            ModelLoader.setCustomModelResourceLocation(this, meta, new ModelResourceLocation(this.getRegistryName() + "_" + this.cloth[meta], "inventory"));
        }
    }
}
