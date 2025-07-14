package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCLook;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.features.NPCFeatureContainer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CNPCLook implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CNPCLook> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("c2s_npc_look"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CNPCLook> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CNPCLook decode(RegistryFriendlyByteBuf buf) {
            return new S2CNPCLook(buf.readInt(), NPCLook.fromBuffer(buf), new NPCFeatureContainer().fromBuffer(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CNPCLook pkt) {
            buf.writeInt(pkt.id);
            pkt.look.writeToBuffer(buf);
            pkt.features.toBuffer(buf);
        }
    };
    private final int id;
    private final NPCLook look;
    private final NPCFeatureContainer features;

    public S2CNPCLook(int id, NPCLook look, NPCFeatureContainer features) {
        this.id = id;
        this.look = look;
        this.features = features;
    }

    public static S2CNPCLook read(RegistryFriendlyByteBuf buf) {
        return new S2CNPCLook(buf.readInt(), NPCLook.fromBuffer(buf), new NPCFeatureContainer().fromBuffer(buf));
    }

    public static void handle(S2CNPCLook pkt, Player player) {
        Entity e = player.level().getEntity(pkt.id);
        if (e instanceof NPCEntity npc) {
            npc.lookFeatures.with(pkt.features);
            npc.setClientLook(pkt.look);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
