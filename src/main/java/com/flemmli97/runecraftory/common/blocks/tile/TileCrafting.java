package com.flemmli97.runecraftory.common.blocks.tile;

import com.flemmli97.runecraftory.api.enums.EnumCrafting;
import com.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class TileCrafting extends TileEntity implements IItemHandlerModifiable, INamedContainerProvider {

    private final NonNullList<ItemStack> inventory;
    private final EnumCrafting type;

    public TileCrafting(TileEntityType<?> p_i48289_1_, EnumCrafting type) {
        super(p_i48289_1_);
        this.type = type;
        if (this.type == EnumCrafting.COOKING || this.type == EnumCrafting.CHEM)
            this.inventory = NonNullList.withSize(6, ItemStack.EMPTY);
        else this.inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    }

    public EnumCrafting craftingType() {
        return this.type;
    }

    @Override
    public int getSlots() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot <= this.getSlots() && slot >= 0 ? this.inventory.get(slot) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot <= this.getSlots() && slot >= 0)
            this.inventory.set(slot, stack);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("tile.crafting." + this.type.toString().toLowerCase());
    }

    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        return new ContainerCrafting(p_createMenu_1_, p_createMenu_2_, this);
    }
}
