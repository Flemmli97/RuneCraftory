package io.github.flemmli97.runecraftory.common.datapack;

import com.google.gson.Gson;
import io.github.flemmli97.runecraftory.common.datapack.manager.CropManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.FoodManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.GateSpawnsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ItemStatManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.MonsterPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.ShopItemsManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.SkillPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.SpellPropertiesManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.StructureBossManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.GiftManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCActionManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCConversationManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCDataManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NPCLookManager;
import io.github.flemmli97.runecraftory.common.datapack.manager.npc.NameManager;
import io.github.flemmli97.tenshilib.common.data.SyncedReloadListeners;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class DataPackHandler {

    public static final Gson GSON = new Gson();
    private static final Set<ListenerExtension> LISTENERS = new HashSet<>();
    private static final Map<ResourceLocation, SyncableListener<?>> SYNCABLES = new HashMap<>();

    public static final DataPackHandler INSTANCE = new DataPackHandler();

    private final ItemStatManager itemStats = syncable(new ItemStatManager());
    private final CropManager crops = syncable(new CropManager());
    private final FoodManager foods = syncable(new FoodManager());
    private final ShopItemsManager shopItems = add(new ShopItemsManager());
    private final GateSpawnsManager gateSpawnsManager = add(new GateSpawnsManager());
    private final StructureBossManager structureBossManager = add(new StructureBossManager());
    private final MonsterPropertiesManager mobProperties = add(new MonsterPropertiesManager());
    private final SpellPropertiesManager spellProperties = add(new SpellPropertiesManager());
    private final SkillPropertiesManager skillPropertiesManager = add(new SkillPropertiesManager());
    private final NameManager names = add(new NameManager());
    private final GiftManager gifts = add(new GiftManager());
    private final NPCDataManager npcData = add(new NPCDataManager());
    private final NPCLookManager npcLooks = add(new NPCLookManager());
    private final NPCActionManager npcActions = add(new NPCActionManager());
    private final NPCConversationManager npcConversations = add(new NPCConversationManager());

    public static void addListeners(Consumer<ListenerExtension> cons) {
        LISTENERS.forEach(cons);
    }

    private static <T extends ListenerExtension> T add(T listener) {
        LISTENERS.add(listener);
        return listener;
    }

    private static <T extends SyncableListener<?>> T syncable(T listener) {
        SYNCABLES.put(listener.id(), listener);
        SyncedReloadListeners.addOrUpdate(listener.id(), listener);
        return add(listener);
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

    public StructureBossManager structureBossManager() {
        return this.structureBossManager;
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

    @SuppressWarnings("unchecked")
    public <T> SyncableListener<T> getSyncable(ResourceLocation id) {
        return (SyncableListener<T>) SYNCABLES.get(id);
    }
}
