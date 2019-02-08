package com.flemmli97.runecraftory.common.inventory;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInfoScreen extends Container
{
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };;
    
    public ContainerInfoScreen(EntityPlayer player) {
        InventoryPlayer playerInventory = player.inventory;
        InventorySpells playerSpells = player.getCapability(PlayerCapProvider.PlayerCap, null).getInv();
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 12 + i1 * 18, 163));
        }
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 12 + j1 * 18, 105 + l * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            EntityEquipmentSlot entityequipmentslot = ContainerInfoScreen.VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new Slot(playerInventory, 36 + (3 - k), -6, -12 + k * 18) {
                @Override
				public int getSlotStackLimit() {
                    return 1;
                }
                
                @Override
				public boolean isItemValid(ItemStack stack) {
                    return stack.getItem().isValidArmor(stack, entityequipmentslot, (Entity)player);
                }
                
                @Override
				public boolean canTakeStack(EntityPlayer playerIn) {
                    ItemStack itemstack = this.getStack();
                    return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
                }
                
                @Override
				@Nullable
                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }
        this.addSlotToContainer(new Slot(playerInventory, 40, 29, 60) {
            @Override
			@Nullable
            @SideOnly(Side.CLIENT)
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
        for (int m = 0; m < 4; ++m) {
            this.addSlotToContainer(new Slot(playerSpells, m, 64, -12 + m * 18) {
                @Override
				public boolean isItemValid(ItemStack stack) {
                    return playerSpells.isItemValidForSlot(this.getSlotIndex(), stack);
                }
            });
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index >= 1 && index < 5) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 5 && index < 9) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (entityequipmentslot.getSlotType() == EntityEquipmentSlot.Type.ARMOR && !this.inventorySlots.get(8 - entityequipmentslot.getIndex()).getHasStack()) {
                int i = 8 - entityequipmentslot.getIndex();
                if (!this.mergeItemStack(itemstack2, i, i + 1, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (entityequipmentslot == EntityEquipmentSlot.OFFHAND && !this.inventorySlots.get(45).getHasStack()) {
                if (!this.mergeItemStack(itemstack2, 45, 46, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 9 && index < 36) {
                if (!this.mergeItemStack(itemstack2, 36, 45, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (index >= 36 && index < 45) {
                if (!this.mergeItemStack(itemstack2, 9, 36, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            ItemStack itemstack3 = slot.onTake(playerIn, itemstack2);
            if (index == 0) {
                playerIn.dropItem(itemstack3, false);
            }
        }
        return itemstack;
    }
}
