package io.github.flemmli97.runecraftory.forge.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfigSpec {

    public static final Pair<ClientConfigSpec, ForgeConfigSpec> spec = new ForgeConfigSpec.Builder().configure(ClientConfigSpec::new);

    public final ForgeConfigSpec.IntValue healthBarWidgetX;
    public final ForgeConfigSpec.IntValue healthBarWidgetY;
    public final ForgeConfigSpec.IntValue seasonDisplayX;
    public final ForgeConfigSpec.IntValue seasonDisplayY;
    public final ForgeConfigSpec.IntValue inventoryOffsetX;
    public final ForgeConfigSpec.IntValue inventoryOffsetY;
    public final ForgeConfigSpec.IntValue creativeInventoryOffsetX;
    public final ForgeConfigSpec.IntValue creativeInventoryOffsetY;
    public final ForgeConfigSpec.BooleanValue renderOverlay;
    public final ForgeConfigSpec.BooleanValue inventoryButton;
    public final ForgeConfigSpec.BooleanValue grassColor;
    public final ForgeConfigSpec.BooleanValue foliageColor;

    private ClientConfigSpec(ForgeConfigSpec.Builder builder) {
        this.healthBarWidgetX = builder.defineInRange("X Position of health bar", 2, 0, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.defineInRange("Y Position of health bar", 2, 0, Integer.MAX_VALUE);
        this.seasonDisplayX = builder.defineInRange("X Position of calendar display", 0, 0, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.defineInRange("Y Position of calendar display", 35, 0, Integer.MAX_VALUE);
        this.inventoryOffsetX = builder.defineInRange("X offset of inventory button", 79, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.inventoryOffsetY = builder.defineInRange("Y offset of inventory button", 47, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetX = builder.defineInRange("X offset of inventory button in creative", 20, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetY = builder.defineInRange("Y offset of inventory button in creative", 22, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.renderOverlay = builder.define("Render Overlay", false);
        this.inventoryButton = builder.define("Add Button", true);
        this.grassColor = builder.define("Adjust grass color to current season", true);
        this.foliageColor = builder.define("Adjust foliage color to current season", true);
    }
}
