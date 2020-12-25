package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.api.Spell;
import com.flemmli97.runecraftory.common.items.weapons.ItemSpell;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;

public class InventorySpells implements IItemHandlerModifiable {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);

    @Override
    public int getSlots() {
        return this.inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= this.getSlots())
            return ItemStack.EMPTY;
        return this.inventory.get(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (this.inventory.get(slot).isEmpty()) {
            ItemStack copy = stack.copy();
            copy.shrink(1);
            ItemStack toInsert = stack.copy();
            toInsert.setCount(1);
            if (!simulate) {
                this.inventory.set(slot, toInsert);
            }
            return copy;
        }
        return stack;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (simulate)
            return this.getStackInSlot(slot);
        return this.inventory.set(slot, ItemStack.EMPTY);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (this.isItemValid(slot, stack))
            this.inventory.set(slot, stack);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.getItem() instanceof ItemSpell;
    }

    public void readFromNBT(CompoundNBT compound) {
        ItemStackHelper.loadAllItems(compound, this.inventory);
    }

    public CompoundNBT writeToNBT(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.inventory);
        return compound;
    }

    public void useSkill(ServerPlayerEntity player, int index) {
        ItemStack stack = this.getStackInSlot(index);
        if (stack.getItem() instanceof ItemSpell && player.getCooldownTracker().getCooldown(stack.getItem(), 0.0f) <= 0.0f) {
            Spell spell = ((ItemSpell) stack.getItem()).getSpell();
            if (spell.use((ServerWorld) player.world, player, stack)) {
                player.getCooldownTracker().setCooldown(stack.getItem(), spell.coolDown());
                spell.levelSkill(player);
            }
        }
    }

    public void dropItemsAt(LivingEntity entity) {
        if (!entity.world.isRemote) {
            for (ItemStack stack : this.inventory) {
                ItemEntity item = new ItemEntity(entity.world, entity.getX(), entity.getY(), entity.getZ(), stack);
                item.setPickupDelay(0);
                entity.world.addEntity(item);
            }
        }
        this.inventory.clear();
    }

    public void update(PlayerEntity player) {
        for (ItemStack stack : this.inventory) {
            if (stack.getItem() instanceof ItemSpell)
                ((ItemSpell) stack.getItem()).getSpell().update(player, stack);
        }
    }
}
