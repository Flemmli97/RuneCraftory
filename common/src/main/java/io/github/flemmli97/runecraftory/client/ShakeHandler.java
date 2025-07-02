package io.github.flemmli97.runecraftory.client;

public class ShakeHandler {

    public static int shakeTick;
    public static float shakeStrength = 2;

    public static void shakeScreen(int time, float strength) {
        if (shakeTick < time) {
            shakeTick = time;
            shakeStrength = strength;
        }
    }
}
