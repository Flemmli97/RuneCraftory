package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.blocks.tile.TileCrafting;
import com.flemmli97.runecraftory.common.inventory.DummyInventory;
import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import com.flemmli97.runecraftory.common.registry.ModContainer;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.ItemStackHandler;

public class ContainerUpgrade extends Container {

    private final PlayerContainerInv craftingInv;
    private final EnumCrafting type;
    private final DummyInventory outPutInv;

    public ContainerUpgrade(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, inv, ContainerCrafting.getTile(inv.player.world, data));
    }

    public ContainerUpgrade(int windowId, PlayerInventory playerInv, TileCrafting tile) {
        super(ModContainer.upgradeContainer.get(), windowId);
        if (tile.getSlots() <= 6)
            throw new IllegalStateException("Tried ccreating a container for " + tile + " but its not made for upgrading items");
        this.outPutInv = new DummyInventory(new ItemStackHandler());
        this.craftingInv = PlayerContainerInv.create(this, tile, playerInv.player);
        this.type = tile.craftingType();
        this.addSlot(new UpgradeOutputSlot(this.outPutInv, this, this.craftingInv, 0, 116, 34));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }
        this.addSlot(new Slot(this.craftingInv, 6, 20, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return this.inventory.isItemValidForSlot(6, stack);
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }
        });
        this.addSlot(new Slot(this.craftingInv, 7, 56, 35) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return this.inventory.isItemValidForSlot(7, stack);
            }
        });

        this.onCraftMatrixChanged(this.craftingInv);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inv) {
        if (inv == this.craftingInv && !this.craftingInv.getPlayer().world.isRemote) {
            this.outPutInv.setInventorySlotContents(0, ItemUtils.getUpgradedStack(this.craftingInv.getStackInSlot(6), this.craftingInv.getStackInSlot(7)));
        }
        super.onCraftMatrixChanged(inv);
    }

    public EnumCrafting craftingType() {
        return this.type;
    }

    @Override
    public boolean canInteractWith(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotID) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotID);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (slotID == 0) {
                itemstack1.onCrafting(player.world, player, itemstack1.getCount());
                if (!this.mergeItemStack(itemstack1, 1, 36, false))
                    return ItemStack.EMPTY;
                slot.onSlotChange(itemstack1, itemstack);
            } else if (slotID < 37) {
                if (!this.mergeItemStack(itemstack1, 37, this.inventorySlots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 1, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemstack2 = slot.onTake(player, itemstack1);
            if (slotID == 0) {
                player.dropItem(itemstack2, false);
            }
        }

        return itemstack;
    }
}
