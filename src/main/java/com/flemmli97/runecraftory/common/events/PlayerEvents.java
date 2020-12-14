package com.flemmli97.runecraftory.common.events;

import com.flemmli97.runecraftory.network.PacketHandler;
import com.flemmli97.runecraftory.network.S2CDataPackSync;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents {

    @SubscribeEvent
    public void join(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().world.isRemote) {
            PacketHandler.sendToClient(new S2CDataPackSync(), (ServerPlayerEntity) event.getPlayer());
        }
    }
}
