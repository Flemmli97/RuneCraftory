package io.github.flemmli97.runecraftory.common.config;

public class ClientConfig {

    public static int healthBarWidgetX = 2;
    public static int healthBarWidgetY = 2;

    public static int seasonDisplayX = 0;
    public static int seasonDisplayY = 35;

    public static int inventoryOffsetX = 79;
    public static int inventoryOffsetY = 47;

    public static int creativeInventoryOffsetX = 20;
    public static int creativeInventoryOffsetY = 22;

    public static HealthRPRenderType renderHealthRPBar = HealthRPRenderType.BOTH;
    public static boolean renderCalendar = true;
    public static boolean inventoryButton = true;

    public static boolean grassColor = true;
    public static boolean foliageColor = true;

    public enum HealthRPRenderType {
        NONE,
        BOTH,
        RPONLY
    }
}
