package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record C2SSpellKey(int num, boolean release) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SSpellKey> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_cast_spell"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SSpellKey> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SSpellKey decode(RegistryFriendlyByteBuf buf) {
            return new C2SSpellKey(buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SSpellKey pkt) {
            buf.writeInt(pkt.num);
            buf.writeBoolean(pkt.release);
        }
    };

    public static void handle(C2SSpellKey pkt, ServerPlayer sender) {
        if (sender.getVehicle() instanceof BaseMonster)
            ((BaseMonster) sender.getVehicle()).handleRidingCommand(pkt.num);
        else {
            if (pkt.release)
                Platform.INSTANCE.getPlayerData(sender).getInv().onRelease();
            else
                Platform.INSTANCE.getPlayerData(sender).getInv().useSkill(sender, pkt.num);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
