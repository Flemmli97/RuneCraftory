package com.flemmli97.runecraftory.common.inventory;

import javax.annotation.Nullable;

import com.flemmli97.runecraftory.common.core.handler.capabilities.PlayerCapProvider;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerInfoScreenSub extends Container
{
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };

    public ContainerInfoScreenSub(EntityPlayer player) {
        InventoryPlayer playerInventory = player.inventory;
        InventorySpells playerSpells = player.getCapability(PlayerCapProvider.PlayerCap, null).getInv();
        for (int k = 0; k < 4; ++k) {
            EntityEquipmentSlot entityequipmentslot = ContainerInfoScreenSub.VALID_EQUIPMENT_SLOTS[k];
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
        for (int m = 0; m < 4; ++m) {
            this.addSlotToContainer(new Slot(playerSpells, m, 64, -12 + m * 18) {
                @Override
				public boolean isItemValid(ItemStack stack) {
                    return playerSpells.isItemValidForSlot(this.getSlotIndex(), stack);
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
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return ItemStack.EMPTY;
    }
}
