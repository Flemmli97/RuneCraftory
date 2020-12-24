package com.flemmli97.runecraftory.common.inventory;

import com.flemmli97.runecraftory.common.capability.CapabilityInsts;
import com.flemmli97.runecraftory.common.utils.ItemUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class InventoryShippingBin extends ItemStackHandler {

    public InventoryShippingBin() {
        super(54);
    }

    public void shipItems(PlayerEntity player) {
        player.getCapability(CapabilityInsts.PlayerCap).ifPresent(cap -> {
            int money = 0;
            for (int i = 0; i < this.getSlots(); ++i) {
                if (this.getStackInSlot(i).isEmpty())
                    continue;
                ItemStack stack = this.getStackInSlot(i);
                money += ItemUtils.getSellPrice(stack) * stack.getCount();
                cap.addShippingItem(player, stack);
                this.setStackInSlot(i, ItemStack.EMPTY);
            }
            cap.setMoney(player, cap.getMoney() + money);
            player.sendStatusMessage(new TranslationTextComponent("shipping.money").append("" + money).formatted(TextFormatting.GOLD), true);
        });
    }
}
