package com.flemmli97.runecraftory.integration.jei.category;

import com.flemmli97.runecraftory.RuneCraftory;
import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import com.flemmli97.runecraftory.common.registry.ModItems;
import com.google.common.collect.Lists;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SextupleRecipeCategory<T extends SextupleRecipe> implements IRecipeCategory<T> {

    public static final ResourceLocation FORGECATEGORY = new ResourceLocation(RuneCraftory.MODID, "forge_category");
    public static final ResourceLocation COOKINGCATEGORY = new ResourceLocation(RuneCraftory.MODID, "cooking_category");
    public static final ResourceLocation ARMORCATEGORY = new ResourceLocation(RuneCraftory.MODID, "armory_category");
    public static final ResourceLocation CHEMISTRYCATEGORY = new ResourceLocation(RuneCraftory.MODID, "chemistry_category");
    public static final ResourceLocation GUI = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");

    private final IDrawable icon;
    private final IDrawable background;
    private final ResourceLocation id;
    private final Class<T> clss;
    private final String title;

    public SextupleRecipeCategory(IGuiHelper guiHelper, Class<T> clss, ResourceLocation id, Item icon) {
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(icon));
        this.id = id;
        this.clss = clss;
        this.title = I18n.format("runecraftory.jei." + id.getPath().replace("_category", ""));
        this.background = guiHelper.createDrawable(GUI, 19, 20, 119, 41);
    }

    @Override
    public ResourceLocation getUid() {
        return this.id;
    }

    @Override
    public Class<? extends T> getRecipeClass() {
        return this.clss;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(T sextupleRecipe, IIngredients iIngredients) {
        List<List<ItemStack>> list = new ArrayList<>();
        for (Ingredient ingr : sextupleRecipe.getIngredients()) {
            list.add(Arrays.asList(ingr.getMatchingStacks()));
        }
        iIngredients.setInputLists(VanillaTypes.ITEM, list);
        iIngredients.setOutput(VanillaTypes.ITEM, sextupleRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout layout, T sextupleRecipe, IIngredients ing) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        layout.setShapeless();
        IGuiItemStackGroup guiStacks = layout.getItemStacks();

        for (int y = 0; y < 2; y++)
            for (int x = 0; x < 3; x++) {
                int ind = x + y * 3;
                guiStacks.init(ind, true, 0 + x * 18, 5 + y * 18);
            }

        if(player.getRecipeBook().contains(sextupleRecipe.getId())) {
            for (int i = 0; i < ing.getInputs(VanillaTypes.ITEM).size(); i++) {
                guiStacks.set(i, ing.getInputs(VanillaTypes.ITEM).get(i));
            }
        } else {
            guiStacks.init(8, false, 64, 14);
            guiStacks.set(8, Lists.newArrayList(new ItemStack(ModItems.unknown.get())));
        }
        guiStacks.init(7, false, 96, 14);
        guiStacks.set(7, ing.getOutputs(VanillaTypes.ITEM).get(0));
        guiStacks.addTooltipCallback((slot, input, ingredient, tooltip)->{
            if(slot == 8) {
                tooltip.clear();
                tooltip.add(new TranslationTextComponent("runecraftory.jei.locked"));
            }
        });
    }
}
