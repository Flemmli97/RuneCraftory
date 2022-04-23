package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record S2CFoodPkt(ItemStack stack) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_food");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.stack != null);
        if (this.stack != null)
            buf.writeItem(this.stack);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CFoodPkt read(FriendlyByteBuf buf) {
        if (buf.readBoolean())
            return new S2CFoodPkt(buf.readItem());
        return new S2CFoodPkt(null);
    }

    public static void handle(S2CFoodPkt pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (pkt.stack == null)
                data.removeFoodEffect(player);
            else
                data.applyFoodEffect(player, pkt.stack);
        });
    }
}
