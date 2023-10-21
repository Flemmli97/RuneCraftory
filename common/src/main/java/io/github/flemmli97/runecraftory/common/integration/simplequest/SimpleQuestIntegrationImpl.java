package io.github.flemmli97.runecraftory.common.integration.simplequest;

import io.github.flemmli97.runecraftory.common.attachment.player.QuestTracker;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.S2COpenQuestGui;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests.api.QuestCompletionState;
import io.github.flemmli97.simplequests.api.SimpleQuestAPI;
import io.github.flemmli97.simplequests.datapack.QuestBaseRegistry;
import io.github.flemmli97.simplequests.datapack.QuestEntryRegistry;
import io.github.flemmli97.simplequests.datapack.QuestsManager;
import io.github.flemmli97.simplequests.player.PlayerData;
import io.github.flemmli97.simplequests.player.QuestProgress;
import io.github.flemmli97.simplequests.quest.types.QuestBase;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleQuestIntegrationImpl extends SimpleQuestIntegration {

    SimpleQuestIntegrationImpl() {
        super();
    }

    @Override
    public void register() {
        QuestEntryRegistry.registerSerializer(QuestTasks.ShippingEntry.ID, QuestTasks.ShippingEntry.CODEC);
        QuestEntryRegistry.registerSerializer(QuestTasks.LevelEntry.ID, QuestTasks.LevelEntry.CODEC);
        QuestEntryRegistry.registerSerializer(QuestTasks.SkillLevelEntry.ID, QuestTasks.SkillLevelEntry.CODEC);
        QuestEntryRegistry.registerSerializer(QuestTasks.TamingEntry.ID, QuestTasks.TamingEntry.CODEC);
        QuestEntryRegistry.registerSerializer(QuestTasks.NPCTalk.ID, QuestTasks.NPCTalk.CODEC);
        QuestBaseRegistry.registerSerializer(NPCQuest.ID, NPCQuest::of);
        QuestBaseRegistry.registerSerializer(QuestBoardQuest.ID, QuestBoardQuest::of);
    }

    @Override
    public void openGui(ServerPlayer player) {
        Map<ResourceLocation, QuestBase> quest = SimpleQuestIntegration.INST().getQuestsFor(player);
        PlayerData data = PlayerData.get(player);
        Platform.INSTANCE.sendToClient(new S2COpenQuestGui(data.getCurrentQuest().stream().anyMatch(p -> p.getQuest() instanceof QuestBoardQuest), quest.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> new ClientSideQuestDisplay(e.getKey(), e.getValue().getTask(player), e.getValue().getDescription(player), data.isActive(e.getKey()))).toList()), player);
        ((SimpleQuestData) PlayerData.get(player)).setQuestboardQuests(quest);
    }

    @Override
    public void acceptQuest(ServerPlayer player, ResourceLocation res) {
        PlayerData data = PlayerData.get(player);
        if (((SimpleQuestData) data).getQuestboardQuests() == null)
            return;
        QuestBase quest = ((SimpleQuestData) data).getQuestboardQuests().get(res);
        if (quest != null) {
            if (!(quest instanceof NPCQuest npcQuest) || WorldHandler.get(player.getServer()).npcHandler.doesNPCExist(npcQuest.getNpcUuid())) {
                if (data.acceptQuest(quest, 0)) {
                    player.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_YES, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1.2f));
                }
            } else {
                player.sendMessage(new TranslatableComponent("runecraftory.quest.npc.none").withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
            }
        }
    }

    @Override
    public void resetQuest(ServerPlayer player, ResourceLocation res) {
        PlayerData.get(player).reset(res, true, false);
    }

    @Override
    public void resetQuestData(ServerPlayer player) {
        ((SimpleQuestData) PlayerData.get(player)).setQuestboardQuests(null);
    }

    @Override
    public void acceptQuestRandom(ServerPlayer player, EntityNPCBase npc, ResourceLocation res) {
        PlayerData data = PlayerData.get(player);
        QuestBase quest = QuestsManager.instance().getAllQuests().get(res);
        if (quest != null) {
            if (data.acceptQuest(NPCQuest.of(NPCQuest.withUuid(quest.id, npc.getUUID()),
                    npc, quest), 0)) {
                player.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_YES, player.getSoundSource(), player.getX(), player.getY(), player.getZ(), 1, 1.2f));
            }
        }
    }

    @Override
    public Map<ResourceLocation, QuestBase> getQuestsFor(ServerPlayer player) {
        PlayerData data = PlayerData.get(player);
        return Stream.concat(QuestsManager.instance().getQuestsForCategoryID(SimpleQuestIntegration.QUEST_CATEGORY)
                        .entrySet().stream().filter(e -> data.canAcceptQuest(e.getValue()) == PlayerData.AcceptType.ACCEPT && e.getValue().isUnlocked(player)) //TODO
                        .flatMap(e -> {
                            if (e.getValue() instanceof NPCQuest npcQuest) {
                                return NPCQuest.of(npcQuest, player).stream();
                            }
                            return Stream.of(new QuestBoardQuest(e.getValue()));
                        }), data.getCurrentQuest().stream().map(QuestProgress::getQuest).filter(quest -> quest.category.id.equals(SimpleQuestIntegration.QUEST_CATEGORY)))
                .collect(Collectors.toMap(
                        q -> q.id,
                        q -> q,
                        (e1, e2) -> e1,
                        HashMap::new
                ));
    }

    @Override
    public void questBoardComplete(ServerPlayer player) {
        SimpleQuestAPI.submit(player, SimpleQuestIntegration.QUEST_BOARD_TRIGGER, false);
    }

    @Override
    public Map<ResourceLocation, QuestCompletionState> triggerNPCTalk(ServerPlayer player, EntityNPCBase npc) {
        return SimpleQuestAPI.trigger(player, QuestTasks.NPCTalk.class, (name, e, prog) -> npc.getUUID().equals(e.targetNPC),
                (prog, pair) -> {
                });
    }

    @Override
    public void triggerShipping(ServerPlayer player, ItemStack stack) {
        SimpleQuestAPI.trigger(player, QuestTasks.ShippingEntry.class, (name, e, prog) -> Platform.INSTANCE.getPlayerData(player)
                        .map(d -> e.predicate.matches(stack) && d.questTracker.getTrackedAndIncrease(prog.getQuest().id, name, QuestTracker.TrackedTypes.SHIPPING, stack.getCount()) >= e.amount)
                        .orElse(false),
                (prog, pair) -> Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.questTracker.onComplete(prog.getQuest().id, pair.getFirst(), QuestTracker.TrackedTypes.SHIPPING)));
    }

    @Override
    public void triggerTaming(ServerPlayer player, BaseMonster monster) {
        SimpleQuestAPI.trigger(player, QuestTasks.TamingEntry.class, (name, e, prog) -> Platform.INSTANCE.getPlayerData(player)
                        .map(d -> e.predicate().matches(player, monster) &&
                                d.questTracker.getTrackedAndIncrease(prog.getQuest().id, name, QuestTracker.TrackedTypes.SHIPPING, 1) >= e.amount())
                        .orElse(false),
                (prog, pair) -> Platform.INSTANCE.getPlayerData(player).ifPresent(d -> d.questTracker.onComplete(prog.getQuest().id, pair.getFirst(), QuestTracker.TrackedTypes.TAMING)));
    }

    @Override
    public boolean checkCompletionQuest(ServerPlayer player, EntityNPCBase npc) {
        return PlayerData.get(player).getCurrentQuest().stream().anyMatch(p -> p.tryComplete(player, npc.getUUID().toString()) == QuestCompletionState.COMPLETE);
    }

    @Override
    public void submit(ServerPlayer player, EntityNPCBase npc) {
        if (PlayerData.get(player).submit(npc.getUUID().toString(), false).isEmpty()) {
            SimpleQuestAPI.trigger(player, QuestTasks.NPCTalk.class, (name, e, prog) -> npc.getUUID().equals(e.targetNPC),
                    (prog, pair) -> {
                    }, npc.getUUID().toString());
        }
    }

    @Override
    public void removeQuestFor(ServerPlayer player, EntityNPCBase npc) {
        PlayerData data = PlayerData.get(player);
        List<ResourceLocation> toRemove = data.getCurrentQuest().stream().filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid()))
                .map(p -> p.getQuest().id).toList();
        toRemove.forEach(r -> data.reset(r, true, false));
    }

    @Override
    public void removeNPCQuestsFor(ServerPlayer player) {
        PlayerData data = PlayerData.get(player);
        List<ResourceLocation> toRemove = data.getCurrentQuest().stream().filter(p -> p.getQuest() instanceof NPCQuest npcQuest && !WorldHandler.get(player.getServer()).npcHandler.doesNPCExist(npcQuest.getNpcUuid()))
                .map(p -> p.getQuest().id).toList();
        toRemove.forEach(r -> data.reset(r, true, false));
    }

    @Override
    public ResourceLocation questForExists(ServerPlayer player, EntityNPCBase npc) {
        return PlayerData.get(player).getCurrentQuest().stream().filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid()))
                .map(p -> ((NPCQuest) p.getQuest()).getOriginID()).findFirst().orElse(null);
    }
}
