package io.github.flemmli97.runecraftory.common.integration.simplequest;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class SimpleQuestIntegration {

    private static SimpleQuestIntegration INST;

    public static final ResourceLocation QUEST_CATEGORY = new ResourceLocation(RuneCraftory.MODID, "quests");

    public static final String QUEST_BOARD_TRIGGER = RuneCraftory.MODID + "_quest_board_trigger";

    public static final String QUEST_GUI_KEY = RuneCraftory.MODID + ".quest.gui.button";

    public static SimpleQuestIntegration INST() {
        if (INST == null) {
            if (RuneCraftory.simpleQuests)
                INST = new SimpleQuestIntegrationImpl();
            else
                INST = new SimpleQuestIntegration();
        }
        return INST;
    }

    SimpleQuestIntegration() {
    }

    public void register() {
    }

    public void openGui(ServerPlayer player) {
        player.sendMessage(new TranslatableComponent("runecraftory.dependency.simplequest.missing").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
    }

    public void acceptQuest(ServerPlayer player, ResourceLocation res) {
    }

    public void resetQuest(ServerPlayer player, ResourceLocation res) {
    }

    public void resetQuestData(ServerPlayer player) {
    }

    public void acceptQuestRandom(ServerPlayer player, EntityNPCBase npc, ResourceLocation res) {
    }

    public Map<ResourceLocation, QuestBase> getQuestsFor(ServerPlayer player) {
        return Map.of();
    }

    public void questBoardComplete(ServerPlayer player) {
    }

    public Map<ResourceLocation, ProgressState> triggerNPCTalk(ServerPlayer player, EntityNPCBase npc) {
        return Map.of();

    }

    public void triggerShipping(ServerPlayer player, ItemStack stack) {
    }

    public void triggerTaming(ServerPlayer player, BaseMonster monster) {
    }

    public ProgressState checkCompletionQuest(ServerPlayer player, EntityNPCBase npc) {
        return ProgressState.NO;
    }

    public void submit(ServerPlayer player, EntityNPCBase npc) {
    }

    public void removeQuestFor(ServerPlayer player, EntityNPCBase npc) {
    }

    public void removeNPCQuestsFor(ServerPlayer player) {
    }

    public ResourceLocation questForExists(ServerPlayer player, EntityNPCBase npc) {
        return null;
    }
}
