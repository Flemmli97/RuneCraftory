package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class DataPackHandler {

    private static ItemStatManager itemStats = new ItemStatManager();
    private static CropManager crops = new CropManager();
    private static FoodManager foods = new FoodManager();

    public static void reloadItemStats(Consumer<PreparableReloadListener> cons) {
        cons.accept(itemStats);
    }

    public static void reloadCropManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(crops);
    }

    public static void reloadFoodManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(foods);
    }

    @Nullable
    public static Optional<ItemStat> getStats(Item item) {
        return Optional.ofNullable(itemStats.get(item));
    }

    public static CropProperties getCropStat(Item item) {
        return crops.get(item);
    }

    @Nullable
    public static FoodProperties getFoodStat(Item item) {
        return foods.get(item);
    }

    public static void toPacket(FriendlyByteBuf buffer) {
        itemStats.toPacket(buffer);
        crops.toPacket(buffer);
        foods.toPacket(buffer);
    }

    public static void fromPacket(FriendlyByteBuf buffer) {
        if (itemStats == null)
            itemStats = new ItemStatManager();
        if (crops == null)
            crops = new CropManager();
        if (foods == null)
            foods = new FoodManager();
        itemStats.fromPacket(buffer);
        crops.fromPacket(buffer);
        foods.fromPacket(buffer);
    }
}
