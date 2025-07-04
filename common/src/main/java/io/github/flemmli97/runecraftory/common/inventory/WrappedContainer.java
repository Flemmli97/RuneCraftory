package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class WrappedContainer implements Container {

    protected final Container container;
    protected final Consumer<Container> listener;

    public WrappedContainer(Container container) {
        this(container, null);
    }

    public WrappedContainer(Container container, Consumer<Container> listener) {
        this.container = container;
        this.listener = listener;
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
        ItemStack stack = this.container.removeItem(slot, count);
        this.setChanged();
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = this.container.removeItemNoUpdate(slot);
        this.setChanged();
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.container.setItem(slot, stack);
        this.setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return this.container.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.container.setChanged();
        if (this.listener != null) {
            this.listener.accept(this);
        }
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
