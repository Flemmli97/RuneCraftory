package io.github.flemmli97.runecraftory.mixinhelper;

import io.github.flemmli97.runecraftory.common.attachment.ToggleStateHandler;

public interface MobToggleHandler {

    void runecraftory$setIgnoreNoAI();

    ToggleStateHandler runecraftory$NoAIState();
}
