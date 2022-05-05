package io.github.flemmli97.runecraftory.fabric.config;

import io.github.flemmli97.runecraftory.RuneCraftory;
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
    public final CommentedJsonConfig.CommentedVal<Boolean> renderOverlay;
    public final CommentedJsonConfig.CommentedVal<Boolean> inventoryButton;

    private ClientConfigSpec(CommentedJsonConfig.Builder builder) {
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
        builder.registerReloadHandler(() -> ConfigHolder.loadClient(this));
    }
}
