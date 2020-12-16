package com.flemmli97.runecraftory.api;

import net.minecraft.world.server.ServerWorld;

public interface IDailyUpdate {

    void update(ServerWorld world);
}
