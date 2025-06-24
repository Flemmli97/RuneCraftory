package io.github.flemmli97.runecraftory.integration.jei;

import com.mojang.serialization.Codec;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.platform.Platform;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;

public class SextupleRecipeCategory<T extends SextupleRecipe> implements IRecipeCategory<RecipeHolder<T>> {

    public static final RecipeType<RecipeHolder<SextupleRecipe>> FORGING = createHolder(RuneCraftory.MODID, EnumCrafting.FORGE.getId() + "_category");
    public static final RecipeType<RecipeHolder<SextupleRecipe>> COOKING = createHolder(RuneCraftory.MODID, EnumCrafting.COOKING.getId() + "_category");
    public static final RecipeType<RecipeHolder<SextupleRecipe>> ARMOR = createHolder(RuneCraftory.MODID, EnumCrafting.ARMOR.getId() + "_category");
    public static final RecipeType<RecipeHolder<SextupleRecipe>> CHEMISTRY = createHolder(RuneCraftory.MODID, EnumCrafting.CHEM.getId() + "_category");
    public static final ResourceLocation GUI = RuneCraftory.modRes("textures/gui/crafting.png");

    public static <R extends Recipe<?>> RecipeType<RecipeHolder<R>> createHolder(String nameSpace, String path) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(nameSpace, path);
        @SuppressWarnings({"unchecked", "RedundantCast"})
        Class<? extends RecipeHolder<R>> holderClass = (Class<? extends RecipeHolder<R>>) (Object) RecipeHolder.class;
        return new RecipeType<>(id, holderClass);
    }

    private final IDrawable icon;
    private final IDrawable background;
    private final RecipeType<RecipeHolder<T>> recipeType;
    private final Component title;

    public SextupleRecipeCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Item icon) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(icon));
        this.recipeType = recipeType;
        this.title = Component.translatable("runecraftory.tile.crafting." + this.recipeType.getUid().getPath().replace("_category", ""));
        this.background = guiHelper.createDrawable(GUI, 19, 20, 119, 42);
    }

    @Override
    public RecipeType<RecipeHolder<T>> getRecipeType() {
        return this.recipeType;
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> holder, IFocusGroup focuses) {
        Player player = Minecraft.getInstance().player;
        builder.setShapeless();
        T recipe = holder.value();
        if (Platform.INSTANCE.getPlayerData(player).getRecipeKeeper().isUnlocked(holder)) {
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
                    .addItemStack(new ItemStack(ModItems.UNKNOWN.get()))
                    .addRichTooltipCallback((view, tooltip) ->
                            tooltip.add(Component.translatable("runecraftory.recipe_integration.locked")));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 15)
                .addItemStack(recipe.getResultItem(Minecraft.getInstance().level.registryAccess()));
    }

    @Override
    public void draw(RecipeHolder<T> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        if (recipeSlotsView.getSlotViews(RecipeIngredientRole.RENDER_ONLY).isEmpty()) {
            this.drawLevel(recipe.value(), guiGraphics);
        }
    }

    protected void drawLevel(T recipe, GuiGraphics guiGraphics) {
        Component level = Component.translatable("runecraftory.recipe_integration.crafting_level", recipe.getCraftingLevel());
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        int width = font.width(level);
        guiGraphics.drawString(font, level, this.background.getWidth() - width - 10, 0, 0xFF808080);
    }

    @Override
    public Codec<RecipeHolder<T>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }
}
