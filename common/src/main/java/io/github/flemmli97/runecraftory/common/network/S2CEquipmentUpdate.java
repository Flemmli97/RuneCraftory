package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;

public record S2CEquipmentUpdate(EquipmentSlot slot) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_equipment");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.slot);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CEquipmentUpdate read(FriendlyByteBuf buf) {
        return new S2CEquipmentUpdate(buf.readEnum(EquipmentSlot.class));
    }

    public static void handle(S2CEquipmentUpdate pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.updateEquipmentStats(player, pkt.slot));
    }
}
