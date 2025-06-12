package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class WrappedContainer implements Container {

    protected final Container container;

    public WrappedContainer(Container container) {
        this.container = container;
    }

    @Override
    public int getContainerSize() {
        return this.container.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.container.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.container.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return this.container.removeItem(slot, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.container.removeItemNoUpdate(index);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.container.setItem(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.container.stillValid(player);
    }

    @Override
    public void startOpen(Player player) {
        this.container.startOpen(player);
    }

    @Override
    public void stopOpen(Player player) {
        this.container.stopOpen(player);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.container.canPlaceItem(slot, stack);
    }

    @Override
    public void clearContent() {
        this.container.clearContent();
    }
}
