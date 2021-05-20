package com.flemmli97.runecraftory.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfigSpec {

    public static final ForgeConfigSpec clientSpec;
    public static final ClientConfigSpec clientConfig;

    public final ForgeConfigSpec.ConfigValue<Integer> healthBarWidgetX;
    public final ForgeConfigSpec.ConfigValue<Integer> healthBarWidgetY;
    public final ForgeConfigSpec.ConfigValue<Integer> seasonDisplayX;
    public final ForgeConfigSpec.ConfigValue<Integer> seasonDisplayY;
    public final ForgeConfigSpec.ConfigValue<Integer> inventoryOffsetX;
    public final ForgeConfigSpec.ConfigValue<Integer> inventoryOffsetY;
    public final ForgeConfigSpec.ConfigValue<Integer> creativeInventoryOffsetX;
    public final ForgeConfigSpec.ConfigValue<Integer> creativeInventoryOffsetY;

    public final ForgeConfigSpec.ConfigValue<Integer> tempX;
    public final ForgeConfigSpec.ConfigValue<Integer> tempY;

    private ClientConfigSpec(ForgeConfigSpec.Builder builder) {
        this.healthBarWidgetX = builder.define("X Position of health bar", 2);
        this.healthBarWidgetY = builder.define("Y Position of health bar", 2);
        this.seasonDisplayX = builder.define("X Position of calendar display", 3);
        this.seasonDisplayY = builder.define("Y Position of calendar display", 35);
        this.inventoryOffsetX = builder.define("X offset of inventory button", 79);
        this.inventoryOffsetY = builder.define("Y offset of inventory button", 47);
        this.creativeInventoryOffsetX = builder.define("X offset of inventory button in creative", 20);
        this.creativeInventoryOffsetY = builder.define("Y offset of inventory button in creative", 22);

        this.tempX = builder.define("Temp X", 20);
        this.tempY = builder.define("Temp Y", 22);

    }


    static {
        Pair<ClientConfigSpec, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(ClientConfigSpec::new);
        clientSpec = specPair2.getRight();
        clientConfig = specPair2.getLeft();
    }
}
