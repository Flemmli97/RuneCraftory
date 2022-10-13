package io.github.flemmli97.runecraftory.fabric.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.config.ClientConfig;
import io.github.flemmli97.tenshilib.common.config.CommentedJsonConfig;
import io.github.flemmli97.tenshilib.common.config.JsonConfig;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.lang3.tuple.Pair;

public class ClientConfigSpec {

    public static final Pair<JsonConfig<CommentedJsonConfig>, ClientConfigSpec> spec = CommentedJsonConfig.Builder
            .create(FabricLoader.getInstance().getConfigDir().resolve(RuneCraftory.MODID).resolve("client.json"), 1, ClientConfigSpec::new);

    public final CommentedJsonConfig.IntVal healthBarWidgetX;
    public final CommentedJsonConfig.IntVal healthBarWidgetY;
    public final CommentedJsonConfig.CommentedVal<ClientConfig.DisplayPosition> healthBarWidgetPosition;
    public final CommentedJsonConfig.IntVal seasonDisplayX;
    public final CommentedJsonConfig.IntVal seasonDisplayY;
    public final CommentedJsonConfig.CommentedVal<ClientConfig.DisplayPosition> seasonDisplayPosition;
    public final CommentedJsonConfig.IntVal inventoryOffsetX;
    public final CommentedJsonConfig.IntVal inventoryOffsetY;
    public final CommentedJsonConfig.IntVal creativeInventoryOffsetX;
    public final CommentedJsonConfig.IntVal creativeInventoryOffsetY;
    public final CommentedJsonConfig.IntVal farmlandX;
    public final CommentedJsonConfig.IntVal farmlandY;
    public final CommentedJsonConfig.CommentedVal<ClientConfig.DisplayPosition> farmlandPosition;
    public final CommentedJsonConfig.CommentedVal<ClientConfig.HealthRPRenderType> renderHealthRPBar;
    public final CommentedJsonConfig.CommentedVal<Boolean> renderCalendar;
    public final CommentedJsonConfig.CommentedVal<Boolean> inventoryButton;
    public final CommentedJsonConfig.CommentedVal<Boolean> grassColor;
    public final CommentedJsonConfig.CommentedVal<Boolean> foliageColor;

    private ClientConfigSpec(CommentedJsonConfig.Builder builder) {
        this.healthBarWidgetX = builder.comment("X Position of health bar").defineInRange("Health X", ClientConfig.healthBarWidgetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.comment("Y Position of health bar").defineInRange("Health Y", ClientConfig.healthBarWidgetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.healthBarWidgetPosition = builder.comment("Relative Position of the health bar in regards to the screen").define("Health Anchor", ClientConfig.healthBarWidgetPosition);
        this.seasonDisplayX = builder.comment("X Position of calendar display").defineInRange("Calendar X", ClientConfig.seasonDisplayX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.comment("Y Position of calendar display").defineInRange("Calendar Y", ClientConfig.seasonDisplayY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.seasonDisplayPosition = builder.comment("Relative Position of the calendar in regards to the screen").define("Calendar Anchor", ClientConfig.seasonDisplayPosition);
        this.inventoryOffsetX = builder.comment("X offset of inventory button").defineInRange("Inventory X", ClientConfig.inventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.inventoryOffsetY = builder.comment("Y offset of inventory button").defineInRange("Inventory Y", ClientConfig.inventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetX = builder.comment("X offset of inventory button in creative").defineInRange("Creative X", ClientConfig.creativeInventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetY = builder.comment("Y offset of inventory button in creative").defineInRange("Creative Y", ClientConfig.creativeInventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandX = builder.comment("X Position of farmland info with the magnifying glass").defineInRange("Farminfo X", ClientConfig.farmlandX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandY = builder.comment("Y Position of farmland info with the magnifying glass").defineInRange("Farminfo Y", ClientConfig.farmlandY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.farmlandPosition = builder.comment("Relative Position of the farmland info in regards to the screen").define("Farminfo Anchor", ClientConfig.farmlandPosition);
        this.renderHealthRPBar = builder.comment("Render Health and RP bars").define("Health-RP Render", ClientConfig.renderHealthRPBar);
        this.renderCalendar = builder.define("Render Calendar", ClientConfig.renderCalendar);
        this.inventoryButton = builder.comment("Adds the button for the extended inventory to the vanilla inventory").define("Add Button", ClientConfig.inventoryButton);
        this.grassColor = builder.comment("Adjust grass color to current season").define("Season Grass", ClientConfig.grassColor);
        this.foliageColor = builder.comment("Adjust foliage color to current season").define("Season Foliage", ClientConfig.foliageColor);
        builder.registerReloadHandler(() -> ConfigHolder.loadClient(this));
    }
}
