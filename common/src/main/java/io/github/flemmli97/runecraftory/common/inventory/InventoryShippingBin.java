package io.github.flemmli97.runecraftory.common.inventory;

import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.registry.ModCriteria;
import io.github.flemmli97.runecraftory.common.utils.ItemUtils;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.runecraftory.platform.SaveItemContainer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class InventoryShippingBin extends SaveItemContainer {

    public InventoryShippingBin() {
        super(54);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return DataPackHandler.INSTANCE.itemStatManager().get(stack.getItem()).map(ItemStat::getSell).orElse(0) > 0;
    }

    public void shipItems(ServerPlayer player) {
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            int money = 0;
            for (int i = 0; i < this.getContainerSize(); ++i) {
                ItemStack stack = this.getItem(i);
                if (stack.isEmpty())
                    continue;
                int basePrice = ItemUtils.getSellPrice(stack);
                if (basePrice <= 0)
                    continue;
                money += basePrice * stack.getCount();
                data.addShippingItem(stack);
                ModCriteria.SHIPPING_TRIGGER.trigger(player, stack);
                SimpleQuestIntegration.INST().triggerShipping(player, stack);
                this.setItem(i, ItemStack.EMPTY);
            }
            data.setMoney(player, data.getMoney() + money);
            if (money != 0)
                player.displayClientMessage(new TranslatableComponent("runecraftory.shipping.money").append("" + money).withStyle(ChatFormatting.GOLD), true);
        });
    }
}
