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
    public final CommentedJsonConfig.IntVal seasonDisplayX;
    public final CommentedJsonConfig.IntVal seasonDisplayY;
    public final CommentedJsonConfig.IntVal inventoryOffsetX;
    public final CommentedJsonConfig.IntVal inventoryOffsetY;
    public final CommentedJsonConfig.IntVal creativeInventoryOffsetX;
    public final CommentedJsonConfig.IntVal creativeInventoryOffsetY;
    public final CommentedJsonConfig.CommentedVal<ClientConfig.HealthRPRenderType> renderHealthRPBar;
    public final CommentedJsonConfig.CommentedVal<Boolean> renderCalendar;
    public final CommentedJsonConfig.CommentedVal<Boolean> inventoryButton;
    public final CommentedJsonConfig.CommentedVal<Boolean> grassColor;
    public final CommentedJsonConfig.CommentedVal<Boolean> foliageColor;

    private ClientConfigSpec(CommentedJsonConfig.Builder builder) {
        this.healthBarWidgetX = builder.defineInRange("X Position of health bar", ClientConfig.healthBarWidgetX, 0, Integer.MAX_VALUE);
        this.healthBarWidgetY = builder.defineInRange("Y Position of health bar", ClientConfig.healthBarWidgetY, 0, Integer.MAX_VALUE);
        this.seasonDisplayX = builder.defineInRange("X Position of calendar display", ClientConfig.seasonDisplayX, 0, Integer.MAX_VALUE);
        this.seasonDisplayY = builder.defineInRange("Y Position of calendar display", ClientConfig.seasonDisplayY, 0, Integer.MAX_VALUE);
        this.inventoryOffsetX = builder.defineInRange("X offset of inventory button", ClientConfig.inventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.inventoryOffsetY = builder.defineInRange("Y offset of inventory button", ClientConfig.inventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetX = builder.defineInRange("X offset of inventory button in creative", ClientConfig.creativeInventoryOffsetX, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.creativeInventoryOffsetY = builder.defineInRange("Y offset of inventory button in creative", ClientConfig.creativeInventoryOffsetY, Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.renderHealthRPBar = builder.define("Render Health and RP bars", ClientConfig.renderHealthRPBar);
        this.renderCalendar = builder.define("Render Calendar", ClientConfig.renderCalendar);
        this.inventoryButton = builder.define("Add Button", ClientConfig.inventoryButton);
        this.grassColor = builder.define("Adjust grass color to current season", ClientConfig.grassColor);
        this.foliageColor = builder.define("Adjust foliage color to current season", ClientConfig.foliageColor);
        builder.registerReloadHandler(() -> ConfigHolder.loadClient(this));
    }
}
