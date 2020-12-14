package com.flemmli97.runecraftory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;

public class ClientHandlers {

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }
}
