package io.github.flemmli97.runecraftory.forge.config;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfigSpec {

    public static final Pair<ClientConfigSpec, ForgeConfigSpec> SPEC = new ForgeConfigSpec.Builder().configure(ClientConfigSpec::new);

    public final ForgeConfigSpec.IntValue healthBarWidgetX;
    public final ForgeConfigSpec.IntValue healthBarWidgetY;
    public final ForgeConfigSpec.EnumValue<ClientConfig.DisplayPosition> healthBarWidgetPosition;
    public final ForgeConfigSpec.IntValue seasonDisplayX;
    public final ForgeConfigSpec.IntValue seasonDisplayY;
    public final ForgeConfigSpec.EnumValue<ClientConfig.DisplayPosition> seasonDisplayPosition;
    public final ForgeConfigSpec.IntValue inventoryOffsetX;
    public final ForgeConfigSpec.IntValue inventoryOffsetY;
    public final ForgeConfigSpec.IntValue creativeInventoryOffsetX;
    public final ForgeConfigSpec.IntValue creativeInventoryOffsetY;
    public final ForgeConfigSpec.IntValue farmlandX;
    public final ForgeConfigSpec.IntValue farmlandY;
    public final ForgeConfigSpec.EnumValue<ClientConfig.DisplayPosition> farmlandPosition;
    public final ForgeConfigSpec.BooleanValue renderCalendar;
    public final ForgeConfigSpec.EnumValue<ClientConfig.HealthRPRenderType> renderHealthRPBar;
    public final ForgeConfigSpec.BooleanValue inventoryButton;
    public final ForgeConfigSpec.BooleanValue grassColor;
    public final ForgeConfigSpec.BooleanValue foliageColor;
    public final ForgeConfigSpec.BooleanValue bossMusic;
    public final ForgeConfigSpec.IntValue bossMusicFadeDelay;

    private ClientConfigSpec(ForgeConfigSpec.Builder builder) {
        this.healthBarWidgetX = builder.comment("X Position of health bar").defineInRange("Health X", ClientConfig.healthBarWidgetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.comment("Y Position of health bar").defineInRange("Health Y", ClientConfig.healthBarWidgetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetPosition = builder.comment("Relative Position of the health bar in regards to the screen").defineEnum("Health Anchor", ClientConfig.healthBarWidgetPosition);
        this.seasonDisplayX = builder.comment("X Position of calendar display").defineInRange("Calendar X", ClientConfig.seasonDisplayX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.comment("Y Position of calendar display").defineInRange("Calendar Y", ClientConfig.seasonDisplayY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayPosition = builder.comment("Relative Position of the calendar in regards to the screen").defineEnum("Calendar Anchor", ClientConfig.seasonDisplayPosition);
        this.inventoryOffsetX = builder.comment("X offset of inventory button").defineInRange("Inventory X", ClientConfig.inventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.inventoryOffsetY = builder.comment("Y offset of inventory button").defineInRange("Inventory Y", ClientConfig.inventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetX = builder.comment("X offset of inventory button in creative").defineInRange("Creative X", ClientConfig.creativeInventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetY = builder.comment("Y offset of inventory button in creative").defineInRange("Creative Y", ClientConfig.creativeInventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandX = builder.comment("X Position of farmland info with the magnifying glass").defineInRange("Farminfo X", ClientConfig.farmlandX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandY = builder.comment("Y Position of farmland info with the magnifying glass").defineInRange("Farminfo Y", ClientConfig.farmlandY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandPosition = builder.comment("Relative Position of the farmland info in regards to the screen").defineEnum("Farminfo Anchor", ClientConfig.farmlandPosition);
        this.renderHealthRPBar = builder.comment("Render Health and RP bars").defineEnum("Health-RP Render", ClientConfig.renderHealthRpBar);
        this.renderCalendar = builder.define("Render Calendar", ClientConfig.renderCalendar);
        this.inventoryButton = builder.comment("Adds the button for the extended inventory to the vanilla inventory").define("Add Button", ClientConfig.inventoryButton);
        this.grassColor = builder.comment("Adjust grass color to current season").define("Season Grass", ClientConfig.grassColor);
        this.foliageColor = builder.comment("Adjust foliage color to current season").define("Season Foliage", ClientConfig.foliageColor);
        this.bossMusic = builder.comment("Bosses play a battle music during fight").define("Boss Music", ClientConfig.bossMusic);
        this.bossMusicFadeDelay = builder.comment("Time in ticks for boss music to fade away if the player runs away from the boss").defineInRange("Boss Music Fade", ClientConfig.bossMusicFadeDelay, 0, Integer.MAX_VALUE);
    }
}
