package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameAndGiftManager;
import io.github.flemmli97.runecraftory.platform.Platform;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class DataPackHandler {

    private static final DataPackHandler SERVER_PACK = new DataPackHandler();

    private final ItemStatManager itemStats = new ItemStatManager();
    private final CropManager crops = new CropManager();
    private final FoodManager foods = new FoodManager();
    private final ShopItemsManager shopItems = new ShopItemsManager();
    private final NameAndGiftManager nameAndGifts = new NameAndGiftManager();
    private final NPCDataManager npcData = new NPCDataManager();
    private final NPCLookManager npcLooks = new NPCLookManager();

    public static void reloadItemStats(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.itemStats);
    }

    public static void reloadCropManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.crops);
    }

    public static void reloadFoodManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.foods);
    }

    public static void reloadShopItems(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.shopItems);
    }

    public static void reloadNPCData(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.nameAndGifts);
        cons.accept(SERVER_PACK.npcData);
        cons.accept(SERVER_PACK.npcLooks);
    }

    public static ItemStatManager itemStatManager() {
        return SERVER_PACK.itemStats;
    }

    public static CropManager cropManager() {
        return SERVER_PACK.crops;
    }

    public static FoodManager foodManager() {
        return SERVER_PACK.foods;
    }

    public static ShopItemsManager shopItemsManager() {
        return SERVER_PACK.shopItems;
    }

    public static NameAndGiftManager nameAndGiftManager() {
        return SERVER_PACK.nameAndGifts;
    }

    public static NPCDataManager npcDataManager() {
        return SERVER_PACK.npcData;
    }

    public static NPCLookManager npcLookManager() {
        return SERVER_PACK.npcLooks;
    }

    public static void toPacket(FriendlyByteBuf buffer) {
        SERVER_PACK.itemStats.toPacket(buffer);
        SERVER_PACK.crops.toPacket(buffer);
        SERVER_PACK.foods.toPacket(buffer);
    }

    public static void fromPacket(FriendlyByteBuf buffer) {
        ClientHandlers.CLIENT_PACK.itemStats.fromPacket(buffer);
        ClientHandlers.CLIENT_PACK.crops.fromPacket(buffer);
        ClientHandlers.CLIENT_PACK.foods.fromPacket(buffer);
    }
}
