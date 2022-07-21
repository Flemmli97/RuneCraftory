package io.github.flemmli97.runecraftory.common.inventory.container;

import io.github.flemmli97.runecraftory.common.attachment.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.ModContainer;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ShippingContainer extends AbstractContainerMenu {

    private final Container container;

    public ShippingContainer(int i, Inventory inventory) {
        this(i, inventory, Platform.INSTANCE.getPlayerData(inventory.player).map(PlayerData::getShippingInv).orElse(null));
    }

    public ShippingContainer(int i, Inventory inventory, Container shippingBin) {
        super(ModContainer.shippingContainer.get(), i);
        this.container = shippingBin;
        if (this.container == null)
            throw new IllegalStateException("Couldn't get shipping container from player");
        this.container.startOpen(inventory.player);
        int k = (6 - 4) * 18;
        for (int row = 0; row < 6; ++row) {
            for (int column = 0; column < 9; ++column) {
                int slot = column + row * 9;
                this.addSlot(new Slot(this.container, slot, 8 + column * 18, 18 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return this.container.canPlaceItem(slot, stack);
                    }
                });
            }
        }
        for (int row = 0; row < 3; ++row) {
            for (int column = 0; column < 9; ++column) {
                this.addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 103 + row * 18 + k));
            }
        }
        for (int hotbar = 0; hotbar < 9; ++hotbar) {
            this.addSlot(new Slot(inventory, hotbar, 8 + hotbar * 18, 161 + k));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            if (index < 6 * 9 ? !this.moveItemStackTo(itemStack2, 6 * 9, this.slots.size(), true) : !this.moveItemStackTo(itemStack2, 0, 6 * 9, false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    public Container getContainer() {
        return this.container;
    }
}
