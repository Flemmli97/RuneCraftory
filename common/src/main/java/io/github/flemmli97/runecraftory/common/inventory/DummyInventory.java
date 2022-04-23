package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DummyInventory implements Container {

    protected final Container inv;

    public DummyInventory(Container inv) {
        this.inv = inv;
    }

    @Override
    public int getContainerSize() {
        return this.inv.getContainerSize();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inv.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return this.inv.removeItem(slot, count);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inv.setItem(slot, stack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.inv.removeItemNoUpdate(index);
    }

    @Override
    public boolean isEmpty() {
        return this.inv.isEmpty();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.inv.canPlaceItem(slot, stack);
    }

    @Override
    public void clearContent() {
        this.inv.clearContent();
    }

    @Override
    public int getMaxStackSize() {
        return this.inv.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.inv.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inv.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
        this.inv.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        this.inv.stopOpen(player);
    }
}
