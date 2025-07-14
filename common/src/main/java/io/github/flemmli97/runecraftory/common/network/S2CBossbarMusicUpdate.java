package io.github.flemmli97.runecraftory.common.network;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.client.BossBarTracker;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundEvent;

import java.util.UUID;

public class S2CBossbarMusicUpdate implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<S2CBossbarMusicUpdate> TYPE = new CustomPacketPayload.Type<>(RuneCraftory.modRes("s2c_bossbar_music_update"));

    public static final StreamCodec<RegistryFriendlyByteBuf, S2CBossbarMusicUpdate> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CBossbarMusicUpdate decode(RegistryFriendlyByteBuf buf) {
            return new S2CBossbarMusicUpdate(buf.readUUID(), buf.readUUID(), buf.readBoolean() ? ByteBufCodecs.registry(Registries.SOUND_EVENT).decode(buf) : null);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, S2CBossbarMusicUpdate pkt) {
            buf.writeUUID(pkt.id);
            buf.writeUUID(pkt.musicID);
            buf.writeBoolean(pkt.sound != null);
            if (pkt.sound != null)
                ByteBufCodecs.registry(Registries.SOUND_EVENT).encode(buf, pkt.sound);
        }
    };

    private final UUID id, musicID;
    private final SoundEvent sound;

    public S2CBossbarMusicUpdate(UUID uuid, UUID musicID) {
        this(uuid, musicID, null);
    }

    public S2CBossbarMusicUpdate(UUID uuid, UUID musicID, SoundEvent sound) {
        this.id = uuid;
        this.musicID = musicID;
        this.sound = sound;
    }

    public static void handle(S2CBossbarMusicUpdate pkt) {
        BossBarTracker.updateMusic(pkt.id, pkt.musicID, pkt.sound);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
