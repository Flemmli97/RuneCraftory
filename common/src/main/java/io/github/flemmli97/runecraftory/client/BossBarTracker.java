package io.github.flemmli97.runecraftory.client;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BossBarTracker {

    private static final Map<ResourceLocation, ClientBossBarType> BOSS_BARS = new HashMap<>();
    // The bossbars that are visible on the screen for the player
    private static final Map<UUID, BossBarData> ACTIVE_BOSS_BARS = new HashMap<>();
    private static final Map<UUID, BossSoundInstance> ACTIVE_BOSS_BGM = new HashMap<>();
    private static SoundInstance activeMusic;
    private static int lastPlay, tick;

    public static void registerCustomBossbarType(ResourceLocation id, ClientBossBarType type) {
        BOSS_BARS.put(id, type);
    }

    public static void register() {

    }

    public static void tickSounds() {
        tick++;
        ACTIVE_BOSS_BGM.values().removeIf(inst -> {
            if (inst.isStopped()) {
                stopMusic(inst);
                return true;
            }
            return false;
        });
    }

    public static void addActiveBossbar(UUID id, UUID musicID, ResourceLocation type, SoundEvent music) {
        BossBarData old = ACTIVE_BOSS_BARS.get(id);
        // If an old one exist adjust the old one
        if (old != null) {
            BossSoundInstance sound;
            if (old.music != null && (sound = ACTIVE_BOSS_BGM.get(old.music)) != null) {
                // Update old music if music changed
                if (music == null || !sound.getLocation().equals(music.getLocation())) {
                    sound.removeBossBar(id, true);
                    // Generate and play the changed music
                    BossSoundInstance bgm = createSound(musicID, music);
                    old.music = musicID;
                    ACTIVE_BOSS_BGM.put(musicID, bgm);
                    playMusic(bgm);
                }
            }
            return;
        }
        BossSoundInstance inst = ACTIVE_BOSS_BGM.get(musicID);
        if (inst != null) {
            inst.linkBossBar(id, false);
        } else {
            inst = createSound(musicID, music);
            inst.linkBossBar(id, true);
            playMusic(inst);
            ACTIVE_BOSS_BGM.put(musicID, inst);
        }
        BossBarData data = new BossBarData(type, musicID);
        ACTIVE_BOSS_BARS.put(id, data);
    }

    public static void updateMusic(UUID id, UUID musicID, SoundEvent sound) {
        BossSoundInstance bgm = ACTIVE_BOSS_BGM.get(musicID);
        if (bgm != null) {
            if (sound == null) {
                bgm.removeBossBar(id, true);
            } else {
                boolean empty = bgm.instances.isEmpty();
                bgm.linkBossBar(id, false);
                if (empty) {
                    playMusic(bgm);
                }
            }
        } else if (sound != null) {
            BossSoundInstance inst = createSound(musicID, sound);
            inst.linkBossBar(id, true);
            playMusic(inst);
            ACTIVE_BOSS_BGM.put(musicID, inst);
        }
    }

    public static void removeActiveBossbar(UUID id, boolean immediate) {
        BossBarData data = ACTIVE_BOSS_BARS.remove(id);
        if (data != null && data.music != null) {
            BossSoundInstance sound = ACTIVE_BOSS_BGM.get(data.music);
            if (sound == null)
                return;
            sound.removeBossBar(id, immediate);
        }
    }

    public static BossSoundInstance createSound(UUID id, SoundEvent sound) {
        if (sound == null || !ClientConfig.bossMusic)
            return null;
        return new BossSoundInstance(id, sound, SoundSource.RECORDS, 1, 1, ClientConfig.bossMusicFadeDelay);
    }

    private static void playMusic(BossSoundInstance sound) {
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

    private static void stopMusic(BossSoundInstance sound) {
        Minecraft.getInstance().getSoundManager().stop(sound);
        if (activeMusic == sound) {
            // Find any other active boss music to play
            ACTIVE_BOSS_BGM.values().stream().filter(bgm -> bgm != sound && !bgm.instances.isEmpty()).findFirst()
                    .ifPresent(bgm -> {
                        Minecraft.getInstance().getSoundManager().play(bgm);
                        activeMusic = bgm;
                    });
        }
    }

    public static boolean hasActiveMusic() {
        return activeMusic != null;
    }

    public static int tryRenderCustomBossbar(GuiGraphics graphics, int x, int y, BossEvent bossEvent, boolean withName) {
        BossBarData data = ACTIVE_BOSS_BARS.get(bossEvent.getId());
        if (data != null) {
            ClientBossBarType type = BOSS_BARS.get(data.type);
            if (type != null)
                return type.renderFrom(graphics, x, y, bossEvent, withName);
        }
        return 0;
    }

    public static class BossBarData {

        public final ResourceLocation type;
        public UUID music;

        public BossBarData(ResourceLocation type, UUID music) {
            this.type = type;
            this.music = music;
        }
    }

    public static class BossSoundInstance extends AbstractTickableSoundInstance {

        public final UUID id;

        private final int fadeTime;
        private final float defaultVol, volDecrease;
        private int tick = 1;
        private boolean fadeAway, adjustingVolume;

        // Amount of bossbars assigned to this sound instance
        private final Set<UUID> instances = new HashSet<>();

        public BossSoundInstance(UUID id, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch, int fadeTime) {
            super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
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

        public void linkBossBar(UUID barId, boolean init) {
            this.instances.add(barId);
            if (!init)
                this.setFadeState(true);
        }

        public void removeBossBar(UUID barId, boolean immediate) {
            this.instances.remove(barId);
            if (this.instances.isEmpty()) {
                if (immediate) {
                    this.stop();
                } else
                    this.setFadeState(false);
            }
        }

        private void setFadeState(boolean fadeAway) {
            this.fadeAway = fadeAway;
            this.tick = Mth.clamp(this.tick, 0, this.fadeTime);
            this.adjustingVolume = true;
        }

        public boolean done() {
            return this.tick > this.fadeTime || this.tick < 0;
        }

        @Override
        public void tick() {
            if (!this.adjustingVolume) {
                if (this.instances.isEmpty()) {
                    this.stop();
                }
                return;
            }
            boolean done = this.done();
            if (this.fadeAway)
                --this.tick;
            else
                ++this.tick;
            this.volume = this.defaultVol * Mth.clamp(1 - this.volDecrease * this.tick, 0, 1);
            if (done) {
                this.adjustingVolume = false;
            }
        }
    }

    public record ClientBossBarType(BossbarTexture texture, BossbarTexture overlay) {

        public int renderFrom(GuiGraphics graphics, int x, int y, BossEvent bossEvent, boolean withName) {
            Minecraft mc = Minecraft.getInstance();
            if (withName) {
                int screenX = mc.getWindow().getGuiScaledWidth();
                Component component = bossEvent.getName();
                int len = mc.font.width(component);
                int txtX = screenX / 2 - len / 2;
                graphics.drawString(mc.font, component, txtX, y - 9, 0xFFFFFF);
            }

            graphics.blit(this.texture.texture, x, y, 0, this.texture.offsetX, this.texture.offsetY, this.texture.width, this.texture.height, 256, 256);

            int overlayWidth = (int) (bossEvent.getProgress() * this.overlay.width);
            graphics.blit(this.overlay.texture, x, y, 0, this.overlay.offsetX, this.overlay.offsetY, overlayWidth, this.overlay.height, 256, 256);
            return y + mc.font.lineHeight + Math.max(this.texture.height, this.overlay.height) + 5;
        }
    }

    public record BossbarTexture(ResourceLocation texture, int width, int height, int offsetX, int offsetY) {

    }
}
