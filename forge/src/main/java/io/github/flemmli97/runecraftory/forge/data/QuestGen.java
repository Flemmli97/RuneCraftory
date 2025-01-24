package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.api.datapack.provider.NPCDataProvider;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.quests.NPCQuest;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalk;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingEntry;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingEntry;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.simplequests_api.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests_api.impls.entries.single.BlockInteractEntry;
import io.github.flemmli97.simplequests_api.impls.quests.Quest;
import io.github.flemmli97.simplequests_api.quest.QuestCategory;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuestGen extends QuestProvider {

    private final QuestCategory main = new QuestCategory.Builder(QuestHandler.QUEST_CATEGORY, RuneCraftory.MODID + ".quests.category")
            .needContexts(QuestHandler.QUEST_CONTEXT)
            .build();
    private final QuestCategory hidden = new QuestCategory.Builder(id("hidden_quests"), RuneCraftory.MODID + ".quests.category.hidden")
            .needContexts(QuestHandler.QUEST_CONTEXT)
            .setHidden()
            .build();

    public static final ResourceLocation MINING = id("mining");
    public static final ResourceLocation TAMING = id("tame_monster");
    public static final ResourceLocation SHIP_TURNIP = id("ship_turnip");

    public final Map<String, String> translations = new LinkedHashMap<>();
    public final Map<ResourceLocation, LootTable.Builder> loot = new HashMap<>();
    public final Map<ResourceLocation, Map<ResourceLocation, NPCDataProvider.QuestResponseBuilder>> questResponses = new HashMap<>();

    public QuestGen(DataGenerator gen) {
        super(gen, false);
    }

    @Override
    protected void add() {
        this.addQuest(this.createNPCQuest(SHIP_TURNIP, "First Shipment!", "Come see me.",
                        b -> b.addNPC("shop_owner/1", new NPCDataProvider.QuestResponseBuilder(
                                new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.start", """
                                        Are you here for my request?
                                        I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.
                                        Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."""),
                                new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.active", "Please ship a turnip."),
                                new NPCData.ConversationSet.Builder("npc.shop_owner.quest.ship_turnip.end", "Great. There are a lot of items you can ship to make money. Here take" +
                                        "these turnip seeds. It should come in handy.")
                        )),
                        LootTable.lootTable().withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ModItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))),
                        b -> b.addEntry(new ShippingEntry(ItemPredicate.Builder.item().of(ModItems.TURNIP.get()).build(), 1)))
                .setRepeatDelay(-1)
                .withCategory(this.main)
                .withIcon(new ItemStack(ModItems.TURNIP_SEEDS.get())));

        this.addQuest(this.createNPCQuest(MINING, "Acquire Hardware??", "Come see me.",
                        b -> b.addNPC("smith/1", new NPCDataProvider.QuestResponseBuilder(
                                new NPCData.ConversationSet.Builder("npc.smith.quest.mining.start", """
                                        You saw my request? Great!
                                        You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.
                                        I want you to mine 10 of them for me."""),
                                new NPCData.ConversationSet.Builder("npc.smith.quest.mining.active", "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                        "I want you to mine 10 mineral blocks for me."),
                                new NPCData.ConversationSet.Builder("npc.smith.quest.mining.end", "Nice! Mining ores increases your mining level. " +
                                        "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier.")
                        )),
                        LootTable.lootTable().withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(ModItems.HAMMER_SCRAP.get())))
                                .withPool(LootPool.lootPool()
                                        .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                                        .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))),
                        builder -> builder.addEntry("Break 10 mineral blocks", desc -> new BlockInteractEntry(ItemPredicate.ANY, BlockPredicate.Builder.block().of(RunecraftoryTags.ORES).build(),
                                10, false, false, true, desc, "", "", EntityPredicate.ANY)))
                .setRepeatDelay(-1)
                .withCategory(this.main)
                .withIcon(new ItemStack(ModItems.HAMMER_SCRAP.get())));

        this.addQuest(this.createNPCQuest(TAMING, "Tame a monster", "I need you to tame a monster. Come see me.",
                        b -> b.addNPC("random_npc_1", new NPCDataProvider.QuestResponseBuilder(
                                new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.start", """
                                        Did you know that you can tame the monsters in this world?
                                        You would need to setup a barn first and then just give them an item.
                                        
                                        With that said I would like you to tame a monster."""),
                                new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.active", "You still need to tame a monster.\n" +
                                        "Some monsters prefer certain items more."),
                                new NPCData.ConversationSet.Builder("npc.generic.quest.tame_monster.end", "I see you've successfully tamed a monster. Congrats!")
                        )),
                        LootTable.lootTable().withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(ModItems.BRUSH.get()))),
                        builder -> builder.addEntry("Tame a monster", desc -> new TamingEntry(EntityPredicate.ANY, 1, desc)))
                .setRepeatDelay(-1)
                .withCategory(this.main)
                .withIcon(new ItemStack(SpawnEgg.fromType(ModEntities.WOOLY.get()).get())));
    }

    private NPCQuest.Builder createNPCQuest(ResourceLocation id, String task, String description, Consumer<NPCEntryBuilder> npcs,
                                            LootTable.Builder loot, Consumer<QuestEntryBuilder> cons) {
        NPCEntryBuilder npcsBuilder = new NPCEntryBuilder();
        npcs.accept(npcsBuilder);
        List<ResourceLocation> npcDataID = new ArrayList<>();
        npcsBuilder.questResponses.forEach((key, val) -> {
            npcDataID.add(key);
            this.questResponses.computeIfAbsent(key, _k -> new LinkedHashMap<>())
                    .put(id, val);
        });
        NPCQuest.Builder builder = new NPCQuest.Builder(id, getTask(id),
                npcDataID, id);
        builder.addDescription(getDescription(builder.getID()));
        this.translations.put(getTask(builder.getID()), task);
        this.translations.put(getDescription(builder.getID()), description);
        this.loot.put(id, loot);
        QuestEntryBuilder entryBuilder = new QuestEntryBuilder(id, this.translations);
        cons.accept(entryBuilder);
        for (int i = 0; i < entryBuilder.entries.size(); i++) {
            Map<String, QuestEntry> entries = entryBuilder.entries.get(i);
            ResourceLocation subID = new ResourceLocation(builder.getID().getNamespace(), builder.getID().getPath() + "_ref_" + i);
            Quest.Builder questBuilder = new Quest.Builder(subID, "NPC_SUBQUEST", BuiltInLootTables.EMPTY)
                    .withCategory(this.hidden);
            entries.forEach(questBuilder::addTaskEntry);
            this.addQuest(questBuilder);
            builder.withQuests(subID);
        }
        return builder;
    }

    public static String getTask(ResourceLocation id) {
        return String.format("%s.quest.%s", id.getNamespace(), id.getPath());
    }

    public static String getDescription(ResourceLocation id) {
        return String.format("%s.quest.%s.description", id.getNamespace(), id.getPath());
    }

    public static String getTaskDescription(ResourceLocation id, int sub, int task) {
        return String.format("%s.quest.%s.sub.%s.task.%s", id.getNamespace(), id.getPath(), sub, task);
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(RuneCraftory.MODID, name);
    }

    private static class QuestEntryBuilder {

        private final List<Map<String, QuestEntry>> entries = new ArrayList<>();
        private final Map<String, String> translations;
        private final ResourceLocation id;

        private QuestEntryBuilder(ResourceLocation id, Map<String, String> translations) {
            this.id = id;
            this.translations = translations;
        }

        void addEntry(QuestEntry entry) {
            this.addEntry("", _n -> entry);
        }

        void addEntry(String translation, Function<String, QuestEntry> entry) {
            if (this.entries.isEmpty()) {
                this.push();
                this.addEntry(new NPCTalk(null));
                this.push();
            }
            int subIdx = this.entries.size() - 1;
            Map<String, QuestEntry> current = this.entries.get(subIdx);
            int taskIdx = current.size();
            String desc = getTaskDescription(this.id, subIdx, taskIdx);
            current.put(taskIdx + "", entry.apply(desc));
            if (!translation.isEmpty()) {
                this.translations.put(desc, translation);
            }
        }

        void push() {
            this.entries.add(new HashMap<>());
        }
    }

    private static class NPCEntryBuilder {

        private final Map<ResourceLocation, NPCDataProvider.QuestResponseBuilder> questResponses = new HashMap<>();

        void addNPC(String npcId, NPCDataProvider.QuestResponseBuilder responses) {
            this.questResponses.put(id(npcId), responses);
        }
    }
}
