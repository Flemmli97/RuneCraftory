package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CUpdateNPCData implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CUpdateNPCData> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_npc_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CUpdateNPCData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CUpdateNPCData decode(RegistryFriendlyByteBuf buf) {
            return new S2CUpdateNPCData(buf.readInt(), buf.readNbt(), buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CUpdateNPCData pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeNbt(pkt.hearts);
            buf.writeNbt(pkt.schedule);
        }
    };

    private final int entityID;
    private final CompoundTag hearts;
    private final CompoundTag schedule;

    private S2CUpdateNPCData(int id, CompoundTag tag, CompoundTag schedule) {
        this.entityID = id;
        this.hearts = tag;
        this.schedule = schedule;
    }

    public S2CUpdateNPCData(EntityNPCBase entity, CompoundTag hearts) {
        this.entityID = entity.getId();
        this.hearts = hearts;
        this.schedule = entity.getSchedule().save();
    }

    public static S2CUpdateNPCData read(RegistryFriendlyByteBuf buf) {
        return new S2CUpdateNPCData(buf.readInt(), buf.readNbt(), buf.readNbt());
    }

    public static void handle(S2CUpdateNPCData pkt, Player player) {
        Entity e = player.level().getEntity(pkt.entityID);
        if (e instanceof EntityNPCBase npc) {
            npc.updateFriendPointsFrom(player, pkt.hearts);
            npc.syncActivity(pkt.schedule);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
