package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.network.Packet;
import io.github.flemmli97.runecraftory.common.network.S2CBossbarInfoAdd;
import io.github.flemmli97.runecraftory.common.network.S2CBossbarInfoRemove;
import io.github.flemmli97.runecraftory.common.network.S2CBossbarMusicUpdate;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class RunecraftoryBossbar extends ServerBossEvent {

    public final ResourceLocation type;
    private SoundEvent music;
    private UUID musicID;

    public RunecraftoryBossbar(@Nullable ResourceLocation type, Component name, BossBarColor color, BossBarOverlay overlay) {
        super(name, color, overlay);
        this.type = type;
    }

    public RunecraftoryBossbar setMusic(SoundEvent music) {
        if (this.music != music) {
            this.music = music;
            this.broadcast(false);
        }
        return this;
    }

    public RunecraftoryBossbar setMusicID(UUID uuid) {
        if (this.musicID == null || !this.musicID.equals(uuid)) {
            this.musicID = uuid;
            this.broadcast(false);
        }
        return this;
    }

    public UUID getMusicId() {
        return this.musicID != null ? this.musicID : this.getId();
    }

    @Override
    public void addPlayer(ServerPlayer player) {
        boolean contained = this.getPlayers().contains(player);
        super.addPlayer(player);
        if (!contained)
            this.broadcast(false);
    }

    @Override
    public void removePlayer(ServerPlayer player) {
        if (this.getPlayers().contains(player))
            this.broadcast(true);
        super.removePlayer(player);
    }

    /**
     * Removes the player from this bossbar but makes it so the music fades out.
     * Used when the player moves out of the bosses range
     */
    public void removePlayerFading(ServerPlayer player) {
        if (this.getPlayers().contains(player))
            this.broadcast(true, false);
        super.removePlayer(player);
    }

    @Override
    public void setVisible(boolean visible) {
        boolean prev = this.isVisible();
        super.setVisible(visible);
        if (visible != prev)
            this.broadcast(!visible);
    }

    @Override
    public void setProgress(float progress) {
        progress = Math.max(0, progress);
        float prev = this.progress;
        super.setProgress(progress);
        if (this.isVisible()) {
            if (prev != 0 && progress == 0) {
                S2CBossbarMusicUpdate pkt = new S2CBossbarMusicUpdate(this.getId(), this.getMusicId(), true);
                for (ServerPlayer serverPlayer : this.getPlayers()) {
                    Platform.INSTANCE.sendToClient(pkt, serverPlayer);
                }
            } else if (prev == 0 && progress != 0) {
                S2CBossbarMusicUpdate pkt = new S2CBossbarMusicUpdate(this.getId(), this.getMusicId(), false);
                for (ServerPlayer serverPlayer : this.getPlayers()) {
                    Platform.INSTANCE.sendToClient(pkt, serverPlayer);
                }
            }
        }
    }

    private void broadcast(boolean remove) {
        this.broadcast(remove, true);
    }

    private void broadcast(boolean remove, boolean immediate) {
        if (this.isVisible()) {
            Packet pkt = remove ? new S2CBossbarInfoRemove(this.getId(), immediate) : new S2CBossbarInfoAdd(this.getId(), this.getMusicId(), this.type, this.music);
            for (ServerPlayer serverPlayer : this.getPlayers()) {
                Platform.INSTANCE.sendToClient(pkt, serverPlayer);
            }
        }
    }
}
