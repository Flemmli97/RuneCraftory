package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.client.ClientHandlers;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.GateSpawnsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.MonsterPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.SpellPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameAndGiftManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class DataPackHandler {

    public static final DataPackHandler SERVER_PACK = new DataPackHandler();

    private final ItemStatManager itemStats = new ItemStatManager();
    private final CropManager crops = new CropManager();
    private final FoodManager foods = new FoodManager();
    private final ShopItemsManager shopItems = new ShopItemsManager();
    private final GateSpawnsManager gateSpawnsManager = new GateSpawnsManager();
    private final MonsterPropertiesManager mobProperties = new MonsterPropertiesManager();
    private final SpellPropertiesManager spellProperties = new SpellPropertiesManager();
    private final NameAndGiftManager nameAndGifts = new NameAndGiftManager();
    private final NPCDataManager npcData = new NPCDataManager();
    private final NPCLookManager npcLooks = new NPCLookManager();
    private final NPCConversationManager npcConversations = new NPCConversationManager();

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

    public static void reloadGateSpawns(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.gateSpawnsManager);
    }

    public static void reloadMobProperties(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.mobProperties);
    }

    public static void reloadSpellProperties(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.spellProperties);
    }

    public static void reloadNPCData(Consumer<PreparableReloadListener> cons) {
        cons.accept(SERVER_PACK.nameAndGifts);
        cons.accept(SERVER_PACK.npcData);
        cons.accept(SERVER_PACK.npcLooks);
        cons.accept(SERVER_PACK.npcConversations);
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

    public ItemStatManager itemStatManager() {
        return this.itemStats;
    }

    public CropManager cropManager() {
        return this.crops;
    }

    public FoodManager foodManager() {
        return this.foods;
    }

    public ShopItemsManager shopItemsManager() {
        return this.shopItems;
    }

    public GateSpawnsManager gateSpawnsManager() {
        return this.gateSpawnsManager;
    }

    public MonsterPropertiesManager monsterPropertiesManager() {
        return this.mobProperties;
    }

    public SpellPropertiesManager spellPropertiesManager() {
        return this.spellProperties;
    }

    public NameAndGiftManager nameAndGiftManager() {
        return this.nameAndGifts;
    }

    public NPCDataManager npcDataManager() {
        return this.npcData;
    }

    public NPCLookManager npcLookManager() {
        return this.npcLooks;
    }

    public NPCConversationManager npcConversationManager() {
        return this.npcConversations;
    }
}
