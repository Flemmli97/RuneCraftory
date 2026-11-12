package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.items.creative.NPCSpawnEgg;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class C2SNPCLookRequest implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<C2SNPCLookRequest> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_npc_look_request"));

    public static final StreamCodec<RegistryFriendlyByteBuf, C2SNPCLookRequest> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public C2SNPCLookRequest decode(RegistryFriendlyByteBuf buf) {
            return new C2SNPCLookRequest(buf.readBoolean() ? buf.readResourceLocation() : null, buf.readBoolean() ? buf.readResourceLocation() : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, C2SNPCLookRequest pkt) {
            buf.writeBoolean(pkt.npcProfession != null);
            if (pkt.npcProfession != null)
                buf.writeResourceLocation(pkt.npcProfession);
            buf.writeBoolean(pkt.npcID != null);
            if (pkt.npcID != null)
                buf.writeResourceLocation(pkt.npcID);
        }
    };

    private final ResourceLocation npcProfession, npcID;

    public C2SNPCLookRequest(@Nullable ResourceLocation npcProfession, @Nullable ResourceLocation npcID) {
        this.npcProfession = npcProfession;
        this.npcID = npcID;
    }

    public static void handle(C2SNPCLookRequest pkt, ServerPlayer sender) {
        NPCSpawnEgg.computeLooks(sender, pkt.npcID, pkt.npcProfession);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
