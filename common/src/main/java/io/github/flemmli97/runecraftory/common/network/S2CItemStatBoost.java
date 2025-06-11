package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.items.tools.ItemStatIncrease;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public record S2CItemStatBoost(ItemStatIncrease.Stat stat, boolean reset) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CItemStatBoost> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_item_stat_boost"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CItemStatBoost> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CItemStatBoost decode(RegistryFriendlyByteBuf buf) {
            return new S2CItemStatBoost(buf.readEnum(ItemStatIncrease.Stat.class), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CItemStatBoost pkt) {
            buf.writeEnum(pkt.stat);
            buf.writeBoolean(pkt.reset);
        }
    };

    public static void handle(S2CItemStatBoost pkt, Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (pkt.reset)
            data.resetAllStatBoost(pkt.stat);
        else
            data.increaseStatBonus(pkt.stat);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
