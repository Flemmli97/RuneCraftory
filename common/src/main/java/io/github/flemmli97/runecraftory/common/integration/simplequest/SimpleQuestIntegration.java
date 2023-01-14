package io.github.flemmli97.runecraftory.common.integration.simplequest;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.simplequests.api.SimpleQuestAPI;
import io.github.flemmli97.simplequests.datapack.QuestEntryRegistry;
import io.github.flemmli97.simplequests.datapack.QuestsManager;
import io.github.flemmli97.simplequests.gui.QuestGui;
import io.github.flemmli97.simplequests.quest.Quest;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Map;

public class SimpleQuestIntegration {

    public static final ResourceLocation QUEST_CATEGORY = new ResourceLocation(RuneCraftory.MODID, "quests");

    public static final String QUEST_TRIGGER = RuneCraftory.MODID + "_trigger";

    public static void register() {
        if (!RuneCraftory.simpleQuests) {
            return;
        }
        QuestEntryRegistry.registerSerializer(QuestTasks.ShippingEntry.ID, QuestTasks.ShippingEntry::fromJson);
        QuestEntryRegistry.registerSerializer(QuestTasks.LevelEntry.ID, QuestTasks.LevelEntry::fromJson);
        QuestEntryRegistry.registerSerializer(QuestTasks.SkillLevelEntry.ID, QuestTasks.SkillLevelEntry::fromJson);
        QuestEntryRegistry.registerSerializer(QuestTasks.TamingEntry.ID, QuestTasks.TamingEntry::fromJson);
    }

    public static void openGui(ServerPlayer player) {
        if (!RuneCraftory.simpleQuests) {
            player.sendMessage(new TranslatableComponent("dependency.simplequest.missing"), Util.NIL_UUID);
            return;
        }
        //TODO Custom gui
        QuestGui.openGui(player, QuestsManager.instance().getQuestCategory(QUEST_CATEGORY), false);
    }

    public static void tryComplete(ServerPlayer player) {
        if (!RuneCraftory.simpleQuests) {
            return;
        }
        SimpleQuestAPI.submit(player, QUEST_TRIGGER, false);
    }

    public static Map<ResourceLocation, Quest> getQuests() {
        if (!RuneCraftory.simpleQuests) {
            return Map.of();
        }
        return QuestsManager.instance().getQuestsForCategoryID(QUEST_CATEGORY);
    }

    public static Collection<Quest> getQuestIds() {
        return getQuests().values();
    }
}
