package com.flemmli97.runecraftory.compat.crafttweaker;

import com.flemmli97.runecraftory.common.core.handler.crafting.CraftingHandler;
import com.flemmli97.runecraftory.common.core.handler.crafting.RecipeSextuple;
import com.flemmli97.runecraftory.common.lib.enums.EnumCrafting;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.runecraftory.Recipes")
@ZenRegister
public class RecipeTweaker
{
    @ZenMethod
    public static void addForgeRecipe(IItemStack stack, IIngredient[] ingredients, int level) 
    {
        CraftTweakerAPI.apply(new AddRecipe((byte)0, CraftTweakerMC.getItemStack(stack), toObject(ingredients), level));
    }
    
    @ZenMethod
    public static void addAccessRecipe(IItemStack stack, IIngredient[] ingredients, int level) 
    {
        CraftTweakerAPI.apply(new AddRecipe((byte)1, CraftTweakerMC.getItemStack(stack), toObject(ingredients), level));
    }
    
    @ZenMethod
    public static void addPharmacyRecipe(IItemStack stack, IIngredient[] ingredients, int level) 
    {
        CraftTweakerAPI.apply(new AddRecipe((byte)2, CraftTweakerMC.getItemStack(stack), toObject(ingredients), level));
    }
    
    @ZenMethod
    public static void addCookingRecipe(IItemStack stack, IIngredient[] ingredients, int level) 
    {
        CraftTweakerAPI.apply(new AddRecipe((byte)3, CraftTweakerMC.getItemStack(stack), toObject(ingredients), level));
    }
    
    @ZenMethod
    public static void removeForgeRecipe(IItemStack stack) 
    {
        CraftTweakerAPI.apply(new RemoveRecipe((byte)0, CraftTweakerMC.getItemStack(stack)));
    }
    
    @ZenMethod
    public static void removeAccessRecipe(IItemStack stack) 
    {
        CraftTweakerAPI.apply(new RemoveRecipe((byte)1, CraftTweakerMC.getItemStack(stack)));
    }
    
    @ZenMethod
    public static void removePharmacyRecipe(IItemStack stack) 
    {
        CraftTweakerAPI.apply(new RemoveRecipe((byte)2, CraftTweakerMC.getItemStack(stack)));
    }
    
    @ZenMethod
    public static void removeCookingRecipe(IItemStack stack) 
    {
        CraftTweakerAPI.apply(new RemoveRecipe((byte)3, CraftTweakerMC.getItemStack(stack)));
    }
    
    private static Object[] toObject(IIngredient[] ingredients) 
    {
        if (ingredients == null) 
        {
            throw new IllegalArgumentException();
        }
        Object[] objs = new Object[ingredients.length];
        for (int i = 0; i < ingredients.length; ++i) 
        {
            IIngredient ing = ingredients[i];
            if (ing instanceof IItemStack) 
            {
                objs[i] = CraftTweakerMC.getItemStack((IItemStack)ing);
            }
            else if (ing instanceof IOreDictEntry) 
            {
                objs[i] = ((IOreDictEntry)ing.getInternal()).getName();
            }
        }
        return objs;
    }
    
    private static class AddRecipe implements IAction
    {
        private EnumCrafting type;
        private ItemStack output;
        private Object[] recipe;
        private int level;
        
        public AddRecipe(byte type, ItemStack output, Object[] recipe, int level) 
        {
            switch (type) 
            {
                case 0:
                    this.type = EnumCrafting.FORGE;
                    break;
                case 1:
                    this.type = EnumCrafting.ARMOR;
                    break;
                case 2:
                    this.type = EnumCrafting.PHARMA;
                    break;
                case 3:
                    this.type = EnumCrafting.COOKING;
                    break;
            }
            this.output = output;
            this.recipe = recipe;
            this.level = level;
        }
        
        @Override
        public void apply() 
        {
            CraftingHandler.addRecipe(this.type, new RecipeSextuple(this.level, this.output, this.recipe));
        }
        
        @Override
        public String describe() 
        {
            return "Added crafting recipe with type " + this.type + " for item " + this.output;
        }
    }
    
    private static class RemoveRecipe implements IAction
    {
        private String id;
        private EnumCrafting type;
        
        public RemoveRecipe(byte type, ItemStack output) 
        {
            this.id = output.getItem().getRegistryName().toString();
            switch (type) 
            {
                case 0:
                    this.type = EnumCrafting.FORGE;
                    break;
                case 1:
                    this.type = EnumCrafting.ARMOR;
                    break;
                case 2:
                    this.type = EnumCrafting.PHARMA;
                    break;
                case 3:
                    this.type = EnumCrafting.COOKING;
                    break;
            }
        }
        
        @Override
        public void apply() 
        {
            CraftingHandler.removeRecipe(this.type, this.id);
        }
        
        @Override
        public String describe() 
        {
            return "Removed crafting recipe(s) with type " + this.type + " for item " + this.id;
        }
    }
}
