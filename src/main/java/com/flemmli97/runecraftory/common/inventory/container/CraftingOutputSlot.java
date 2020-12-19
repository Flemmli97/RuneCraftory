package com.flemmli97.runecraftory.common.inventory.container;

import com.flemmli97.runecraftory.common.inventory.PlayerContainerInv;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CraftingOutputSlot extends Slot {

    private int amountCrafted;
    private PlayerContainerInv ingredientInv;
    private final ContainerCrafting container;

    public CraftingOutputSlot(IInventory p_i1824_1_, ContainerCrafting container, PlayerContainerInv ingredientInv, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
        super(p_i1824_1_, p_i1824_2_, p_i1824_3_, p_i1824_4_);
        this.ingredientInv = ingredientInv;
        this.container = container;
    }

    @Override
    protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
        this.amountCrafted += p_75210_2_;
        this.onCrafting(p_75210_1_);
    }

    @Override
    protected void onSwapCraft(int p_190900_1_) {
        super.onSwapCraft(p_190900_1_);
        this.amountCrafted += p_190900_1_;

    }

    @Override
    protected void onCrafting(ItemStack p_75208_1_) {
        PlayerEntity player = ingredientInv.getPlayer();
        if (this.amountCrafted > 0) {
            p_75208_1_.onCrafting(player.world, player, this.amountCrafted);
            net.minecraftforge.fml.hooks.BasicEventHooks.firePlayerCraftingEvent(player, p_75208_1_, this.inventory);
        }

        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder) this.inventory).onCrafting(player);
        }

        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity p_190901_1_, ItemStack p_190901_2_) {
        this.onCrafting(p_190901_2_);
        NonNullList<ItemStack> remaining = this.container.getCurrentRecipe() != null ? this.container.getCurrentRecipe().getRemainingItems(this.ingredientInv) : NonNullList.withSize(0, ItemStack.EMPTY);
        boolean changed = false;
        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack itemstack = this.ingredientInv.getStackInSlot(i);
            ItemStack itemstack1 = remaining.get(i);
            if (!itemstack.isEmpty()) {
                this.ingredientInv.decrStackSize(i, 1);
                itemstack = this.ingredientInv.getStackInSlot(i);
            }

            if (!itemstack1.isEmpty()) {
                if (itemstack.isEmpty()) {
                    this.ingredientInv.setInventorySlotContents(i, itemstack1);
                    changed = true;
                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
                    itemstack1.grow(itemstack.getCount());
                    this.ingredientInv.setInventorySlotContents(i, itemstack1);
                } else if (!p_190901_1_.inventory.addItemStackToInventory(itemstack1)) {
                    p_190901_1_.dropItem(itemstack1, false);
                }
            }
        }
        if (changed)
            this.container.updateCraftingOutput();
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
        return super.decrStackSize(p_75209_1_);
    }
}
