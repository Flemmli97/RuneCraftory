package io.github.flemmli97.runecraftory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class ShakeHandler {

    public static int shakeTick;

    public static void shakeScreen(Vec3 pos, double maxDist, int time) {
        Vec3 player = Minecraft.getInstance().player.position();
        if (pos.distanceToSqr(player) > maxDist * maxDist)
            return;
        if (shakeTick < time)
            shakeTick = time;
    }
}
