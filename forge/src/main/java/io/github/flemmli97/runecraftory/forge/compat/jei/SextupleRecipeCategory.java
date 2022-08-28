package io.github.flemmli97.runecraftory.forge.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@SuppressWarnings("removal")
public class SextupleRecipeCategory<T extends SextupleRecipe> implements IRecipeCategory<T> {

    public static final RecipeType<SextupleRecipe> FORGECATEGORY = RecipeType.create(RuneCraftory.MODID, EnumCrafting.FORGE.getId() + "_category", SextupleRecipe.class);
    public static final RecipeType<SextupleRecipe> COOKINGCATEGORY = RecipeType.create(RuneCraftory.MODID, EnumCrafting.COOKING.getId() + "_category", SextupleRecipe.class);
    public static final RecipeType<SextupleRecipe> ARMORCATEGORY = RecipeType.create(RuneCraftory.MODID, EnumCrafting.ARMOR.getId() + "_category", SextupleRecipe.class);
    public static final RecipeType<SextupleRecipe> CHEMISTRYCATEGORY = RecipeType.create(RuneCraftory.MODID, EnumCrafting.CHEM.getId() + "_category", SextupleRecipe.class);
    public static final ResourceLocation GUI = new ResourceLocation(RuneCraftory.MODID, "textures/gui/forgec.png");

    private final IDrawable icon;
    private final IDrawable background;
    private final RecipeType<T> recipeType;
    private final TranslatableComponent title;

    public SextupleRecipeCategory(IGuiHelper guiHelper, RecipeType<T> recipeType, Item icon) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon));
        this.recipeType = recipeType;
        this.title = new TranslatableComponent("tile.crafting." + this.recipeType.getUid().getPath().replace("_category", ""));
        this.background = guiHelper.createDrawable(GUI, 19, 20, 119, 42);
    }

    @Override
    public Component getTitle() {
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
    public ResourceLocation getUid() {
        return this.recipeType.getUid();
    }

    @Override
    public Class<? extends T> getRecipeClass() {
        return this.recipeType.getRecipeClass();
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return this.recipeType;
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        if (recipeSlotsView.getSlotViews(RecipeIngredientRole.RENDER_ONLY).isEmpty()) {
            this.drawLevel(recipe, stack);
        }
    }

    protected void drawLevel(T recipe, PoseStack poseStack) {
        TranslatableComponent level = new TranslatableComponent("runecraftory.recipe_integration.crafting_level", recipe.getCraftingLevel());
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        int width = fontRenderer.width(level);
        fontRenderer.draw(poseStack, level, this.background.getWidth() - width - 10, 0, 0xFF808080);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        Player player = Minecraft.getInstance().player;
        builder.setShapeless();
        if (Platform.INSTANCE.getPlayerData(player).map(cap -> cap.getRecipeKeeper().isUnlocked(recipe)).orElse(false)) {
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                int x = i % 3;
                int y = i / 3;
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + x * 18, 6 + y * 18)
                        .addIngredients(recipe.getIngredients().get(i));
            }
        } else {
            for (Ingredient ing : recipe.getIngredients()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                        .addIngredients(ing);
            }
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 65, 15)
                    .addItemStack(new ItemStack(ModItems.unknown.get()))
                    .addTooltipCallback((view, tooltip) -> {
                        tooltip.clear();
                        tooltip.add(new TranslatableComponent("runecraftory.recipe_integration.locked"));
                    });
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 15)
                .addItemStack(recipe.getResultItem());
    }
}
