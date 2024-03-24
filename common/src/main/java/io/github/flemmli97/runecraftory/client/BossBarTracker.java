package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.audio.Channel;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.mixin.SoundManagerAccessor;
import io.github.flemmli97.runecraftory.mixinhelper.SoundEngineUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class BossBarTracker {

    private static final Map<ResourceLocation, ClientBossBarType> BOSS_BARS = new HashMap<>();
    private static final Map<UUID, BossBarData> ACTIVE_BOSS_BARS = new HashMap<>();
    private static final Map<UUID, TickingSoundChannel> FADING_CHANNEL = new HashMap<>();

    public static void registerCustomBossbarType(ResourceLocation id, ClientBossBarType type) {
        BOSS_BARS.put(id, type);
    }

    public static void register() {

    }

    public static void tickSounds() {
        FADING_CHANNEL.values().removeIf(TickingSoundChannel::tick);
    }

    public static void addActiveBossbar(UUID id, ResourceLocation type, SoundEvent music) {
        // Remove old ones
        BossBarData old = ACTIVE_BOSS_BARS.remove(id);
        SoundInstance soundInstance = null;
        boolean playMusic = true;
        if (old != null) {
            if (old.music != null) {
                if (music == null || !old.music.getLocation().equals(music.getLocation()))
                    Minecraft.getInstance().getSoundManager().stop(old.music);
            }
        }
        TickingSoundChannel ch = FADING_CHANNEL.get(id);
        if (ch != null) {
            ch.reverse(true);
            playMusic = false;
            soundInstance = ch.inst;
        }
        BossBarData data = new BossBarData(type, soundInstance != null ? soundInstance : createSound(music));
        ACTIVE_BOSS_BARS.put(id, data);
        if (playMusic && data.music != null) {
            Minecraft.getInstance().getSoundManager().play(data.music);
        }
    }

    public static void stopMusic(UUID id, boolean stop) {
        BossBarData data = ACTIVE_BOSS_BARS.get(id);
        if (data != null && data.music != null) {
            if (stop)
                Minecraft.getInstance().getSoundManager().stop(data.music);
            else
                Minecraft.getInstance().getSoundManager().play(data.music);
        }
    }

    public static void removeActiveBossbar(UUID id, boolean immediate) {
        BossBarData data = ACTIVE_BOSS_BARS.remove(id);
        if (data != null && data.music != null) {
            if (immediate || ClientConfig.bossMusicFadeDelay == 0) {
                FADING_CHANNEL.remove(id);
                Minecraft.getInstance().getSoundManager().stop(data.music);
            } else {
                TickingSoundChannel old = FADING_CHANNEL.get(id);
                if (old != null) {
                    old.reverse(false);
                } else {
                    TickingSoundChannel ch = new TickingSoundChannel(data.music, ClientConfig.bossMusicFadeDelay);
                    FADING_CHANNEL.put(id, ch);
                }
            }
        }
    }

    public static int tryRenderCustomBossbar(PoseStack poseStack, int x, int y, BossEvent bossEvent) {
        BossBarData data = ACTIVE_BOSS_BARS.get(bossEvent.getId());
        if (data != null) {
            ClientBossBarType type = BOSS_BARS.get(data.type);
            if (type != null)
                return type.renderFrom(poseStack, x, y, bossEvent);
        }
        return 0;
    }

    public static SimpleSoundInstance createSound(SoundEvent sound) {
        if (sound == null || !ClientConfig.bossMusic)
            return null;
        return new SimpleSoundInstance(sound.getLocation(), SoundSource.RECORDS, 1, 1, true, 0, SoundInstance.Attenuation.LINEAR, 0.0, 0.0, 0.0, true);
    }

    public record BossBarData(ResourceLocation type, SoundInstance music) {
    }

    public static class TickingSoundChannel {

        private final SoundInstance inst;
        private final ChannelAccess.ChannelHandle channel;
        private final int fadeTime;
        private final float volDecrease;
        private int tick;
        private boolean reverse;

        public TickingSoundChannel(SoundInstance inst, int fadeTime) {
            this.inst = inst;
            SoundEngineUtil engine = (SoundEngineUtil) ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
            this.channel = engine.getHandle(inst);
            this.fadeTime = fadeTime;
            this.tick = 0;
            this.volDecrease = 1f / this.fadeTime;
        }

        public boolean tick() {
            if (this.channel == null)
                return true;
            boolean done = this.tick > this.fadeTime || this.tick < 0;
            if (this.reverse)
                this.tick--;
            else
                this.tick++;
            float volMult = Math.max(0, 1 - this.volDecrease * this.tick);
            this.channel.execute(ch -> ch.setVolume(this.inst.getVolume() * volMult));
            if (done && !this.reverse)
                Minecraft.getInstance().getSoundManager().stop(this.inst);
            return done;
        }

        public void reverse(boolean reverse) {
            this.reverse = reverse;
        }
    }

    public record ClientBossBarType(BossbarTexture texture, BossbarTexture overlay) {

        public int renderFrom(PoseStack poseStack, int x, int y, BossEvent bossEvent) {
            return 0;
        }
    }

    public record BossbarTexture(ResourceLocation texture, int width, int height, int offsetX, int offsetY) {
    }
}
