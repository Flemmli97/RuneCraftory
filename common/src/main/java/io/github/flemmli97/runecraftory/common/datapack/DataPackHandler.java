package io.github.flemmli97.runecraftory.common.datapack;

import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.GateSpawnsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.MonsterPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.SkillPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.SpellPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.WeaponPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.GiftManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCActionManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameManager;
import io.github.flemmli97.runecraftory.common.network.S2CDataPackSync;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Consumer;

public class DataPackHandler {

    public static final DataPackHandler INSTANCE = new DataPackHandler();

    private final ItemStatManager itemStats = new ItemStatManager();
    private final CropManager crops = new CropManager();
    private final FoodManager foods = new FoodManager();
    private final ShopItemsManager shopItems = new ShopItemsManager();
    private final GateSpawnsManager gateSpawnsManager = new GateSpawnsManager();
    private final MonsterPropertiesManager mobProperties = new MonsterPropertiesManager();
    private final SpellPropertiesManager spellProperties = new SpellPropertiesManager();
    private final SkillPropertiesManager skillPropertiesManager = new SkillPropertiesManager();
    private final WeaponPropertiesManager weaponPropertiesManager = new WeaponPropertiesManager();
    private final NameManager names = new NameManager();
    private final GiftManager gifts = new GiftManager();
    private final NPCDataManager npcData = new NPCDataManager();
    private final NPCLookManager npcLooks = new NPCLookManager();
    private final NPCActionManager npcActions = new NPCActionManager();
    private final NPCConversationManager npcConversations = new NPCConversationManager();

    public static void reloadItemStats(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.itemStats);
    }

    public static void reloadCropManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.crops);
    }

    public static void reloadFoodManager(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.foods);
    }

    public static void reloadShopItems(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.shopItems);
    }

    public static void reloadGateSpawns(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.gateSpawnsManager);
    }

    public static void reloadProperties(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.mobProperties);
        cons.accept(INSTANCE.spellProperties);
        cons.accept(INSTANCE.skillPropertiesManager);
        cons.accept(INSTANCE.weaponPropertiesManager);
    }

    public static void reloadNPCData(Consumer<PreparableReloadListener> cons) {
        cons.accept(INSTANCE.names);
        cons.accept(INSTANCE.gifts);
        cons.accept(INSTANCE.npcData);
        cons.accept(INSTANCE.npcLooks);
        cons.accept(INSTANCE.npcConversations);
        cons.accept(INSTANCE.npcActions);
    }

    public static void toPacket(FriendlyByteBuf buffer, S2CDataPackSync.SyncedType type) {
        // See S2CDataPackSync
        /*switch (type) {
            case ITEMSTATS -> INSTANCE.itemStats.toPacket(buffer);
            case CROPS -> INSTANCE.crops.toPacket(buffer);
            case FOOD -> INSTANCE.foods.toPacket(buffer);
            case SKILL -> INSTANCE.skillPropertiesManager.toPacket(buffer);
        }*/
        INSTANCE.itemStats.toPacket(buffer);
        INSTANCE.crops.toPacket(buffer);
        INSTANCE.foods.toPacket(buffer);
        INSTANCE.skillPropertiesManager.toPacket(buffer);
    }

    public static void fromPacket(S2CDataPackSync.SyncedType type, FriendlyByteBuf buffer) {
        // See S2CDataPackSync
        /*switch (type) {
            case ITEMSTATS -> INSTANCE.itemStats.fromPacket(buffer);
            case CROPS -> INSTANCE.crops.fromPacket(buffer);
            case FOOD -> INSTANCE.foods.fromPacket(buffer);
            case SKILL -> INSTANCE.skillPropertiesManager.fromPacket(buffer);
        }*/
        INSTANCE.itemStats.fromPacket(buffer);
        INSTANCE.crops.fromPacket(buffer);
        INSTANCE.foods.fromPacket(buffer);
        INSTANCE.skillPropertiesManager.fromPacket(buffer);
    }

    private DataPackHandler() {
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

    public SkillPropertiesManager skillPropertiesManager() {
        return this.skillPropertiesManager;
    }

    public WeaponPropertiesManager weaponPropertiesManager() {
        return this.weaponPropertiesManager;
    }

    public NameManager nameManager() {
        return this.names;
    }

    public GiftManager giftManager() {
        return this.gifts;
    }

    public NPCDataManager npcDataManager() {
        return this.npcData;
    }

    public NPCLookManager npcLookManager() {
        return this.npcLooks;
    }

    public NPCActionManager npcActionsManager() {
        return this.npcActions;
    }

    public NPCConversationManager npcConversationManager() {
        return this.npcConversations;
    }
}
