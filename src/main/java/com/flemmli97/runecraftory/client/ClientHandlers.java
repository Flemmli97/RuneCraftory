package com.flemmli97.runecraftory.client;

import com.flemmli97.runecraftory.client.gui.OverlayGui;
import com.flemmli97.runecraftory.client.gui.SpellInvOverlayGui;
import com.flemmli97.runecraftory.common.utils.CalendarImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;

public class ClientHandlers {

    public static final CalendarImpl clientCalendar = new CalendarImpl();

    public static OverlayGui overlay;
    public static SpellInvOverlayGui spellDisplay;
    public static TriggerKeyBind spell1;
    public static TriggerKeyBind spell2;
    public static TriggerKeyBind spell3;
    public static TriggerKeyBind spell4;

    public static PlayerEntity getPlayer() {
        return Minecraft.getInstance().player;
    }

    public static void updateClientCalendar(PacketBuffer buffer) {
        clientCalendar.fromPacket(buffer);
    }


}
