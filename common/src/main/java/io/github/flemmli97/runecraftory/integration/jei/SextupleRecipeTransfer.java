package io.github.flemmli97.runecraftory.integration.jei;

import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.registry.ModMenuTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SextupleRecipeTransfer(
        CraftingIdentifier identifier) implements IRecipeTransferInfo<ContainerCrafting, RecipeHolder<SextupleRecipe>> {

    @Override
    public Class<? extends ContainerCrafting> getContainerClass() {
        return ContainerCrafting.class;
    }

    @Override
    public Optional<MenuType<ContainerCrafting>> getMenuType() {
        return Optional.of(ModMenuTypes.CRAFTING_CONTAINER.get());
    }

    @Override
    public RecipeType<RecipeHolder<SextupleRecipe>> getRecipeType() {
        return this.identifier.identifier();
    }

    @Override
    public boolean canHandle(ContainerCrafting container, RecipeHolder<SextupleRecipe> recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(ContainerCrafting container, RecipeHolder<SextupleRecipe> recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 37; i < container.slots.size(); i++)
            slots.add(container.getSlot(i));
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ContainerCrafting container, RecipeHolder<SextupleRecipe> recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 1; i < 37; i++)
            slots.add(container.getSlot(i));
        return slots;
    }
}
