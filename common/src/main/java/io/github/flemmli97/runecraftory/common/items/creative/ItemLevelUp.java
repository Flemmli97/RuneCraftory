package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemLevelUp extends Item {

    public ItemLevelUp(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        if (!world.isClientSide) {
            Platform.INSTANCE.getPlayerData(player).ifPresent(data ->
                    data.addXp(player, LevelCalc.xpAmountForLevelUp(data.getPlayerLevel()[0]) - data.getPlayerLevel()[1]));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
