package io.github.flemmli97.runecraftory.common.inventory;

import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventoryShippingBin extends SaveItemContainer {

    public InventoryShippingBin() {
        super(54);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return DataPackHandler.getStats(stack.getItem()).isPresent();
    }

    public void shipItems(Player player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            int money = 0;
            for (int i = 0; i < this.getContainerSize(); ++i) {
                ItemStack stack = this.getItem(i);
                if (stack.isEmpty())
                    continue;
                money += ItemUtils.getSellPrice(stack) * stack.getCount();
                data.addShippingItem(player, stack);
                this.setItem(i, ItemStack.EMPTY);
            }
            data.setMoney(player, data.getMoney() + money);
            player.displayClientMessage(new TranslatableComponent("shipping.money").append("" + money).withStyle(ChatFormatting.GOLD), true);
        });
    }
}
