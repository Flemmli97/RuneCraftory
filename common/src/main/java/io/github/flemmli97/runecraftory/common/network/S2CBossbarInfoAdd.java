package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.UUID;

public class S2CBossbarInfoAdd implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CBossbarInfoAdd> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_bossbar_info_add"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBossbarInfoAdd> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CBossbarInfoAdd decode(RegistryFriendlyByteBuf buf) {
            return new S2CBossbarInfoAdd(buf.readUUID(), buf.readUUID(), buf.readBoolean() ? buf.readResourceLocation() : null,
                    buf.readBoolean() ? ByteBufCodecs.registry(Registries.SOUND_EVENT).decode(buf) : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CBossbarInfoAdd pkt) {
            buf.writeUUID(pkt.id);
            buf.writeUUID(pkt.musicID);
            buf.writeBoolean(pkt.type != null);
            if (pkt.type != null)
                buf.writeResourceLocation(pkt.type);
            buf.writeBoolean(pkt.sound != null);
            if (pkt.sound != null)
                ByteBufCodecs.registry(Registries.SOUND_EVENT).encode(buf, pkt.sound);
        }
    };

    private final UUID id, musicID;
    private final ResourceLocation type;
    private final SoundEvent sound;

    public S2CBossbarInfoAdd(UUID id, UUID musicID, ResourceLocation type, SoundEvent sound) {
        this.id = id;
        this.musicID = musicID;
        this.type = type;
        this.sound = sound;
    }

    public static void handle(S2CBossbarInfoAdd pkt) {
        BossBarTracker.addActiveBossbar(pkt.id, pkt.musicID, pkt.type, pkt.sound);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
