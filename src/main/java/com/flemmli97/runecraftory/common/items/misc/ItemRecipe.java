package com.flemmli97.runecraftory.common.items.misc;

import java.util.Iterator;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.client.render.item.BakedItemRecipeModel;
import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.lib.LibReference;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecipe extends Item
{
    public ItemRecipe() {
        this.setRegistryName(new ResourceLocation(LibReference.MODID, "recipe"));
        this.setUnlocalizedName(this.getRegistryName().toString());
        this.setHasSubtypes(true);
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.getRegistryName().toString();
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName();
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
    
    @SideOnly(Side.CLIENT)
    public void initModel(ModelBakeEvent event) {
        ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation(ItemRecipe.this.getRegistryName(), "inventory");
            }
        });
        event.getModelRegistry().putObject(new ModelResourceLocation(this.getRegistryName(), "inventory"), new BakedItemRecipeModel(event.getModelManager().getModel(new ModelResourceLocation(this.getRegistryName(), "inventory"))));
    }
}
