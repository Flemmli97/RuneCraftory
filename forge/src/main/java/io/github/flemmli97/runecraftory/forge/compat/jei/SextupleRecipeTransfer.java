package io.github.flemmli97.runecraftory.forge.compat.jei;

import io.github.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("removal")
public record SextupleRecipeTransfer(
        RecipeType<SextupleRecipe> category) implements IRecipeTransferInfo<ContainerCrafting, SextupleRecipe> {

    @Override
    public Class<ContainerCrafting> getContainerClass() {
        return ContainerCrafting.class;
    }

    @Override
    public RecipeType<SextupleRecipe> getRecipeType() {
        return this.category;
    }

    @Override
    public boolean canHandle(ContainerCrafting containerCrafting, SextupleRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(ContainerCrafting containerCrafting, SextupleRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 37; i < containerCrafting.slots.size(); i++)
            slots.add(containerCrafting.getSlot(i));
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ContainerCrafting containerCrafting, SextupleRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 1; i < 37; i++)
            slots.add(containerCrafting.getSlot(i));
        return slots;
    }

    @Override
    public Class<SextupleRecipe> getRecipeClass() {
        return SextupleRecipe.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return this.category.getUid();
    }
}
