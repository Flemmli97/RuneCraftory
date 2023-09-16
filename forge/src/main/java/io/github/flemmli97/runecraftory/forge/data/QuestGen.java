package io.github.flemmli97.runecraftory.forge.data;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.integration.simplequest.QuestTasks;
import io.github.flemmli97.runecraftory.common.integration.simplequest.SimpleQuestIntegration;
import io.github.flemmli97.runecraftory.common.registry.ModEntities;
import io.github.flemmli97.runecraftory.common.registry.ModItems;
import io.github.flemmli97.simplequests.datapack.provider.QuestProvider;
import io.github.flemmli97.simplequests.quest.Quest;
import io.github.flemmli97.simplequests.quest.QuestCategory;
import io.github.flemmli97.tenshilib.common.item.SpawnEgg;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class QuestGen extends QuestProvider {

    public QuestGen(DataGenerator gen) {
        super(gen, false);
    }

    @Override
    protected void add() {
        QuestCategory main = new QuestCategory.Builder(SimpleQuestIntegration.QUEST_CATEGORY, RuneCraftory.MODID + ".quests.category")
                .unselectable()
                .setHidden()
                .countSameCategoryOnly()
                .setMaxConcurrent(1).build();
        this.addQuest(new Quest.Builder(id("ship_turnip"), "Ship a turnip", BuiltInLootTables.EMPTY)
                .withCategory(main)
                .withIcon(new ItemStack(ModItems.turnip.get()))
                .withSubmissionTrigger(SimpleQuestIntegration.QUEST_TRIGGER)
                .setRepeatDelay(-1)
                .addTaskEntry("turnip", new QuestTasks.ShippingEntry(ItemPredicate.Builder.item().of(ModItems.turnip.get()).build(), 1)));

        this.addQuest(new Quest.Builder(id("tame_monster"), "Tame a monster", BuiltInLootTables.EMPTY)
                .withCategory(main)
                .withIcon(new ItemStack(SpawnEgg.fromType(ModEntities.WOOLY.get()).get()))
                .withSubmissionTrigger(SimpleQuestIntegration.QUEST_TRIGGER)
                .setRepeatDelay(-1)
                .addTaskEntry("tame", new QuestTasks.TamingEntry(EntityPredicate.ANY, 1, "Tame one monster")));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(RuneCraftory.MODID, name);
    }
}
