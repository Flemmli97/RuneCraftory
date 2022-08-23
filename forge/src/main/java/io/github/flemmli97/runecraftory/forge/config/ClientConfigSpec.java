package io.github.flemmli97.runecraftory.forge.config;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
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
    public final ForgeConfigSpec.BooleanValue renderCalendar;
    public final ForgeConfigSpec.EnumValue<ClientConfig.HealthRPRenderType> renderHealthRPBar;
    public final ForgeConfigSpec.BooleanValue inventoryButton;
    public final ForgeConfigSpec.BooleanValue grassColor;
    public final ForgeConfigSpec.BooleanValue foliageColor;

    private ClientConfigSpec(ForgeConfigSpec.Builder builder) {
        this.healthBarWidgetX = builder.defineInRange("X Position of health bar", ClientConfig.healthBarWidgetX, 0, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.defineInRange("Y Position of health bar", ClientConfig.healthBarWidgetY, 0, Integer.MAX_VALUE);
        this.seasonDisplayX = builder.defineInRange("X Position of calendar display", ClientConfig.seasonDisplayX, 0, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.defineInRange("Y Position of calendar display", ClientConfig.seasonDisplayY, 0, Integer.MAX_VALUE);
        this.inventoryOffsetX = builder.defineInRange("X offset of inventory button", ClientConfig.inventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.inventoryOffsetY = builder.defineInRange("Y offset of inventory button", ClientConfig.inventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetX = builder.defineInRange("X offset of inventory button in creative", ClientConfig.creativeInventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetY = builder.defineInRange("Y offset of inventory button in creative", ClientConfig.creativeInventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.renderHealthRPBar = builder.defineEnum("Render Health and RP bars", ClientConfig.renderHealthRPBar);
        this.renderCalendar = builder.define("Render Calendar", ClientConfig.renderCalendar);
        this.inventoryButton = builder.define("Add Button", ClientConfig.inventoryButton);
        this.grassColor = builder.define("Adjust grass color to current season", ClientConfig.grassColor);
        this.foliageColor = builder.define("Adjust foliage color to current season", ClientConfig.foliageColor);
    }
}
