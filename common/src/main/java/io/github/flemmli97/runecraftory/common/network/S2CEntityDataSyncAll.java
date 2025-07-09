package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.EntityData;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class S2CEntityDataSyncAll implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CEntityDataSyncAll> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_entity_data_all"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntityDataSyncAll> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CEntityDataSyncAll decode(RegistryFriendlyByteBuf buf) {
            return new S2CEntityDataSyncAll(buf.readInt(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CEntityDataSyncAll pkt) {
            buf.writeInt(pkt.entityID);
            buf.writeBoolean(pkt.sleeping);
            buf.writeBoolean(pkt.paralysis);
            buf.writeBoolean(pkt.cold);
            buf.writeBoolean(pkt.poison);
            buf.writeBoolean(pkt.stunned);
        }
    };

    private final boolean sleeping, paralysis, cold, poison, stunned;
    private final int entityID;

    public S2CEntityDataSyncAll(LivingEntity entity) {
        this.entityID = entity.getId();
        EntityData data = Platform.INSTANCE.getEntityData(entity);
        this.sleeping = data.isSleeping();
        this.paralysis = data.isParalysed();
        this.stunned = data.isStunned();
        this.cold = data.hasCold();
        this.poison = data.isPoisoned();
    }

    private S2CEntityDataSyncAll(int entityID, boolean sleeping, boolean paralysis, boolean cold, boolean poison, boolean stunned) {
        this.entityID = entityID;
        this.sleeping = sleeping;
        this.paralysis = paralysis;
        this.cold = cold;
        this.poison = poison;
        this.stunned = stunned;
    }

    public static void handle(S2CEntityDataSyncAll pkt, Player player) {
        Entity e = player.level().getEntity(pkt.entityID);
        if (e instanceof LivingEntity living) {
            EntityData data = Platform.INSTANCE.getEntityData(living);
            data.setSleeping(living, pkt.sleeping);
            data.setParalysis(living, pkt.paralysis);
            data.setCold(living, pkt.cold);
            data.setPoison(living, pkt.poison);
            data.setStunned(living, pkt.stunned);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
