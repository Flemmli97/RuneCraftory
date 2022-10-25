package io.github.flemmli97.runecraftory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public class ShakeHandler {

    public static int shakeTick;
    public static float shakeStrength = 2;

    public static void shakeScreen(Vec3 pos, double maxDist, int time, float strength) {
        Vec3 player = Minecraft.getInstance().player.position();
        if (pos.distanceToSqr(player) > maxDist * maxDist)
            return;
        shakeScreen(time, shakeStrength);
    }

    public static void shakeScreen(int time, float strength) {
        if (shakeTick < time) {
            shakeTick = time;
            shakeStrength = strength;
        }
    }
}
