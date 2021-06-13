package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.api.datapack.CropProperties;
import io.github.flemmli97.runecraftory.api.datapack.FoodProperties;
import io.github.flemmli97.runecraftory.api.datapack.ItemStat;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.event.AddReloadListenerEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class DataPackHandler {

    private static ItemStatManager itemStats;
    private static CropManager crops;
    private static FoodManager foods;

    public static void registerDataPackHandler(AddReloadListenerEvent event) {
        event.addListener(itemStats = new ItemStatManager());
        event.addListener(crops = new CropManager());
        event.addListener(foods = new FoodManager());
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

    public static void toPacket(PacketBuffer buffer) {
        itemStats.toPacket(buffer);
        crops.toPacket(buffer);
        foods.toPacket(buffer);
    }

    public static void fromPacket(PacketBuffer buffer) {
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
