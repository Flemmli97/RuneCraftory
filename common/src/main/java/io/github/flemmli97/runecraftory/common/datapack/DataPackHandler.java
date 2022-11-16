package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameAndGiftManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class DataPackHandler {

    private static final ItemStatManager ITEM_STATS = new ItemStatManager();
    private static final CropManager CROPS = new CropManager();
    private static final FoodManager FOODS = new FoodManager();
    private static final ShopItemsManager SHOP_ITEMS = new ShopItemsManager();
    private static final NameAndGiftManager NAME_AND_GIFTS = new NameAndGiftManager();
    private static final NPCDataManager NPC_DATA = new NPCDataManager();
    private static final NPCLookManager NPC_LOOKS = new NPCLookManager();

    public static void reloadItemStats(Consumer<PreparableReloadListener> cons) {
        cons.accept(ITEM_STATS);
    }

    public static void reloadCropManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(CROPS);
    }

    public static void reloadFoodManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(FOODS);
    }

    public static void reloadShopItems(Consumer<PreparableReloadListener> cons) {
        cons.accept(SHOP_ITEMS);
    }

    public static void reloadNPCData(Consumer<PreparableReloadListener> cons) {
        cons.accept(NAME_AND_GIFTS);
        cons.accept(NPC_DATA);
        cons.accept(NPC_LOOKS);
    }

    public static ItemStatManager itemStatManager() {
        return ITEM_STATS;
    }

    public static CropManager cropManager() {
        return CROPS;
    }

    public static FoodManager foodManager() {
        return FOODS;
    }

    public static ShopItemsManager shopItemsManager() {
        return SHOP_ITEMS;
    }

    public static NameAndGiftManager nameAndGiftManager() {
        return NAME_AND_GIFTS;
    }

    public static NPCDataManager npcDataManager() {
        return NPC_DATA;
    }

    public static NPCLookManager npcLookManager() {
        return NPC_LOOKS;
    }

    public static void toPacket(FriendlyByteBuf buffer) {
        ITEM_STATS.toPacket(buffer);
        CROPS.toPacket(buffer);
        FOODS.toPacket(buffer);
    }

    public static void fromPacket(FriendlyByteBuf buffer) {
        ITEM_STATS.fromPacket(buffer);
        CROPS.fromPacket(buffer);
        FOODS.fromPacket(buffer);
    }
}
