package io.github.flemmli97.runecraftory.common.quests;

import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.network.S2COpenQuestGui;
import io.github.flemmli97.runecraftory.common.quests.progress.NPCTalkTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.ShippingTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.TamingTracker;
import io.github.flemmli97.runecraftory.common.quests.tasks.LevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.SkillLevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.runecraftory.common.world.data.RunecraftorySavedData;
import io.github.flemmli97.runecraftory.mixinhelper.QuestDataGet;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.QuestState;
import io.github.flemmli97.simplequests_api.registry.PlayerQuestDataRegistry;
import io.github.flemmli97.simplequests_api.registry.ProgressionTrackerRegistry;
import io.github.flemmli97.simplequests_api.registry.QuestBaseRegistry;
import io.github.flemmli97.simplequests_api.registry.QuestEntryRegistry;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestHandler {

    public static final ResourceLocation QUEST_CATEGORY = RuneCraftory.modRes("quests");
    public static final ResourceLocation QUEST_CONTEXT = RuneCraftory.modRes("contexts");

    public static final String QUEST_BOARD_TRIGGER = RuneCraftory.MODID + "_quest_board_trigger";

    public static void register() {
        QuestEntryRegistry.registerSerializer(ShippingTask.ID, ShippingTask.CODEC, ShippingTask.SkillLevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(LevelTask.ID, LevelTask.CODEC, LevelTask.LevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(SkillLevelTask.ID, SkillLevelTask.CODEC, SkillLevelTask.SkillLevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(TamingTask.ID, TamingTask.CODEC, TamingTask.TamingTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(NPCTalkTask.ID, NPCTalkTask.CODEC, NPCTalkTask.NPCTalkResolved.CODEC);
        QuestBaseRegistry.registerSerializer(NPCQuest.ID, NPCQuest.CODEC);
        ProgressionTrackerRegistry.registerSerializer(ShippingTracker.KEY, ShippingTracker::new);
        ProgressionTrackerRegistry.registerSerializer(TamingTracker.KEY, TamingTracker::new);
        ProgressionTrackerRegistry.registerSerializer(NPCTalkTracker.KEY, NPCTalkTracker::new);
        PlayerQuestDataRegistry.registerFetcher(RuneCraftory.modRes("quest_data"), QuestHandler::getData);
    }

    public static QuestData getData(ServerPlayer player) {
        return ((QuestDataGet) player).runecraftory$getQuestData();
    }

    public static void openGui(ServerPlayer player, Vec3 at) {
        Map<ResourceLocation, QuestBase> quest = getQuestsFor(player, at);
        QuestData data = getData(player);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2COpenQuestGui(false, quest.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> {
            List<MutableComponent> description = e.getValue().getDescription(player);
            NPCEntity npc;
            if (e.getValue() instanceof NPCQuest npcQuest && (npc = npcQuest.getNpc(player.level())) != null) {
                description = Stream.concat(Stream.of(Component.translatable("runecraftory.quest.npc.header", npc.getName(), npc.blockPosition().getX(),
                                npc.blockPosition().getY(), npc.blockPosition().getZ()).withStyle(ChatFormatting.GOLD),
                        Component.empty()), description.stream()).toList();
                return new ClientSideQuestDisplay(e.getKey(), e.getValue().getName(player), description,
                        npc.lookFeatures, npc.getLook().value().playerSkin(), data.isActive(e.getKey()));
            }
            return new ClientSideQuestDisplay(e.getKey(), e.getValue().getName(player), description,
                    null, null, data.isActive(e.getKey()));
        }).toList()), player);
        data.setQuestboardQuests(quest);
    }

    public static void acceptQuestRandom(ServerPlayer player, NPCEntity npc, ResourceLocation res) {
        // TODO
    }

    public static Map<ResourceLocation, QuestBase> getQuestsFor(ServerPlayer player, Vec3 at) {
        QuestData data = getData(player);
        return Stream.concat(QuestsManager.instance().getQuestsForCategory(QUEST_CATEGORY, QUEST_CONTEXT)
                                .entrySet().stream()
                                .flatMap(e -> {
                                    if (e.getValue() instanceof NPCQuest npcQuest) {
                                        return NPCQuest.resolve(npcQuest, player, at).stream();
                                    }
                                    return Stream.of();
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

    public static QuestState checkCompletionQuest(ServerPlayer player, NPCEntity npc) {
        QuestData data = getData(player);
        return data.getCurrentQuest().stream()
                .map(p -> p.tryComplete(data, npc.getUUID().toString()))
                .filter(s -> s != QuestState.NO)
                .findFirst().orElse(QuestState.NO);
    }

    public static void removeQuestFor(ServerPlayer player, NPCEntity npc) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid())).toList();
        toRemove.forEach(p -> data.reset(p.getQuest().id));
    }

    public static void removeNPCQuestsFor(ServerPlayer player) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getQuest() instanceof NPCQuest npcQuest && !RunecraftorySavedData.get(player.getServer()).npcHandler.doesNPCExist(npcQuest.getNpcUuid())).toList();
        toRemove.forEach(p -> data.reset(p.getQuest().id));
    }

    public static ResourceLocation questForExists(ServerPlayer player, NPCEntity npc) {
        QuestData data = getData(player);
        return data.getCurrentQuest().stream().filter(p -> p.getQuest() instanceof NPCQuest npcQuest && npc.getUUID().equals(npcQuest.getNpcUuid()))
                .map(p -> ((NPCQuest) p.getQuest()).getOriginID()).findFirst().orElse(null);
    }
}
