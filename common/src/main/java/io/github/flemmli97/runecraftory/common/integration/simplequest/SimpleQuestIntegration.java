package io.github.flemmli97.runecraftory.common.integration.simplequest;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.attachment.player.QuestTracker;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests.api.SimpleQuestAPI;
import io.github.flemmli97.simplequests.datapack.QuestEntryRegistry;
import io.github.flemmli97.simplequests.datapack.QuestsManager;
import io.github.flemmli97.simplequests.gui.QuestGui;
import io.github.flemmli97.simplequests.quest.Quest;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.Set;

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

    public static Set<ResourceLocation> getQuestIds() {
        return getQuests().keySet();
    }

    public static void triggerShipping(ServerPlayer player, ItemStack stack) {
        if (!RuneCraftory.simpleQuests) {
            return;
        }
        SimpleQuestAPI.trigger(player, QuestTasks.ShippingEntry.class, (name, e, prog) -> Platform.INSTANCE.getPlayerData(player)
                        .map(d -> e.predicate.matches(stack) && d.questTracker.getTrackedAndIncrease(prog.getQuest().id, name, QuestTracker.TrackedTypes.SHIPPING, stack.getCount()) >= e.amount)
                        .orElse(false),
                (prog, pair) -> Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.questTracker.onComplete(prog.getQuest().id, pair.getFirst(), QuestTracker.TrackedTypes.SHIPPING)));
    }

    public static void triggerTaming(ServerPlayer player, BaseMonster monster) {
        if (!RuneCraftory.simpleQuests) {
            return;
        }
        SimpleQuestAPI.trigger(player, QuestTasks.TamingEntry.class, (name, e, prog) -> Platform.INSTANCE.getPlayerData(player)
                        .map(d -> e.predicate().matches(player, monster) &&
                                d.questTracker.getTrackedAndIncrease(prog.getQuest().id, name, QuestTracker.TrackedTypes.SHIPPING, 1) >= e.amount())
                        .orElse(false),
                (prog, pair) -> Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.questTracker.onComplete(prog.getQuest().id, pair.getFirst(), QuestTracker.TrackedTypes.TAMING)));
    }

    public static boolean questExists(ResourceLocation id) {
        if (!RuneCraftory.simpleQuests) {
            return false;
        }
        return QuestsManager.instance().getAllQuests().containsKey(id);
    }
}
