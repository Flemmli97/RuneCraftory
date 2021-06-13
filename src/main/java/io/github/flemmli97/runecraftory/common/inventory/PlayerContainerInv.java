package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class PlayerContainerInv extends RecipeWrapper {

    private final PlayerEntity player;
    private final Container container;
    private boolean refreshFlag = true;

    private PlayerContainerInv(Container container, IItemHandlerModifiable inv, PlayerEntity player) {
        super(inv);
        this.player = player;
        this.container = container;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack stack = super.decrStackSize(slot, count);
        if (this.inv.getStackInSlot(slot).isEmpty())
            this.refreshFlag = true;
        this.container.onCraftMatrixChanged(this);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (!ItemStack.areItemsEqual(this.inv.getStackInSlot(slot), stack))
            this.refreshFlag = true;
        super.setInventorySlotContents(slot, stack);
        this.container.onCraftMatrixChanged(this);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = super.removeStackFromSlot(index);
        if (!stack.isEmpty())
            this.refreshFlag = true;
        this.container.onCraftMatrixChanged(this);
        return stack;
    }

    public boolean refreshAndSet() {
        boolean refresh = this.refreshFlag;
        this.refreshFlag = false;
        return refresh;
    }

    public static PlayerContainerInv create(Container container, IItemHandlerModifiable inv, PlayerEntity player) {
        return new PlayerContainerInv(container, inv, player);
    }
}
