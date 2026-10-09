package io.github.flemmli97.runecraftory.neoforge.data;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.api.datapack.provider.AdditionalLanguages;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.quests.quest.QuestDialogueEntry;
import io.github.flemmli97.runecraftory.common.quests.quest.QuestResponses;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryItems;
import io.github.flemmli97.simplequests_api.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests_api.impls.quests.TasksQuestDefinition;
import io.github.flemmli97.simplequests_api.impls.tasks.BlockInteractTask;
import io.github.flemmli97.simplequests_api.quest.QuestCategory;
import io.github.flemmli97.simplequests_api.quest.QuestHolder;
import io.github.flemmli97.simplequests_api.quest.entry.QuestTask;
import io.github.flemmli97.simplequests_api.util.DescriptiveValue;
import net.minecraft.advancements.critereon.BlockPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class QuestGen extends QuestProvider implements AdditionalLanguages {

    private final QuestCategory main = new QuestCategory.Builder(QuestHandler.QUEST_CATEGORY, RuneCraftory.MODID + ".quests.category")
            .needContexts(QuestHandler.QUEST_CONTEXT)
            .build();

    private final Map<String, String> translations = new LinkedHashMap<>();
    public final Map<ResourceKey<LootTable>, LootTable.Builder> loot = new HashMap<>();
    public final Map<ResourceLocation, Pair<String, ConversationSet.Builder>> questResponses = new HashMap<>();

    public QuestGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, false);
    }

    @Override
    protected void add(HolderLookup.Provider provider) {
        this.addNPCQuest(id("ship_turnip"), "First Shipment!", "Come see me.",
                List.of(id("shop_owner/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.TURNIP_SEEDS.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Are you here for my request?
                            I will show you how to ship items to make money: Shipping items is very simple. First you need a shipping bin. It can hold any shippable items in it.
                            Put the items you want to ship in it and everyday in the morning your items will be automatically shipped. Lets try it out now: I want you to ship a turnip."""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), "Please ship a turnip."),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), "Great. There are a lot of items you can ship to make money. Here take" +
                                    "these turnip seeds. It should come in handy."));
                    builder.addEntry("Ship %s turnips", desc -> new ShippingTask("", DescriptiveValue.list(ItemPredicate.Builder.item().of(RuneCraftoryItems.TURNIP.get()).build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.setRepeatDelay(-1).withCategory(this.main));

        this.addNPCQuest(id("mining"), "Acquire Hardware??", "Come see me.",
                List.of(id("smith/1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(RuneCraftoryItems.HAMMER_SCRAP.get())))
                        .withPool(LootPool.lootPool()
                                .add(LootItem.lootTableItem(Items.IRON_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 5))))
                                .add(LootItem.lootTableItem(Items.COPPER_INGOT).apply(SetItemCountFunction.setCount(UniformGenerator.between(6, 10))))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            You saw my request? Great!
                            You might have noticed various strange stones around the world. Those are minerals and they provide various different ores.
                            I want you to mine 10 of them for me."""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), "To mine minerals you need atleast an iron pickaxe or a hammer. " +
                                    "I want you to mine 10 mineral blocks for me."),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), "Nice! Mining ores increases your mining level. " +
                                    "With higher level you can get better ores from minerals. Here take this hammer, it should make mining minerals a bit easier."));
                    builder.addEntry("Break %s mineral blocks", desc -> new BlockInteractTask(DescriptiveValue.list(BlockPredicate.Builder.block().of(RunecraftoryTags.Blocks.ORES).build(), desc).build(),
                            List.of(), ConstantValue.exactly(10), false, false, true, "", null));
                }, b -> b.setRepeatDelay(-1).withCategory(this.main));

        this.addNPCQuest(id("tame_monster"), "Tame a monster", "I need you to tame a monster. Come see me.",
                List.of(id("random_npc_1")),
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .add(LootItem.lootTableItem(RuneCraftoryItems.BRUSH.get()))),
                builder -> {
                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
                            Did you know that you can tame the monsters in this world?
                            You would need to setup a barn first and then just give them an item. \n
                            With that said I would like you to tame a monster."""), null, null);
                    builder.addEntry(new NPCTalkTask());
                    builder.push(null,
                            new ConversationSet.Builder(builder.translationKeyOf("active"), "You still need to tame a monster.\n" +
                                    "Some monsters prefer certain items more."),
                            new ConversationSet.Builder(builder.translationKeyOf("end"), "I see you've successfully tamed a monster. Congrats!"));
                    builder.addEntry("Tame a monster", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
                }, b -> b.setRepeatDelay(-1).withCategory(this.main));

//        this.addNPCQuest(id("test_quest"), "Test Quest", "A test quest",
//                List.of(id("random_npc_1")),
//                LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(Items.STONE))),
//                builder -> {
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start"), """
//                            Start dialogue. Now go tame a monster"""), null, null);
//                    builder.addEntry(new NPCTalkTask());
//                    builder.push(null,
//                            new ConversationSet.Builder(builder.translationKeyOf("active"), "Active dialogue 1. Tame a monster"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end"), "End dialogue 1"));
//                    builder.addEntry("Tame a monster", desc -> new TamingTask("", DescriptiveValue.list(EntityPredicate.Builder.entity().build(), desc).build(), ConstantValue.exactly(1)));
//                    builder.push(new ConversationSet.Builder(builder.translationKeyOf("start_1"), """
//                                    Talk to %quest.target_npc:0%"""),
//                            new ConversationSet.Builder(builder.translationKeyOf("active_1"), "Active dialogue 1. Talk to %quest.target_npc:0%"),
//                            new ConversationSet.Builder(builder.translationKeyOf("end_1"), "End dialogue 2. Quest done"));
//                    builder.addEntry(new NPCTalkTask(id("random_npc_1"), builder.getConversation("talk_random_npc", new ConversationSet.Builder(builder.translationKeyOf("talk_random_npc"), "I am responding to a talk task"))));
//                }, b -> b.setRepeatDelay(-1).withCategory(this.main));
    }

    private void addNPCQuest(ResourceLocation id, String task, String description,
                             List<ResourceLocation> npcs, LootTable.Builder loot,
                             Consumer<SubQuestBuilder> tasks, Consumer<NPCQuestDefinition.Builder> builderConsumer) {
        NPCQuestDefinition.Builder builder = new NPCQuestDefinition.Builder(getTask(id), npcs, id);
        this.translations.put(getTask(id), task);
        this.translations.put(getDescription(id), description);
        this.loot.put(ResourceKey.create(Registries.LOOT_TABLE, id), loot);
        SubQuestBuilder subQuests = new SubQuestBuilder(id);
        tasks.accept(subQuests);
        for (SubQuestEntry subQuest : subQuests.entries) {
            TasksQuestDefinition.Builder taskQuest = new TasksQuestDefinition.Builder("", BuiltInLootTables.EMPTY.location());
            subQuest.tasks.forEach(taskQuest::addTaskEntry);
            builder.withQuests(new QuestDialogueEntry(new QuestHolder(taskQuest.build()),
                    new QuestResponses(Optional.ofNullable(subQuest.start), Optional.ofNullable(subQuest.active), Optional.ofNullable(subQuest.end))));
        }
        builderConsumer.accept(builder);
        this.addQuest(id, builder);
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
        return RuneCraftory.modRes(name);
    }

    @Override
    public Map<String, String> translations() {
        return this.translations;
    }

    private class SubQuestBuilder {

        private final List<SubQuestEntry> entries = new ArrayList<>();
        private final ResourceLocation id;

        private SubQuestBuilder(ResourceLocation id) {
            this.id = id;
        }

        public String translationKeyOf(String key) {
            return String.format("quest.conversation.%s.%s", this.id, key);
        }

        private String translationPath() {
            return "quests/" + this.id.getPath();
        }

        public ResourceLocation getConversation(String idSuffix, ConversationSet.Builder conv) {
            ResourceLocation id = this.id.withPath(p -> "quest/" + p + "/" + idSuffix);
            QuestGen.this.questResponses.put(id, Pair.of(this.translationPath(), conv));
            return id;
        }

        void addEntry(QuestTask<?> entry) {
            this.addEntry("", _n -> entry);
        }

        void addEntry(String translation, Function<String, QuestTask<?>> entry) {
            if (this.entries.isEmpty()) {
                throw new IllegalStateException("You need to push first");
            }
            int subIdx = this.entries.size() - 1;
            SubQuestEntry current = this.entries.get(subIdx);
            int taskIdx = current.tasks().size();
            String desc = getTaskDescription(this.id, subIdx, taskIdx);
            current.tasks().put(taskIdx + "", entry.apply(desc));
            if (!translation.isEmpty()) {
                QuestGen.this.translations.put(desc, translation);
            }
        }

        void push(ConversationSet.Builder start, ConversationSet.Builder active, ConversationSet.Builder end) {
            int idx = this.entries.size();
            ResourceLocation startID = null;
            if (start != null) {
                startID = this.id.withPath(p -> "quest/" + p + "/start_" + idx);
                QuestGen.this.questResponses.put(startID, Pair.of(this.translationPath(), start));
            }
            ResourceLocation activeID = null;
            if (active != null) {
                activeID = this.id.withPath(p -> "quest/" + p + "/active_" + idx);
                QuestGen.this.questResponses.put(activeID, Pair.of(this.translationPath(), active));
            }
            ResourceLocation endID = null;
            if (end != null) {
                endID = this.id.withPath(p -> "quest/" + p + "/end_" + idx);
                QuestGen.this.questResponses.put(endID, Pair.of(this.translationPath(), end));
            }
            this.entries.add(new SubQuestEntry(startID, activeID, endID, new HashMap<>()));
        }
    }

    private record SubQuestEntry(ResourceLocation start, ResourceLocation active, ResourceLocation end,
                                 Map<String, QuestTask<?>> tasks) {
    }

}
