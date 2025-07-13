package io.github.flemmli97.runecraftory.common.blocks;

import net.minecraft.server.level.ServerLevel;

public interface DailyUpdateable {

    void update(ServerLevel level);

    boolean inValid();
}
