package com.flemmli97.runecraftory.mobs.events;

import com.flemmli97.runecraftory.mobs.entity.BaseMonster;
import com.flemmli97.runecraftory.mobs.network.C2SRideJump;
import com.flemmli97.tenshilib.common.network.PacketHandler;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MobClientEvents {

    @SubscribeEvent
    public void jump(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof ClientPlayerEntity && e.getEntityLiving().getRidingEntity() instanceof BaseMonster && ((ClientPlayerEntity) e.getEntityLiving()).movementInput.jump)
            PacketHandler.sendToServer(new C2SRideJump());
    }
}
