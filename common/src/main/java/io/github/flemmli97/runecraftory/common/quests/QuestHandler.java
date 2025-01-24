package io.github.flemmli97.runecraftory.common.quests;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.S2COpenQuestGui;
import io.github.flemmli97.runecraftory.common.quests.progress.NPCTalkTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.ShippingTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.TamingTracker;
import io.github.flemmli97.runecraftory.common.quests.tasks.LevelEntry;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalk;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingEntry;
import io.github.flemmli97.runecraftory.common.quests.tasks.SkillLevelEntry;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingEntry;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.mixinhelper.QuestDataGet;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.QuestState;
import io.github.flemmli97.simplequests_api.registry.PlayerQuestDataRegistry;
import io.github.flemmli97.simplequests_api.registry.ProgressionTrackerRegistry;
import io.github.flemmli97.simplequests_api.registry.QuestBaseRegistry;
import io.github.flemmli97.simplequests_api.registry.QuestEntryRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestHandler {

    public static final ResourceLocation QUEST_CATEGORY = new ResourceLocation(RuneCraftory.MODID, "quests");
    public static final ResourceLocation QUEST_CONTEXT = new ResourceLocation(RuneCraftory.MODID, "contexts");

    public static final String QUEST_BOARD_TRIGGER = RuneCraftory.MODID + "_quest_board_trigger";

    public static void register() {
        QuestEntryRegistry.registerSerializer(ShippingEntry.ID, ShippingEntry.CODEC);
        QuestEntryRegistry.registerSerializer(LevelEntry.ID, LevelEntry.CODEC);
        QuestEntryRegistry.registerSerializer(SkillLevelEntry.ID, SkillLevelEntry.CODEC);
        QuestEntryRegistry.registerSerializer(TamingEntry.ID, TamingEntry.CODEC);
        QuestEntryRegistry.registerSerializer(NPCTalk.ID, NPCTalk.CODEC);
        QuestBaseRegistry.registerSerializer(NPCQuest.ID, NPCQuest::of);
        QuestBaseRegistry.registerSerializer(QuestBoardQuest.ID, QuestBoardQuest::of);
        ProgressionTrackerRegistry.registerSerializer(ShippingTracker.KEY, ShippingTracker::new);
        ProgressionTrackerRegistry.registerSerializer(TamingTracker.KEY, TamingTracker::new);
        ProgressionTrackerRegistry.registerSerializer(NPCTalkTracker.KEY, NPCTalkTracker::new);
        PlayerQuestDataRegistry.registerFetcher(new ResourceLocation(RuneCraftory.MODID, "quest_data"), QuestHandler::getData);
    }

    public static QuestData getData(ServerPlayer player) {
        return ((QuestDataGet) player).runecraftory$getQuestData();
    }

    public static void openGui(ServerPlayer player) {
        Map<ResourceLocation, QuestBase> quest = getQuestsFor(player);
        QuestData data = getData(player);
        Platform.INSTANCE.sendToClient(new S2COpenQuestGui(data.getCurrentQuest().stream().anyMatch(p -> p.getQuest() instanceof QuestBoardQuest), quest.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    List<MutableComponent> description = e.getValue().getDescription(player);
                    EntityNPCBase npc;
                    if (e.getValue() instanceof NPCQuest npcQuest && (npc = npcQuest.getNpc(player.level)) != null) {
                        description = Stream.concat(Stream.of(new TranslatableComponent("runecraftory.quest.npc.header", npc.getName(), npc.blockPosition().getX(),
                                        npc.blockPosition().getY(), npc.blockPosition().getZ()).withStyle(ChatFormatting.GOLD),
                                (MutableComponent) TextComponent.EMPTY), description.stream()).toList();
                        return new ClientSideQuestDisplay(e.getKey(), e.getValue().getTask(player), description,
                                npc.lookFeatures, npc.getLook().playerSkin(), data.isActive(e.getKey()));
                    }
                    return new ClientSideQuestDisplay(e.getKey(), e.getValue().getTask(player), description,
                            null, null, data.isActive(e.getKey()));
                }).toList()), player);
        data.setQuestboardQuests(quest);
    }

    public static void acceptQuestRandom(ServerPlayer player, EntityNPCBase npc, ResourceLocation res) {
        QuestData data = getData(player);
        QuestBase quest = QuestsManager.instance().getQuest(res);
        if (quest != null && questForExists(player, npc) == null) {
            if (data.acceptQuest(NPCQuest.of(NPCQuest.withUuid(quest.id, npc.getUUID()), npc, quest))) {
                npc.updater.acceptRandomQuest(player);
            }
        }
    }

    public static Map<ResourceLocation, QuestBase> getQuestsFor(ServerPlayer player) {
        QuestData data = getData(player);
        return Stream.concat(QuestsManager.instance().getQuestsForCategory(QUEST_CATEGORY, QUEST_CONTEXT)
                                .entrySet().stream()
                                .flatMap(e -> {
                                    if (e.getValue() instanceof NPCQuest npcQuest) {
                                        return NPCQuest.of(npcQuest, player).stream();
                                    }
                                    return Stream.of(new QuestBoardQuest(e.getValue()));
                                })
                                .filter(q -> data.canAcceptQuest(q, true) == QuestData.AcceptType.ACCEPT)
                        , data.getCurrentQuest().stream().map(QuestProgress::getQuest).filter(quest -> quest.category.id.equals(QUEST_CATEGORY)))
                .collect(Collectors.toMap(
                        q -> q.id,
                        q -> q,
                        (e1, e2) -> e1,
                        HashMap::new
                ));
    }

    public static QuestState checkCompletionQuest(ServerPlayer player, EntityNPCBase npc) {
        QuestData data = getData(player);
        return data.getCurrentQuest().stream()
                .map(p -> p.tryComplete(data, npc.getUUID().toString()))
                .filter(s -> s != QuestState.NO)
                .findFirst().orElse(QuestState.NO);
    }

    public static void removeQuestFor(ServerPlayer player, EntityNPCBase npc) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid())).toList();
        toRemove.forEach(p -> data.reset(p.getQuest().id));
    }

    public static void removeNPCQuestsFor(ServerPlayer player) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getQuest() instanceof NPCQuest npcQuest && !WorldHandler.get(player.getServer()).npcHandler.doesNPCExist(npcQuest.getNpcUuid())).toList();
        toRemove.forEach(p -> data.reset(p.getQuest().id));
    }

    public static ResourceLocation questForExists(ServerPlayer player, EntityNPCBase npc) {
        QuestData data = getData(player);
        return data.getCurrentQuest().stream().filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid()))
                .map(p -> ((NPCQuest) p.getQuest()).getOriginID()).findFirst().orElse(null);
    }
}
