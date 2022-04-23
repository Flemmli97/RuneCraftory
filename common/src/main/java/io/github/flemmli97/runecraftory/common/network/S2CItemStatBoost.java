package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record S2CItemStatBoost(
        ItemStatIncrease.Stat stat) implements Packet {

    public static final ResourceLocation ID = new ResourceLocation(RuneCraftory.MODID, "s2c_item_stat_boost");

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.stat);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public static S2CItemStatBoost read(FriendlyByteBuf buf) {
        return new S2CItemStatBoost(buf.readEnum(ItemStatIncrease.Stat.class));
    }

    public static void handle(S2CItemStatBoost pkt) {
        Player player = ClientHandlers.getPlayer();
        if (player == null)
            return;
        Platform.INSTANCE.getPlayerData(player).ifPresent(data -> data.consumeStatBoostItem(player, pkt.stat));
    }
}
