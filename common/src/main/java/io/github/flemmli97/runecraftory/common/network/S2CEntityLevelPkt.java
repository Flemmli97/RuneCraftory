package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.XpLevelHolder;
import io.github.flemmli97.runecraftory.common.entities.utils.IBaseMob;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class S2CEntityLevelPkt implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CEntityLevelPkt> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_entity.level"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntityLevelPkt> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CEntityLevelPkt decode(RegistryFriendlyByteBuf buf) {
            return new S2CEntityLevelPkt(buf.readInt(), new XpLevelHolder(buf));
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CEntityLevelPkt pkt) {
            buf.writeInt(pkt.entityID);
            pkt.level.toPacket(buf);
        }
    };

    private final int entityID;
    private final XpLevelHolder level;

    private S2CEntityLevelPkt(int id, XpLevelHolder level) {
        this.entityID = id;
        this.level = level;
    }

    public static <T extends Entity & IBaseMob> S2CEntityLevelPkt create(T entity) {
        return new S2CEntityLevelPkt(entity.getId(), entity.xpLevel());
    }

    public static void handle(S2CEntityLevelPkt pkt, Player player) {
        Entity e = player.level().getEntity(pkt.entityID);
        if (e instanceof IBaseMob mob) {
            mob.xpLevel().from(pkt.level);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
