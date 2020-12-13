package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.common.entities.BaseMonster;
import com.flemmli97.runecraftory.network.C2SRideJump;
import com.flemmli97.runecraftory.network.PacketHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEvents {

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new ClientEvents());
    }

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }
}
