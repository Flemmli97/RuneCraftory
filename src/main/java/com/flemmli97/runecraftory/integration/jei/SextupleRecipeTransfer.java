package com.flemmli97.runecraftory.integration.jei;

import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SextupleRecipeTransfer implements IRecipeTransferInfo<ContainerCrafting> {

    private final ResourceLocation category;

    public SextupleRecipeTransfer(ResourceLocation category) {
        this.category = category;
    }

    @Override
    public Class<ContainerCrafting> getContainerClass() {
        return ContainerCrafting.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return this.category;
    }

    @Override
    public boolean canHandle(ContainerCrafting containerCrafting) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(ContainerCrafting containerCrafting) {
        List<Slot> slots = new ArrayList<>();
        for(int i = 37; i < containerCrafting.inventorySlots.size(); i++)
            slots.add(containerCrafting.getSlot(i));
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(ContainerCrafting containerCrafting) {
        List<Slot> slots = new ArrayList<>();
        for(int i = 1; i < 37; i++)
            slots.add(containerCrafting.getSlot(i));
        return slots;
    }
}
