package io.github.flemmli97.runecraftory.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.runecraftory.mixin.SoundManagerAccessor;
import io.github.flemmli97.runecraftory.mixinhelper.SoundEngineUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarTracker {

    private static final Map<ResourceLocation, ClientBossBarType> BOSS_BARS = new HashMap<>();
    private static final Map<UUID, BossBarData> ACTIVE_BOSS_BARS = new HashMap<>();
    private static final Map<UUID, BossSoundInstance> FADING_CHANNEL = new HashMap<>();
    private static SoundInstance activeMusic;
    private static int lastPlay, tick;

    public static void registerCustomBossbarType(ResourceLocation id, ClientBossBarType type) {
        BOSS_BARS.put(id, type);
    }

    public static void register() {

    }

    public static void tickSounds() {
        tick++;
        FADING_CHANNEL.values().removeIf(BossSoundInstance::done);
    }

    public static void addActiveBossbar(UUID id, ResourceLocation type, SoundEvent music) {
        BossBarData old = ACTIVE_BOSS_BARS.get(id);
        // If an old one exist adjust the old one
        if (old != null) {
            if(old.music != null) {
                // Update old music
                if (music == null || !old.music.getLocation().equals(music.getLocation())) {
                    Minecraft.getInstance().getSoundManager().stop(old.music);
                    old.music = createSound(id, music);
                    playMusic(old.music);
                }
            }
            return;
        }
        // Try lookup if the music is fading away
        BossSoundInstance inst = FADING_CHANNEL.get(id);
        if (inst != null) {
            inst.reverse(true);
        }
        BossBarData data = new BossBarData(type, inst != null ? inst : createSound(id, music));
        if (data.music != null && inst == null) {
            playMusic(data.music);
        }
        ACTIVE_BOSS_BARS.put(id, data);
    }

    public static void updateMusic(UUID id, boolean stop) {
        BossBarData data = ACTIVE_BOSS_BARS.get(id);
        if (data != null && data.music != null) {
            data.music.stop(stop);
            if (stop)
                stopMusic(data.music);
            else {
                playMusic(data.music);
            }
        }
    }

    public static void removeActiveBossbar(UUID id, boolean immediate) {
        BossBarData data = ACTIVE_BOSS_BARS.remove(id);
        if (data != null && data.music != null) {
            if (immediate || ClientConfig.bossMusicFadeDelay == 0) {
                // Stop the sound without fading away
                FADING_CHANNEL.remove(id);
                stopMusic(data.music);
            } else {
                BossSoundInstance inst = FADING_CHANNEL.get(id);
                if (inst != null) {
                    inst.reverse(false);
                } else {
                    FADING_CHANNEL.put(id, data.music.reverse(false));
                }
            }
        }
    }

    public static int tryRenderCustomBossbar(PoseStack poseStack, int x, int y, BossEvent bossEvent, boolean withName) {
        BossBarData data = ACTIVE_BOSS_BARS.get(bossEvent.getId());
        if (data != null) {
            ClientBossBarType type = BOSS_BARS.get(data.type);
            if (type != null)
                return type.renderFrom(poseStack, x, y, bossEvent, withName);
        }
        return 0;
    }

    public static BossSoundInstance createSound(UUID id, SoundEvent sound) {
        if (sound == null || !ClientConfig.bossMusic)
            return null;
        return new BossSoundInstance(id, sound, SoundSource.RECORDS, 1, 1, ClientConfig.bossMusicFadeDelay);
    }

    private static void playMusic(SoundInstance sound) {
        // Attempting to play more than one at the same time will make it unable to stop the previous one so we just cancel it
        if (lastPlay == tick)
            return;
        lastPlay = tick;
        if (activeMusic != null) {
            Minecraft.getInstance().getSoundManager().stop(activeMusic);
        }
        Minecraft.getInstance().getSoundManager().play(sound);
        activeMusic = sound;
    }

    private static void stopMusic(SoundInstance sound) {
        if (activeMusic == sound) {
            Minecraft.getInstance().getSoundManager().stop(sound);
            // Find any other active boss music to play
            ACTIVE_BOSS_BARS.values().stream().filter(d -> d.music != sound && !d.music.stopped).findFirst()
                    .ifPresent(d -> {
                        Minecraft.getInstance().getSoundManager().play(d.music);
                        activeMusic = d.music;
                    });
        }
    }

    public static class BossBarData {

        public final ResourceLocation type;
        public BossSoundInstance music;

        public BossBarData(ResourceLocation type, BossSoundInstance music) {
            this.type = type;
            this.music = music;
        }
    }

    public static class BossSoundInstance extends AbstractTickableSoundInstance {

        public final UUID id;
        private final int fadeTime;
        private final float defaultVol, volDecrease;
        private int tick = 1;
        private boolean reverse, fade;
        // A flag to check if this sound instance is stopped. This differs from AbstractTickableSoundInstance#stopped as this can be continued again
        private boolean stopped;

        public BossSoundInstance(UUID id, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, int fadeTime) {
            super(soundEvent, soundSource);
            this.id = id;
            this.volume = volume;
            this.pitch = pitch;
            this.fadeTime = fadeTime;
            this.defaultVol = Mth.clamp(volume * getVolume(soundSource), 0.0f, 1.0f);
            this.volDecrease = 1f / this.fadeTime;
            this.looping = true;
            this.relative = true;
        }

        private static float getVolume(@Nullable SoundSource category) {
            if (category == null || category == SoundSource.MASTER) {
                return 1.0f;
            }
            return Minecraft.getInstance().options.getSoundSourceVolume(category);
        }

        public BossSoundInstance reverse(boolean reverse) {
            this.reverse = reverse;
            this.tick = Mth.clamp(this.tick, 0, this.fadeTime);
            this.fade = true;
            return this;
        }

        public boolean done() {
            return this.tick > this.fadeTime || this.tick < 0;
        }

        public void stop(boolean stopped) {
            this.stopped = stopped;
        }

        @Override
        public void tick() {
            if (!this.fade)
                return;
            boolean done = this.done();
            if (this.reverse)
                --this.tick;
            else
                ++this.tick;
            this.volume = this.defaultVol * Mth.clamp(1 - this.volDecrease * this.tick, 0, 1);
            if (done) {
                if (!this.reverse) {
                    stopMusic(this);
                    this.looping = false;
                }
                this.fade = false;
            }
        }
    }

    public static class TickingSoundChannel {

        private final SoundInstance inst;
        private final ChannelAccess.ChannelHandle channel;
        private final int fadeTime;
        private final float defaultVol, volDecrease;
        private int tick;
        private boolean reverse;

        public TickingSoundChannel(SoundInstance inst, int fadeTime) {
            this.inst = inst;
            SoundEngineUtil engine = (SoundEngineUtil) ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).getSoundEngine();
            this.channel = engine.getHandle(inst);
            this.fadeTime = fadeTime;
            this.tick = 0;
            this.volDecrease = 1f / this.fadeTime;
            this.defaultVol = calculateVolume(inst);
        }

        private static float calculateVolume(SoundInstance sound) {
            return Mth.clamp(sound.getVolume() * getVolume(sound.getSource()), 0.0f, 1.0f);
        }

        private static float getVolume(@Nullable SoundSource category) {
            if (category == null || category == SoundSource.MASTER) {
                return 1.0f;
            }
            return Minecraft.getInstance().options.getSoundSourceVolume(category);
        }

        public boolean tick() {
            if (this.channel == null)
                return true;
            boolean done = this.tick > this.fadeTime || this.tick < 0;
            if (this.reverse)
                this.tick--;
            else
                this.tick++;
            float vol = this.defaultVol * Mth.clamp(1 - this.volDecrease * this.tick, 0, 1);
            this.channel.execute(ch -> ch.setVolume(vol));
            if (done && !this.reverse)
                stopMusic(this.inst);
            return done;
        }

        public void reverse(boolean reverse) {
            this.reverse = reverse;
        }
    }

    public record ClientBossBarType(BossbarTexture texture, BossbarTexture overlay) {

        public int renderFrom(PoseStack poseStack, int x, int y, BossEvent bossEvent, boolean withName) {
            Minecraft mc = Minecraft.getInstance();
            if (withName) {
                int screenX = mc.getWindow().getGuiScaledWidth();
                Component component = bossEvent.getName();
                int len = mc.font.width(component);
                int txtX = screenX / 2 - len / 2;
                mc.font.drawShadow(poseStack, component, txtX, y - 9, 0xFFFFFF);
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShaderTexture(0, this.texture.texture);
            GuiComponent.blit(poseStack, x, y, 0, this.texture.offsetX, this.texture.offsetY, this.texture.width, this.texture.height, 256, 256);

            RenderSystem.setShaderTexture(0, this.overlay.texture);
            int overlayWidth = (int) (bossEvent.getProgress() * this.overlay.width);
            GuiComponent.blit(poseStack, x, y, 0, this.overlay.offsetX, this.overlay.offsetY, overlayWidth, this.overlay.height, 256, 256);
            return y + mc.font.lineHeight + Math.max(this.texture.height, this.overlay.height) + 5;
        }
    }

    public record BossbarTexture(ResourceLocation texture, int width, int height, int offsetX, int offsetY) {
    }
}
