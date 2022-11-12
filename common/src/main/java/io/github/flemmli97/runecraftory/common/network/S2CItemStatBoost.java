package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record S2CItemStatBoost(
        ItemStatIncrease.Stat stat, boolean reset) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_item_stat_boost");

    public static S2CItemStatBoost read(FriendlyByteBuf buf) {
        return new S2CItemStatBoost(buf.readEnum(ItemStatIncrease.Stat.class), buf.readBoolean());
    }

    public static void handle(S2CItemStatBoost pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> {
            if (pkt.reset)
                data.resetAllStatBoost(player, pkt.stat);
            else
                data.increaseStatBonus(player, pkt.stat);
        });
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.stat);
        buf.writeBoolean(this.reset);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
