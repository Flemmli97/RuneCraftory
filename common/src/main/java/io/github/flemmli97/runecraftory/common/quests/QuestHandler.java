package io.github.flemmli97.runecraftory.common.quests;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.network.S2COpenQuestGui;
import io.github.flemmli97.runecraftory.common.quests.progress.DeliveryTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.NPCTalkTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.ShippingTracker;
import io.github.flemmli97.runecraftory.common.quests.progress.TamingTracker;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestDefinition;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestSetupData;
import io.github.flemmli97.runecraftory.common.quests.tasks.DeliveryTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.LevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.ShippingTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.SkillLevelTask;
import io.github.flemmli97.runecraftory.common.quests.tasks.TamingTask;
import io.github.flemmli97.runecraftory.common.registry.RunecraftoryAttachments;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestDefinition;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.registry.PlayerQuestDataRegistry;
import io.github.flemmli97.simplequests_api.registry.ProgressionTrackerRegistry;
import io.github.flemmli97.simplequests_api.registry.QuestDefinitionRegistry;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestHandler {

    public static final ResourceLocation QUEST_CATEGORY = RuneCraftory.modRes("quests");
    public static final ResourceLocation QUEST_CONTEXT = RuneCraftory.modRes("contexts");

    public static final String QUEST_BOARD_TRIGGER = RuneCraftory.MODID + "_quest_board_trigger";

    public static void register() {
        QuestEntryRegistry.registerSerializer(DeliveryTask.ID, DeliveryTask.CODEC, DeliveryTask.DeliveryTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(LevelTask.ID, LevelTask.CODEC, LevelTask.LevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(NPCTalkTask.ID, NPCTalkTask.CODEC, NPCTalkTask.NPCTalkResolved.CODEC);
        QuestEntryRegistry.registerSerializer(ShippingTask.ID, ShippingTask.CODEC, ShippingTask.SkillLevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(SkillLevelTask.ID, SkillLevelTask.CODEC, SkillLevelTask.SkillLevelTaskResolved.CODEC);
        QuestEntryRegistry.registerSerializer(TamingTask.ID, TamingTask.CODEC, TamingTask.TamingTaskResolved.CODEC);
        QuestDefinitionRegistry.register(NPCQuestDefinition.ID, NPCQuestDefinition.CODEC);
        ProgressionTrackerRegistry.registerSerializer(DeliveryTracker.KEY, DeliveryTracker::new);
        ProgressionTrackerRegistry.registerSerializer(NPCTalkTracker.KEY, NPCTalkTracker::new);
        ProgressionTrackerRegistry.registerSerializer(ShippingTracker.KEY, ShippingTracker::new);
        ProgressionTrackerRegistry.registerSerializer(TamingTracker.KEY, TamingTracker::new);
        PlayerQuestDataRegistry.registerFetcher(RuneCraftory.modRes("quest_data"), QuestHandler::getData);
    }

    public static QuestData getData(ServerPlayer player) {
        return RunecraftoryAttachments.PLAYER_QUEST_DATA.get().get(player);
    }

    public static void openGui(ServerPlayer player, Vec3 at) {
        Map<ResourceLocation, Either<QuestboardQuestHolder, ActiveQuestRef>> quests = getQuestsFor(player, at);
        QuestData data = getData(player);
        LoaderNetwork.INSTANCE.sendToPlayer(new S2COpenQuestGui(false, quests.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> {
            List<MutableComponent> description = e.getValue().map(h -> h.definition().description(player),
                    q -> q.quest.description(player));
            NPCEntity npc = e.getValue().map(h -> h.data().map(NPCQuestSetupData::questGiver), ActiveQuestRef::npc).orElse(null);
            MutableComponent name = e.getValue().map(h -> h.definition.name(player), q -> q.quest.name(player));
            ResourceLocation realId = e.getValue().map(QuestboardQuestHolder::definitionId, a -> a.quest().id());
            if (npc != null) {
                description = Stream.concat(Stream.of(Component.translatable("runecraftory.quest.npc.header", npc.getName(), npc.blockPosition().getX(),
                                npc.blockPosition().getY(), npc.blockPosition().getZ()).withStyle(ChatFormatting.GOLD),
                        Component.empty()), description.stream()).toList();
                return new ClientSideQuestDisplay(realId, name, description,
                        npc.lookFeatures, npc.getLook().value().playerSkin(), data.isActive(realId));
            }
            return new ClientSideQuestDisplay(realId, name, description,
                    null, null, data.isActive(realId));
        }).toList()), player);
        data.setQuestboardQuests(quests.entrySet().stream()
                .filter(entry -> entry.getValue().left().isPresent()).collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().left().get(),
                        (e1, e2) -> e1,
                        HashMap::new
                )));
    }

    public static void acceptQuestRandom(ServerPlayer player, NPCEntity npc, ResourceLocation res) {
        // TODO
    }

    public static Map<ResourceLocation, Either<QuestboardQuestHolder, ActiveQuestRef>> getQuestsFor(ServerPlayer player, Vec3 at) {
        QuestData data = getData(player);
        return Stream.<Either<QuestboardQuestHolder, ActiveQuestRef>>concat(QuestsManager.instance().getQuestsForCategory(QUEST_CATEGORY, QUEST_CONTEXT)
                                .entrySet().stream()
                                .flatMap(e -> {
                                    if (e.getValue() instanceof NPCQuestDefinition npcQuestDefinition) {
                                        return NPCQuestDefinition.resolve(npcQuestDefinition, player, at).stream().limit(1).map(QuestboardQuestHolder::new);
                                    }
                                    return Stream.of(new QuestboardQuestHolder(e.getValue()));
                                })
                                .filter(q -> data.canAcceptQuest(q, true) == QuestData.AcceptType.ACCEPT)
                                .map(Either::left),
                        data.getCurrentQuest().stream().map(QuestProgress::getRootQuest).filter(quest -> quest.source().category().id.equals(QUEST_CATEGORY))
                                .map(q -> Either.right(new ActiveQuestRef(q, q instanceof NPCQuestDefinition.NPCQuest quest ? quest.getNpc(player.level()) : null))))
                .collect(Collectors.toMap(
                        q -> q.map(QuestboardQuestHolder::definitionId, ActiveQuestRef::definitionId),
                        Function.identity(),
                        (e1, e2) -> e1.right().isPresent() ? e1 : e2,
                        HashMap::new
                ));
    }

    public static void removeQuestFor(ServerPlayer player, NPCEntity npc) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getRootQuest() instanceof NPCQuestDefinition.NPCQuest quest
                        && (npc.getUUID().equals(quest.getQuestGiverUUID()) || quest.relevantNpcIds().contains(npc.getUUID()))).toList();
        toRemove.forEach(p -> data.reset(p.getRootId()));
    }

    public static void removeInvalidQuests(ServerPlayer player) {
        QuestData data = getData(player);
        List<QuestProgress> toRemove = data.getCurrentQuest().stream()
                .filter(p -> p.getRootQuest() instanceof NPCQuestDefinition.NPCQuest quest && !quest.isValid(player.getServer())).toList();
        toRemove.forEach(p -> data.reset(p.getRootId()));
    }

    public static QuestProgress getFirstQuestFor(ServerPlayer player, NPCEntity npc) {
        QuestData data = getData(player);
        return data.getCurrentQuest().stream().filter(p -> p.getRootQuest() instanceof NPCQuestDefinition.NPCQuest quest && npc.getUUID().equals(quest.getQuestGiverUUID())).findFirst().orElse(null);
    }

    public record QuestboardQuestHolder(QuestDefinition definition,
                                        Optional<NPCQuestSetupData> data) {

        public QuestboardQuestHolder(QuestDefinition definition) {
            this(definition, Optional.empty());
        }

        public QuestboardQuestHolder(NPCQuestSetupData data) {
            this(data.quest(), Optional.of(data));
        }

        public ResourceLocation definitionId() {
            return this.definition().id();
        }
    }

    public record ActiveQuestRef(ResolvedQuest quest, Optional<NPCEntity> npc) {

        public ActiveQuestRef(ResolvedQuest quest, NPCEntity npc) {
            this(quest, Optional.ofNullable(npc));
        }

        public ResourceLocation definitionId() {
            return this.quest().source().id();
        }
    }
}
