package io.github.flemmli97.runecraftory.platform;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.ItemStack;

public class SaveItemContainer implements Container, StackedContentsCompatible {

    protected final NonNullList<ItemStack> stacks;

    public SaveItemContainer(int size) {
        this.stacks = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        this.validateSlotIndex(index);
        return this.stacks.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        this.validateSlotIndex(index);
        ItemStack stack = this.stacks.get(index);
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        return stack.split(count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        this.validateSlotIndex(index);
        ItemStack stack = this.stacks.get(index);
        if (stack.isEmpty())
            return ItemStack.EMPTY;
        this.stacks.set(index, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        this.validateSlotIndex(index);
        if (!stack.isEmpty() && !this.canPlaceItem(index, stack))
            return;
        int max = this.getMaxStackSize(index);
        if (stack.getCount() < max) {
            this.stacks.set(index, stack);
        } else {
            ItemStack copy = stack.copy();
            copy.setCount(max);
            this.stacks.set(index, copy);
            stack.shrink(max);
        }
        this.setChanged();
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
        this.setChanged();
    }

    @Override
    public void fillStackedContents(StackedContents helper) {
        for (ItemStack stack : this.stacks)
            helper.accountSimpleStack(stack);
    }

    protected void validateSlotIndex(int index) {
        if (index < 0 || index >= this.stacks.size())
            throw new RuntimeException("Slot " + index + " not in valid range - [0," + this.stacks.size() + ")");
    }

    public int getMaxStackSize(int index) {
        return 64;
    }

    public void load(CompoundTag compound) {
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        ContainerHelper.saveAllItems(compound, this.stacks);
        return compound;
    }
}
