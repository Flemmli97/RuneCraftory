package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.PlayerData;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;

public class S2CMoney implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CMoney> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_money"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CMoney> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CMoney decode(RegistryFriendlyByteBuf buf) {
            return new S2CMoney(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CMoney pkt) {
            buf.writeInt(pkt.money);
        }
    };

    private final int money;

    private S2CMoney(int money) {
        this.money = money;
    }

    public S2CMoney(PlayerData data) {
        this.money = data.getMoney();
    }

    public static void handle(S2CMoney pkt, Player player) {
        if (player == null)
            return;
        RunecraftoryAttachments.PLAYER_DATA.get().get(player).setMoney(pkt.money);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
