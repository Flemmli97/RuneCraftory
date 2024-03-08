package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.runecraftory.integration.simplequest.NPCQuest;
import io.github.flemmli97.runecraftory.integration.simplequest.QuestTasks;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.simplequests.api.QuestEntry;
import io.github.flemmli97.simplequests.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests.quest.QuestCategory;
import io.github.flemmli97.simplequests.quest.entry.QuestEntryImpls;
import io.github.flemmli97.simplequests.quest.types.Quest;
import io.github.flemmli97.simplequests.quest.types.SequentialQuest;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.advancements.critereon.BlockPredicate;
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

    public static final ResourceLocation MINING = id("quest/mining");
    public static final ResourceLocation TAMING = id("quest/tame_monster");
    public static final ResourceLocation SHIP_TURNIP = id("quest/ship_turnip");

    public QuestGen(DataGenerator gen) {
        super(gen, false);
    }

    @Override
    protected void add() {
        this.createNPCQuest(new NPCQuest.Builder(SHIP_TURNIP, getTask(SHIP_TURNIP),
                        List.of(id("shop_owner/male_1"), id("shop_owner/female_1")), SHIP_TURNIP)
                        .setRepeatDelay(-1)
                        .withCategory(this.main)
                        .withIcon(new ItemStack(ModItems.TURNIP_SEEDS.get())),
                List.of(of(m -> m.put("talk", new QuestTasks.NPCTalk(null))),
                        of(m -> m.put("shipping", new QuestTasks.ShippingEntry(ItemPredicate.Builder.item().of(ModItems.TURNIP.get()).build(), 1)))));

        this.createNPCQuest(new NPCQuest.Builder(MINING, getTask(MINING),
                        List.of(id("smith/male_1"), id("smith/female_1")), MINING)
                        .setRepeatDelay(-1)
                        .withCategory(this.main)
                        .withIcon(new ItemStack(ModItems.HAMMER_SCRAP.get())),
                List.of(of(m -> m.put("talk", new QuestTasks.NPCTalk(null))),
                        of(m -> m.put("mine", new QuestEntryImpls.BlockInteractEntry(ItemPredicate.ANY, BlockPredicate.Builder.block().of(RunecraftoryTags.ORES).build(),
                                10, false, false, true, "", "", "", EntityPredicate.ANY)))));

        this.createNPCQuest(new NPCQuest.Builder(TAMING, getTask(TAMING),
                        id("random_npc"), TAMING)
                        .setRepeatDelay(-1)
                        .withCategory(this.main)
                        .withIcon(new ItemStack(SpawnEgg.fromType(ModEntities.WOOLY.get()).get())),
                List.of(of(m -> m.put("talk", new QuestTasks.NPCTalk(null))),
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
