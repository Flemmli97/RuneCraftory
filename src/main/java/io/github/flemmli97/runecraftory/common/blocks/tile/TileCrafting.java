package io.github.flemmli97.runecraftory.common.blocks.tile;

import io.github.flemmli97.runecraftory.api.enums.EnumCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerCrafting;
import io.github.flemmli97.runecraftory.common.inventory.container.ContainerUpgrade;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
    private int craftingIndex;

    public TileCrafting(TileEntityType<?> tileEntityType, EnumCrafting type) {
        super(tileEntityType);
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
        return slot == 6 ? 1 : 64;
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
        return new TranslationTextComponent("tile.crafting." + this.type.getTranslation());
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        ItemStackHelper.loadAllItems(nbt, this.inventory);
        nbt.getInt("Index");
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        ItemStackHelper.saveAllItems(nbt, this.inventory);
        nbt.putInt("Index", this.craftingIndex);
        return nbt;
    }

    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
        return new ContainerCrafting(windowID, playerInventory, this);
    }

    public int craftingIndex() {
        return this.craftingIndex;
    }

    public void increaseIndex() {
        this.craftingIndex++;
    }

    public void decreaseIndex() {
        this.craftingIndex--;
    }

    public void resetIndex() {
        this.craftingIndex = 0;
    }

    public INamedContainerProvider upgradeMenu() {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return TileCrafting.this.getDisplayName();
            }

            @Override
            public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
                return new ContainerUpgrade(windowID, playerInventory, TileCrafting.this);
            }
        };
    }
}
