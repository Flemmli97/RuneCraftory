package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class UpgradeOutputSlot extends CraftingOutputSlot{

    public UpgradeOutputSlot(IInventory output, ContainerCrafting container, PlayerContainerInv ingredientInv, int id, int x, int y) {
        super(output, container, ingredientInv, id, x, y);
    }

    @Override
    public ItemStack onTake(PlayerEntity player, ItemStack stack) {
        return super.onTake(player, stack);
    }
}
