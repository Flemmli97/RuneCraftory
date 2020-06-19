package com.flemmli97.runecraftory.common.items.misc;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.common.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class ItemRecipe extends Item
{
    public ItemRecipe() {
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "recipe"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setHasSubtypes(true);
    }
    
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound() && Keyboard.isKeyDown(42)) {
            String itemName = "";
            Iterator<RecipeSextuple> iterator = CraftingHandler.getRecipeFromID(EnumCrafting.fromID(stack.getMetadata()), stack.getTagCompound().getString("Recipe")).iterator();
            if (iterator.hasNext()) {
                RecipeSextuple r = iterator.next();
                itemName = r.getCraftingOutput().getItem().getUnlocalizedName() + ".name";
            }
            tooltip.add(TextFormatting.AQUA + "Recipe: " + I18n.format(itemName, new Object[0]));
        }
    }
    
    public void setRecipe(ItemStack stack, String recipeID) {
        if (!stack.hasTagCompound()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setString("Recipe", recipeID);
            stack.setTagCompound(compound);
        }
        else {
            stack.getTagCompound().setString("Recipe", recipeID);
        }
    }
}
