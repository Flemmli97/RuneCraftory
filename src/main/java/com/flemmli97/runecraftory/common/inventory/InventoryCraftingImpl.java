package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.common.crafting.SextupleRecipe;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

import java.util.function.BiFunction;

public class InventoryCraftingImpl extends CraftingInventory{

    private final BiFunction<Integer, ItemStack, Boolean> validation;
    private final ServerPlayerEntity craftingPlayer;

    public InventoryCraftingImpl(Container container, ServerPlayerEntity player, BiFunction<Integer, ItemStack, Boolean> validation){
        super(container, 3,2);
        this.validation = validation;
        this.craftingPlayer = player;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return this.validation.apply(slot, stack);
    }

    public boolean canCraft(SextupleRecipe recipe){
        return false;
    }

    public ServerPlayerEntity getCraftingPlayer(){
        return this.craftingPlayer;
    }
}
