package io.github.flemmli97.runecraftory.common.items.tools;

import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class ItemCommandStaff extends Item {

    public ItemCommandStaff(Item.Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        if (useOnContext.getPlayer() instanceof ServerPlayer player) {
            PlayerData data = RunecraftoryAttachments.PLAYER_DATA.get().get(useOnContext.getPlayer());
            if (data.entitySelector.apply != null)
                data.entitySelector.apply.accept(player, useOnContext.getClickedPos().immutable());
        }
        return super.useOn(useOnContext);
    }
}
