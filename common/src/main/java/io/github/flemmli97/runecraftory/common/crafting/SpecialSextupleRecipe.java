package io.github.flemmli97.runecraftory.common.crafting;

import com.google.common.base.Suppliers;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.common.utils.CraftingUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class SpecialSextupleRecipe extends SextupleRecipe {

    //Since we dont want to register the recipe so mc doesn't pick it up elsewhere we just have it here as instances
    public static final Supplier<SpecialSextupleRecipe> scrap = Suppliers.memoize(() -> new SpecialSextupleRecipe(new ResourceLocation(RuneCraftory.MODID, "scrap_metal"), new ItemStack(ModItems.scrap.get())) {
        @Override
        public RecipeOutput getCraftingOutput(PlayerContainerInv inv) {
            Platform.INSTANCE.getPlayerData(inv.getPlayer()).ifPresent(d -> CraftingUtils.RAND.setSeed(d.getCraftingSeed(inv.getPlayer())));
            if (CraftingUtils.RAND.nextFloat() < 0.9)
                return super.getCraftingOutput(inv);
            return new RecipeOutput(new ItemStack(ModItems.scrapPlus.get()), new ItemStack(ModItems.unknown.get()), NonNullList.create());
        }
    });
    public static final Supplier<SpecialSextupleRecipe> objectX = Suppliers.memoize(() -> new SpecialSextupleRecipe(new ResourceLocation(RuneCraftory.MODID, "object_x"), new ItemStack(ModItems.objectX.get())));
    public static final Supplier<SpecialSextupleRecipe> failedDish = Suppliers.memoize(() -> new SpecialSextupleRecipe(new ResourceLocation(RuneCraftory.MODID, "scrap_metal"), new ItemStack(ModItems.failedDish.get())) {
        @Override
        public RecipeOutput getCraftingOutput(PlayerContainerInv inv) {
            Platform.INSTANCE.getPlayerData(inv.getPlayer()).ifPresent(d -> CraftingUtils.RAND.setSeed(d.getCraftingSeed(inv.getPlayer())));
            if (CraftingUtils.RAND.nextFloat() < 0.9)
                return super.getCraftingOutput(inv);
            return new RecipeOutput(new ItemStack(ModItems.disastrousDish.get()), new ItemStack(ModItems.unknown.get()), NonNullList.create());
        }
    });

    public SpecialSextupleRecipe(ResourceLocation id, ItemStack result) {
        super(id, "", 1, 10, result, NonNullList.create());
    }

    @Override
    public boolean matches(PlayerContainerInv inv, Level world) {
        return !inv.isEmpty();
    }

    @Override
    public RecipeOutput getCraftingOutput(PlayerContainerInv inv) {
        return new RecipeOutput(this.getResultItem(), new ItemStack(ModItems.unknown.get()), NonNullList.create());
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
}
