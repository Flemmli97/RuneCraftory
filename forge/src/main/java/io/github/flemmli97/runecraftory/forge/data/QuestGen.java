package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.integration.simplequest.NPCQuest;
import io.github.flemmli97.runecraftory.common.integration.simplequest.QuestTasks;
import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.simplequests.api.QuestEntry;
import io.github.flemmli97.simplequests.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests.quest.QuestCategory;
import io.github.flemmli97.simplequests.quest.types.Quest;
import io.github.flemmli97.simplequests.quest.types.SequentialQuest;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class QuestGen extends QuestProvider {

    private final QuestCategory main = new QuestCategory.Builder(SimpleQuestIntegration.QUEST_CATEGORY, RuneCraftory.MODID + ".quests.category")
            .unselectable()
            .setHidden()
            .countSameCategoryOnly()
            .setSilent()
            .setMaxConcurrent(1).build();
    private final QuestCategory hidden = new QuestCategory.Builder(id("hidden_quests"), RuneCraftory.MODID + ".quests.category.hidden")
            .unselectable()
            .setHidden()
            .countSameCategoryOnly()
            .setSilent()
            .setMaxConcurrent(1).build();

    public QuestGen(DataGenerator gen) {
        super(gen, false);
    }

    @Override
    protected void add() {
        this.addQuest(new Quest.Builder(id("ship_turnip"), "Ship a turnip", BuiltInLootTables.EMPTY)
                .withCategory(this.main)
                .withIcon(new ItemStack(ModItems.turnip.get()))
                .withSubmissionTrigger(SimpleQuestIntegration.QUEST_BOARD_TRIGGER)
                .setRepeatDelay(-1)
                .addTaskEntry("turnip", new QuestTasks.ShippingEntry(ItemPredicate.Builder.item().of(ModItems.turnip.get()).build(), 1)));

        this.createNPCQuest(new NPCQuest.Builder(id("tame_monster"), getTask(id("tame_monster")),
                        id("random_npc"), new ResourceLocation(RuneCraftory.MODID, "taming_quest_brush"))
                        .setRepeatDelay(-1)
                        .withCategory(this.main)
                        .withIcon(new ItemStack(SpawnEgg.fromType(ModEntities.WOOLY.get()).get())),
                List.of(of(m -> m.put("talk", new QuestTasks.NPCTalk("", false))),
                        of(m -> m.put("tame", new QuestTasks.TamingEntry(EntityPredicate.ANY, 1, "")))));
    }

    private void createNPCQuest(NPCQuest.Builder builder, Map<String, QuestEntry> entries) {
        this.createNPCQuest(builder, List.of(entries));
    }

    private void createNPCQuest(NPCQuest.Builder builder, List<Map<String, QuestEntry>> entriesList) {
        if (entriesList.isEmpty())
            return;
        ResourceLocation questID;
        if (entriesList.size() > 1) {
            questID = new ResourceLocation(builder.getID().getNamespace(), builder.getID().getPath() + "_ref");
            SequentialQuest.Builder seq = new SequentialQuest.Builder(questID, "NPC_SUBQUEST", BuiltInLootTables.EMPTY);
            for (int i = 0; i < entriesList.size(); i++) {
                Map<String, QuestEntry> entries = entriesList.get(i);
                ResourceLocation subID = new ResourceLocation(builder.getID().getNamespace(), builder.getID().getPath() + "_ref_" + i);
                Quest.Builder questBuilder = new Quest.Builder(subID, "NPC_SUBQUEST", BuiltInLootTables.EMPTY)
                        .withCategory(this.hidden);
                entries.forEach(questBuilder::addTaskEntry);
                this.addQuest(questBuilder);
                seq.addQuest(subID);
            }
            this.addQuest(seq);
        } else {
            Map<String, QuestEntry> entries = entriesList.get(0);
            questID = new ResourceLocation(builder.getID().getNamespace(), builder.getID().getPath() + "_ref");
            Quest.Builder questBuilder = new Quest.Builder(questID, "NPC_SUBQUEST", BuiltInLootTables.EMPTY)
                    .withCategory(this.hidden);
            entries.forEach(questBuilder::addTaskEntry);
            this.addQuest(questBuilder);
        }
        builder.addDescription(getDescription(builder.getID()));
        this.addQuest(builder.withQuest(questID));
    }

    public static String getTask(ResourceLocation id) {
        return String.format("%s.quest.%s", id.getPath(), id.getNamespace());
    }

    public static String getDescription(ResourceLocation id) {
        return String.format("%s.quest.%s_description", id.getPath(), id.getNamespace());
    }

    private static <K, V> Map<K, V> of(Consumer<Map<K, V>> cons) {
        Map<K, V> map = new LinkedHashMap<>();
        cons.accept(map);
        return map;
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(RuneCraftory.MODID, name);
    }
}
