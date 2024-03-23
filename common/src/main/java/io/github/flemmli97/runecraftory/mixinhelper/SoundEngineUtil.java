package io.github.flemmli97.runecraftory.mixinhelper;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;

public interface SoundEngineUtil {

    ChannelAccess.ChannelHandle getHandle(SoundInstance inst);
}
