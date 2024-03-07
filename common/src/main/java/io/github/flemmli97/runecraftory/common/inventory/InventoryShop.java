package io.github.flemmli97.runecraftory.common.inventory;

import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class InventoryShop implements Container {

    public static final int SHOP_SIZE = 30;

    private final NonNullList<ItemStack> slots = NonNullList.withSize(SHOP_SIZE, ItemStack.EMPTY);
    private final NonNullList<ItemStack> shop;
    private int index;
    private ItemStack output = ItemStack.EMPTY;

    public final EntityNPCBase npc;

    public InventoryShop(EntityNPCBase npc, NonNullList<ItemStack> shop) {
        this.npc = npc;
        this.shop = shop;
        this.updateInv();
    }

    @Override
    public int getContainerSize() {
        return this.slots.size() + 1;
    }

    @Override
    public boolean isEmpty() {
        if (!this.output.isEmpty())
            return false;
        for (ItemStack itemstack : this.slots)
            if (!itemstack.isEmpty())
                return false;
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        if (index == SHOP_SIZE) {
            return this.output;
        }
        return this.slots.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index == SHOP_SIZE) {
            return this.output.split(count);
        }
        return ContainerHelper.removeItem(this.slots, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index == SHOP_SIZE) {
            ItemStack stack = this.output.copy();
            this.output = ItemStack.EMPTY;
            return stack;
        }
        return ContainerHelper.takeItem(this.slots, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == SHOP_SIZE) {
            this.output = stack;
        } else {
            stack.setCount(this.getMaxStackSize());
            this.slots.set(index, stack);
        }
    }

    @Override
    public void setChanged() {
        this.updateInv();
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.slots.clear();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return false;
    }

    public void next() {
        this.index = Math.min(this.shop.size() / SHOP_SIZE, ++this.index);
        this.updateInv();
    }

    public void prev() {
        this.index = Math.max(0, --this.index);
        this.updateInv();
    }

    public boolean hasNext() {
        return this.index < this.shop.size() / SHOP_SIZE;
    }

    public boolean hasPrev() {
        return this.index > 0;
    }

    private void updateInv() {
        List<ItemStack> list = this.shop;
        for (int i = 0; i < this.slots.size(); ++i) {
            int slot = i + SHOP_SIZE * this.index;
            ItemStack stack = ItemStack.EMPTY;
            if (slot < list.size()) {
                stack = list.get(slot);
            }
            this.slots.set(i, stack);
        }
    }
}