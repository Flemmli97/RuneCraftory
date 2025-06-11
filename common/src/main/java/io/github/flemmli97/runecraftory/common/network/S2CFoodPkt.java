package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record S2CFoodPkt(ItemStack stack) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CFoodPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_food"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CFoodPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CFoodPkt decode(RegistryFriendlyByteBuf buf) {
            if (buf.readBoolean())
                return new S2CFoodPkt(ItemStack.STREAM_CODEC.decode(buf));
            return new S2CFoodPkt(null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CFoodPkt pkt) {
            buf.writeBoolean(pkt.stack != null);
            if (pkt.stack != null)
                ItemStack.STREAM_CODEC.encode(buf, pkt.stack);
        }
    };

    public static void handle(S2CFoodPkt pkt, Player player) {
        PlayerData data = Platform.INSTANCE.getPlayerData(player);
        if (pkt.stack == null)
            data.removeFoodEffect(player);
        else
            data.applyFoodEffect(pkt.stack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
