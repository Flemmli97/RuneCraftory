package io.github.flemmli97.runecraftory.common.items.creative;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(player);
            data.addXp(GeneralConfig.experienceLevel.xpAmountForNext(data.getPlayerLevel().getLevel()) - data.getPlayerLevel().getXp());
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
