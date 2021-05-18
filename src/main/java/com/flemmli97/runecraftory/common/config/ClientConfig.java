package com.flemmli97.runecraftory.common.config;

public class ClientConfig {

    public static int healthBarWidgetX;
    public static int healthBarWidgetY;

    public static int seasonDisplayX;
    public static int seasonDisplayY;

    public static int inventoryOffsetX;
    public static int inventoryOffsetY;

    public static int creativeInventoryOffsetX;
    public static int creativeInventoryOffsetY;

    public static void load() {
        healthBarWidgetX = ClientConfigSpec.clientConfig.healthBarWidgetX.get();
        healthBarWidgetY = ClientConfigSpec.clientConfig.healthBarWidgetY.get();
        seasonDisplayX = ClientConfigSpec.clientConfig.seasonDisplayX.get();
        seasonDisplayY = ClientConfigSpec.clientConfig.seasonDisplayY.get();
        inventoryOffsetX = ClientConfigSpec.clientConfig.inventoryOffsetX.get();
        inventoryOffsetY = ClientConfigSpec.clientConfig.inventoryOffsetY.get();
        creativeInventoryOffsetX = ClientConfigSpec.clientConfig.creativeInventoryOffsetX.get();
        creativeInventoryOffsetY = ClientConfigSpec.clientConfig.creativeInventoryOffsetY.get();
    }

}
