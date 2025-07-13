package io.github.flemmli97.runecraftory.common.config.specs;

import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfigSpec {

    public static final Pair<ClientConfigSpec, ModConfigSpec> SPEC = new ModConfigSpec.Builder().configure(ClientConfigSpec::new);

    public final ModConfigSpec.IntValue healthBarWidgetX;
    public final ModConfigSpec.IntValue healthBarWidgetY;
    public final ModConfigSpec.EnumValue<ClientConfig.DisplayPosition> healthBarWidgetPosition;
    public final ModConfigSpec.IntValue seasonDisplayX;
    public final ModConfigSpec.IntValue seasonDisplayY;
    public final ModConfigSpec.EnumValue<ClientConfig.DisplayPosition> seasonDisplayPosition;
    public final ModConfigSpec.IntValue spellsDisplayX;
    public final ModConfigSpec.IntValue spellsDisplayY;
    public final ModConfigSpec.EnumValue<ClientConfig.DisplayPosition> spellsDisplayPosition;
    public final ModConfigSpec.IntValue inventoryOffsetX;
    public final ModConfigSpec.IntValue inventoryOffsetY;
    public final ModConfigSpec.IntValue creativeInventoryOffsetX;
    public final ModConfigSpec.IntValue creativeInventoryOffsetY;
    public final ModConfigSpec.IntValue farmlandX;
    public final ModConfigSpec.IntValue farmlandY;
    public final ModConfigSpec.EnumValue<ClientConfig.DisplayPosition> farmlandPosition;
    public final ModConfigSpec.BooleanValue renderCalendar;
    public final ModConfigSpec.EnumValue<ClientConfig.HealthRPRenderType> renderHealthRPBar;
    public final ModConfigSpec.BooleanValue inventoryButton;
    public final ModConfigSpec.BooleanValue grassColor;
    public final ModConfigSpec.BooleanValue foliageColor;
    public final ModConfigSpec.BooleanValue bossMusic;
    public final ModConfigSpec.IntValue bossMusicFadeDelay;

    private ClientConfigSpec(ModConfigSpec.Builder builder) {
        this.healthBarWidgetX = builder.comment("X Position of health bar").defineInRange("Health X", ClientConfig.healthBarWidgetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.comment("Y Position of health bar").defineInRange("Health Y", ClientConfig.healthBarWidgetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetPosition = builder.comment("Relative Position of the health bar in regards to the screen").defineEnum("Health Anchor", ClientConfig.healthBarWidgetPosition);
        this.seasonDisplayX = builder.comment("X Position of calendar display").defineInRange("Calendar X", ClientConfig.seasonDisplayX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.comment("Y Position of calendar display").defineInRange("Calendar Y", ClientConfig.seasonDisplayY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayPosition = builder.comment("Relative Position of the calendar in regards to the screen").defineEnum("Calendar Anchor", ClientConfig.seasonDisplayPosition);
        this.spellsDisplayX = builder.comment("X Position of the spell inventory display").defineInRange("Spell X", ClientConfig.spellsDisplayX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.spellsDisplayY = builder.comment("Y Position of the spell inventory display").defineInRange("Spell Y", ClientConfig.spellsDisplayY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.spellsDisplayPosition = builder.comment("Relative Position of the the spell inventory display in regards to the screen").defineEnum("Spell Anchor", ClientConfig.spellsDisplayPosition);
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
