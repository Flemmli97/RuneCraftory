package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.client.gui.OverlayGui;
import com.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import com.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class ClientHandlers {

    public static final CalendarImpl clientCalendar = new CalendarImpl();

    public static OverlayGui overlay;
    public static SpellInvOverlayGui spellDisplay;
    public static KeyBinding spell1;
    public static KeyBinding spell2;
    public static KeyBinding spell3;
    public static KeyBinding spell4;
    public static KeyBinding ride0;
    public static KeyBinding ride1;
    public static KeyBinding ride2;

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(PacketBuffer buffer) {
        clientCalendar.fromPacket(buffer);
    }
}
