package com.flemmli97.runecraftory.common.datapack;

import com.flemmli97.runecraftory.api.datapack.CropProperties;
import com.flemmli97.runecraftory.api.datapack.FoodProperties;
import com.flemmli97.runecraftory.api.datapack.ItemStat;
import com.flemmli97.runecraftory.common.datapack.manager.CropManager;
import com.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import com.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import net.minecraft.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;

public class DataPackHandler {

    private static ItemStatManager itemStats;
    private static CropManager crops;
    private static FoodManager foods;

    public static void registerDataPackHandler(AddReloadListenerEvent event) {
        event.addListener(itemStats = new ItemStatManager());
        event.addListener(crops = new CropManager());
        event.addListener(foods = new FoodManager());
    }

    public static ItemStat getStats(Item item) {
        return itemStats.get(item);
    }

    public static CropProperties getCropStat(Item item) {
        return crops.get(item);
    }

    public static FoodProperties getFoodStat(Item item) {
        return foods.get(item);
    }
}
