package io.github.flemmli97.runecraftory.common.config;

public class ClientConfig {

    public static int healthBarWidgetX;
    public static int healthBarWidgetY;

    public static int seasonDisplayX;
    public static int seasonDisplayY;

    public static int inventoryOffsetX;
    public static int inventoryOffsetY;

    public static int creativeInventoryOffsetX;
    public static int creativeInventoryOffsetY;

    public static int tempX, tempY;

    public static void load(ClientConfigSpec spec) {
        healthBarWidgetX = spec.healthBarWidgetX.get();
        healthBarWidgetY = spec.healthBarWidgetY.get();
        seasonDisplayX = spec.seasonDisplayX.get();
        seasonDisplayY = spec.seasonDisplayY.get();
        inventoryOffsetX = spec.inventoryOffsetX.get();
        inventoryOffsetY = spec.inventoryOffsetY.get();
        creativeInventoryOffsetX = spec.creativeInventoryOffsetX.get();
        creativeInventoryOffsetY = spec.creativeInventoryOffsetY.get();

        tempX = spec.tempX.get();
        tempY = spec.tempY.get();
    }

}
