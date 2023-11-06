package io.github.flemmli97.runecraftory.api;

import net.minecraft.server.level.ServerLevel;

public interface IDailyUpdate {

    void update(ServerLevel level);

    boolean inValid();
}
