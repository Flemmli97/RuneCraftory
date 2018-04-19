package com.flemmli97.runecraftory.client.render.item;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.items.misc.ItemRecipe;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.BakedModelWrapper;

public class BakedItemRecipeModel extends BakedModelWrapper<IBakedModel>{

	private ItemOverrideList overrides;
	
	public BakedItemRecipeModel(IBakedModel originalModel)
    {
		super(originalModel);
		this.overrides=new RecipeItemOverride(this);
	}

	@Override
	public ItemOverrideList getOverrides() {
		return overrides;
	}
		
	public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world,
			EntityLivingBase entity) {
		ItemStack stack2 = ItemStack.EMPTY;
		if(stack.getItem() instanceof ItemRecipe && stack.hasTagCompound())
		{
			for(RecipeSextuple r : CraftingHandler.getRecipeFromID(EnumCrafting.fromID(stack.getMetadata()), stack.getTagCompound().getString("Recipe")))
			{
				stack2 = r.getCraftingOutput();
				break;
			}
		}
		IBakedModel modelStack = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(stack2, world, entity);
		return (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT ) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT )) && !stack2.isEmpty() ? modelStack: originalModel;
	}

	private class RecipeItemOverride extends ItemOverrideList
	{
		BakedItemRecipeModel model;
		public RecipeItemOverride(BakedItemRecipeModel model) {
			super(new ArrayList<ItemOverride>());
			this.model=model;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
	    {
			return model.handleItemState( originalModel, stack, world, entity );
		}
	}
}
