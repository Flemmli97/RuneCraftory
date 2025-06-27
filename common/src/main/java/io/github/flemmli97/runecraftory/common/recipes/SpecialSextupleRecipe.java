package io.github.flemmli97.runecraftory.common.recipes;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.PlayerBoundCraftingContainer;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SpecialSextupleRecipe extends SextupleRecipe {

    public static final Supplier<RecipeHolder<SpecialSextupleRecipe>> SCRAP = Suppliers.memoize(() -> new RecipeHolder<>(RuneCraftory.modRes("scrap_metal"),
            new SpecialSextupleRecipe(new ItemStack(ModItems.SCRAP.get()), new AlternateResult(new ItemStack(ModItems.SCRAP_PLUS.get()), 0.1f))));
    public static final Supplier<RecipeHolder<SpecialSextupleRecipe>> OBJECT_X = Suppliers.memoize(() -> new RecipeHolder<>(RuneCraftory.modRes("object_x"),
            new SpecialSextupleRecipe(new ItemStack(ModItems.OBJECT_X.get()), null)));
    public static final Supplier<RecipeHolder<SpecialSextupleRecipe>> FAILED_DISH = Suppliers.memoize(() -> new RecipeHolder<>(RuneCraftory.modRes("failed_dish"),
            new SpecialSextupleRecipe(new ItemStack(ModItems.FAILED_DISH.get()), new AlternateResult(new ItemStack(ModItems.DISASTROUS_DISH.get()), 0.1f))));

    private final AlternateResult alternateResult;

    private SpecialSextupleRecipe(ItemStack result, AlternateResult alternateResult) {
        super("", 1, 10, result, NonNullList.create());
        this.alternateResult = alternateResult;
    }

    @Override
    public boolean matches(PlayerBoundCraftingContainer inv, Level level) {
        return !inv.isEmpty();
    }

    @Override
    public RecipeOutput createOutput(PlayerBoundCraftingContainer inv, boolean unlocked) {
        if (inv.isEmpty())
            return null;
        CraftingUtils.RAND.setSeed(Platform.INSTANCE.getPlayerData(inv.getPlayer()).getCraftingSeed(inv.getPlayer()));
        ItemStack res = this.alternateResult != null && CraftingUtils.RAND.nextFloat() < this.alternateResult.chance() ? this.alternateResult.stack().copy() :
                this.getResultItem(inv.getPlayer().registryAccess());
        return new RecipeOutput(res, new ItemStack(ModItems.UNKNOWN.get()), NonNullList.create());
    }

    @Override
    public ItemStack getToastSymbol() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    //Since the recipes arent registered this should be fine
    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    private record AlternateResult(ItemStack stack, float chance) {

    }
}
