package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.entities.npc.job.NPCJob;
import io.github.flemmli97.runecraftory.common.registry.ModNPCJobs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CUpdateNPCData implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CUpdateNPCData> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_npc_data"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CUpdateNPCData> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CUpdateNPCData decode(RegistryFriendlyByteBuf buf) {
            return new S2CUpdateNPCData(buf.readInt(), buf.readNbt(), buf.readNbt(), ByteBufCodecs.registry(ModNPCJobs.JOB_REGISTRY_KEY)
                    .decode(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CUpdateNPCData pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeNbt(pkt.hearts);
            buf.writeNbt(pkt.schedule);
            ByteBufCodecs.registry(ModNPCJobs.JOB_REGISTRY_KEY)
                    .encode(buf, pkt.job);
        }
    };

    private final int entityID;
    private final CompoundTag hearts;
    private final CompoundTag schedule;
    private final NPCJob job;

    private S2CUpdateNPCData(int id, CompoundTag tag, CompoundTag schedule, NPCJob job) {
        this.entityID = id;
        this.hearts = tag;
        this.schedule = schedule;
        this.job = job;
    }

    public S2CUpdateNPCData(EntityNPCBase entity, CompoundTag hearts) {
        this.entityID = entity.getId();
        this.hearts = hearts;
        this.schedule = entity.getNPCSchedule().save();
        this.job = entity.getShop();
    }

    public static void handle(S2CUpdateNPCData pkt, Player player) {
        Entity e = player.level().getEntity(pkt.entityID);
        if (e instanceof EntityNPCBase npc) {
            npc.handleUpdatePacket(player, pkt.hearts, pkt.schedule, pkt.job);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
