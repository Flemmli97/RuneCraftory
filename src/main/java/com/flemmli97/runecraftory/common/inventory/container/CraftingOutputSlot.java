package com.flemmli97.runecraftory.common.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class CraftingOutputSlot extends Slot {

    private int amountCrafted;
    private PlayerEntity player;
    public CraftingOutputSlot(IInventory p_i1824_1_, PlayerEntity player, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
    }

    @Override
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.amountCrafted += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    @Override
    protected void onSwapCraft(int p_190900_1_) {
        super.onSwapCraft(p_190900_1_);
    }

    @Override
    protected void onCrafting(ItemStack p_75208_1_) {
        if (this.amountCrafted > 0) {
            p_75208_1_.onCrafting(this.player.world, this.player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(this.player, p_75208_1_, this.inventory);
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder)this.inventory).onCrafting(this.player);
        }

        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
        this.onCrafting(p_190901_2_);
        return super.onTake(p_190901_1_, p_190901_2_);
    }

    @Override
    public boolean isItemValid(ItemStack p_75214_1_) {
        return false;
    }

    @Override
    public void onSlotChanged() {
        super.onSlotChanged();
    }

    @Override
    public ItemStack decrStackSize(int p_75209_1_) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(p_75209_1_, this.getStack().getCount());
        }

        return super.decrStackSize(p_75209_1_);    }
}
