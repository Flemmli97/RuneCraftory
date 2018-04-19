package com.flemmli97.runecraftory.common.items.misc;

import java.util.List;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.client.render.item.BakedItemRecipeModel;
import com.flemmli97.runecraftory.common.lib.LibReference;

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

public class ItemRecipe extends Item{

	public ItemRecipe()
	{
		super();
        	this.setRegistryName(new ResourceLocation(LibReference.MODID, "recipe" ));	
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
		if(stack.hasTagCompound())
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
			{
				tooltip.add(TextFormatting.AQUA + "Recipe: " + I18n.format(stack.getTagCompound().getString("Recipe")+".name"));
			}
		}
	}
	
	public void setRecipe(ItemStack stack, String recipeID)
	{
		if(!stack.hasTagCompound())
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("Recipe", recipeID);
			stack.setTagCompound(compound);
		}
		else
			stack.getTagCompound().setString("Recipe", recipeID);
	}

	@SideOnly(Side.CLIENT)
	public void initModel(ModelBakeEvent event) {
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition()
        {
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(getRegistryName(), "inventory");
            }
        });
		event.getModelRegistry().putObject(new ModelResourceLocation(getRegistryName(), "inventory"), new BakedItemRecipeModel(event.getModelManager().getModel(new ModelResourceLocation(getRegistryName(), "inventory"))));
	}
}
