package io.github.flemmli97.runecraftory.common.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class PlayerContainerInv extends DummyInventory {

    private final Player player;
    private final AbstractContainerMenu container;
    private boolean refreshFlag = true;

    private PlayerContainerInv(AbstractContainerMenu container, Container inv, Player player) {
        super(inv);
        this.player = player;
        this.container = container;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = super.removeItem(slot, count);
        if (this.getItem(slot).isEmpty())
            this.refreshFlag = true;
        this.container.slotsChanged(this);
        return stack;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (!ItemStack.isSame(this.getItem(slot), stack))
            this.refreshFlag = true;
        super.setItem(slot, stack);
        this.container.slotsChanged(this);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = super.removeItemNoUpdate(index);
        if (!stack.isEmpty())
            this.refreshFlag = true;
        this.container.slotsChanged(this);
        return stack;
    }

    public boolean refreshAndSet() {
        boolean refresh = this.refreshFlag;
        this.refreshFlag = false;
        return refresh;
    }

    public static PlayerContainerInv create(AbstractContainerMenu container, Container inv, Player player) {
        return new PlayerContainerInv(container, inv, player);
    }
}
