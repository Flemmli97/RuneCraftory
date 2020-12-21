package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

public class ContainerUpgrade extends ContainerCrafting{

    public ContainerUpgrade(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(windowId, inv, data);
    }

    public ContainerUpgrade(int windowId, PlayerInventory playerInv, TileCrafting tile) {
        super(windowId, playerInv, tile);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        super.onCraftMatrixChanged(inv);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        return super.transferStackInSlot(player, slotID);
    }
}
