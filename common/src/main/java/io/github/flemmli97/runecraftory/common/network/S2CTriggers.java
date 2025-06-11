package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.ClientHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

/**
 * Simple triggers from server to client where client does some predefined stuff.
 * E.g. adding bonemeal particles. (Instead of sending multiple particle packets)
 */
public record S2CTriggers(TriggerType triggerType, BlockPos pos) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CTriggers> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_triggers"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CTriggers> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CTriggers decode(RegistryFriendlyByteBuf buf) {
            return new S2CTriggers(buf.readEnum(TriggerType.class), new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CTriggers pkt) {
            buf.writeEnum(pkt.triggerType);
            buf.writeInt(pkt.pos.getX());
            buf.writeInt(pkt.pos.getY());
            buf.writeInt(pkt.pos.getZ());
        }
    };

    public static void handle(S2CTriggers pkt) {
        ClientHandlers.handleTriggers(pkt.triggerType, pkt.pos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum TriggerType {
        FERTILIZER
    }
}
