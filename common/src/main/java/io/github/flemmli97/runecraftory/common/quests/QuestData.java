package io.github.flemmli97.runecraftory.common.quests;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.network.S2CSimpleToast;
import io.github.flemmli97.runecraftory.common.quests.quest.NPCQuestSetupData;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalkTask;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftorySounds;
import io.github.flemmli97.runecraftory.common.utils.EntityUtils;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.impls.progression.EntityTracker;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.InvalidQuestStateException;
import io.github.flemmli97.simplequests_api.quest.QuestDefinition;
import io.github.flemmli97.simplequests_api.quest.QuestState;
import io.github.flemmli97.simplequests_api.quest.ResolvedQuest;
import io.github.flemmli97.simplequests_api.quest.entry.ResolvedQuestTask;
import io.github.flemmli97.tenshilib.common.attachment.SerializableAttachment;
import io.github.flemmli97.tenshilib.loader.LoaderNetwork;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class QuestData implements PlayerQuestData, SerializableAttachment<CompoundTag, QuestData> {

    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ServerPlayer player;

    private final List<QuestProgress> currentQuests = new ArrayList<>();
    private final Map<ResourceLocation, ResourceLocation> questStates = new HashMap<>();
    private final List<QuestProgress> tickables = new ArrayList<>();

    private Map<ResourceLocation, QuestHandler.QuestboardQuestHolder> questBoardContent;
    private QuestHandler.QuestboardQuestHolder selectedContent;
    private final Set<ResourceLocation> unlockTracker = new HashSet<>();

    private LocalDateTime questTrackerTime = LocalDateTime.now();
    private long dailySeed;
    private int finishedQuestDay;

    private final Map<ResourceLocation, Integer> finishedQuestsTracker = new HashMap<>();

    private int interactionCooldown;

    public QuestData(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public ServerPlayer getPlayer() {
        return this.player;
    }

    @Override
    public List<QuestProgress> getCurrentQuest() {
        return this.currentQuests;
    }

    public QuestProgress getCurrentFor(ResourceLocation quest) {
        return this.currentQuests.stream().filter(p -> p.getRootId().equals(quest)).findFirst().orElse(null);
    }

    public boolean getAndUpdateActiveProgress(QuestProgress progress) {
        ResourceLocation current = this.questStates.get(progress.getRootId());
        ResolvedQuest quest = progress.getActiveQuest();
        if (quest == null)
            return false;
        this.questStates.put(progress.getRootId(), quest.id());
        return current != null && current.equals(quest.id());
    }

    @Override
    public long getRandomSeed(@Nullable ResourceLocation quest) {
        return this.dailySeed;
    }

    public boolean acceptQuest(ResourceLocation id) {
        QuestHandler.QuestboardQuestHolder quest = this.questBoardContent == null ? null : this.questBoardContent.get(id);
        if (quest == null) {
            return false;
        }
        return this.acceptQuest(quest);
    }

    public boolean acceptQuest(QuestHandler.QuestboardQuestHolder quest) {
        AcceptType type = this.canAcceptQuest(quest, false);
        if (type != AcceptType.ACCEPT) {
            this.player.displayClientMessage(Component.translatable(type.langKey()).withStyle(ChatFormatting.DARK_RED), false);
            return false;
        }
        this.selectedContent = quest;
        this.currentQuests.add(new QuestProgress(quest.definition(), this, 0));
        this.selectedContent = null;
        EntityUtils.playSoundForPlayer(this.player, RuneCraftorySounds.GENERIC_SUCCESS.get(), 1, 1.2f);
        return true;
    }

    public AcceptType canAcceptQuest(QuestHandler.QuestboardQuestHolder holder, boolean ignoreMiss) {
        QuestDefinition quest = holder.definition();
        if (!ignoreMiss && (this.questBoardContent == null || !this.questBoardContent.containsKey(quest.id())))
            return AcceptType.MISSING;
        if (holder.data().map(d -> !d.isValid(this.player.getServer())).orElse(false)) {
            return AcceptType.NONPC;
        }
        if (!quest.isUnlocked(this.player)
                || (!quest.requiredParentQuests().isEmpty() && !this.unlockTracker.containsAll(quest.requiredParentQuests()))) {
            return AcceptType.REQUIREMENTS;
        }
        ResourceLocation id = holder.data().map(NPCQuestSetupData::questId).orElse(quest.id());
        if (this.finishedQuestDay > 3 || (quest.repeatDelay().time() < 0 && this.getTimesCompleted(id) > 0)) {
            return AcceptType.LIMIT;
        }
        return AcceptType.ACCEPT;
    }

    public Map<ResourceLocation, QuestState> submit(@Nullable NPCEntity npc) {
        if (this.currentQuests.isEmpty()) {
            return Map.of();
        }
        Map<ResourceLocation, QuestState> completion = new HashMap<>();
        List<QuestProgress> completed = new ArrayList<>();
        String trigger = npc == null ? QuestHandler.QUEST_BOARD_TRIGGER : npc.getUUID().toString();
        for (QuestProgress prog : this.currentQuests) {
            Set<Pair<String, ResolvedQuestTask>> fulfilled = prog.submit(trigger);
            QuestState state = prog.tryComplete(trigger);
            switch (state) {
                case COMPLETE -> {
                    this.completeQuest(prog);
                    completed.add(prog);
                    completion.put(prog.getRootId(), state);
                }
                case PARTIAL_COMPLETE -> completion.put(prog.getRootId(), state);
                case SELECT -> {
                    // Select not supported. At least for now
                    completed.add(prog);
                }
                case NO -> {
                    if (!fulfilled.isEmpty()) {
                        EntityUtils.playSoundForPlayer(this.player, RuneCraftorySounds.GENERIC_SUCCESS.get(), this.player.getSoundSource(), 2 * 0.75f, 1.0f);
                        fulfilled.forEach(t -> LoaderNetwork.INSTANCE.sendToPlayer(new S2CSimpleToast(prog.name(this.player).withStyle(ChatFormatting.DARK_PURPLE),
                                t.getSecond().translation(this.player).withStyle(ChatFormatting.GOLD)), this.player));
                        completion.put(prog.getRootId(), state);
                    }
                }
            }
        }
        if (completion.isEmpty()) {
            EntityUtils.playSoundForPlayer(this.player, RuneCraftorySounds.GENERIC_DENY.get(), this.player.getSoundSource(), 2 * 0.75f, 1.0f);
        }
        completed.forEach(this::removeProgress);
        return completion;
    }

    @Override
    public void addTickableProgress(QuestProgress progress) {
        if (!this.tickables.contains(progress))
            this.tickables.add(progress);
    }

    @Override
    public void removeTickableQuestProgress(QuestProgress progress) {
        this.tickables.remove(progress);
    }

    @Override
    public int getTimesCompleted(ResourceLocation quest) {
        return this.finishedQuestsTracker.getOrDefault(quest, 0);
    }

    public <V, T extends ResolvedQuestTask> List<ProgressResult<T>> trigger(ProgressionTrackerKey<V, T> key, V with) {
        return this.trigger(key, with, "");
    }

    @Override
    public <V, T extends ResolvedQuestTask> List<ProgressResult<T>> trigger(ProgressionTrackerKey<V, T> key, V with, @NotNull String trigger) {
        return this.trigger(key, with, trigger, -1);
    }

    public <V, T extends ResolvedQuestTask> List<ProgressResult<T>> trigger(ProgressionTrackerKey<V, T> key, V with, @NotNull String trigger, int max) {
        if (key.equals(EntityTracker.KEY)) {
            if (this.interactionCooldown > 0)
                return List.of();
            this.interactionCooldown = 2;
        }
        List<QuestProgress> completed = new ArrayList<>();
        List<ProgressResult<T>> completion = new ArrayList<>();
        for (QuestProgress prog : this.currentQuests) {
            Set<Pair<String, T>> fulfilled = prog.tryFulfill(this.player, key, with);
            QuestState state = prog.tryComplete(trigger);
            switch (state) {
                case COMPLETE -> {
                    this.completeQuest(prog);
                    completed.add(prog);
                    completion.add(new ProgressResult<>(prog, fulfilled, state));
                }
                case PARTIAL_COMPLETE -> completion.add(new ProgressResult<>(prog, fulfilled, state));
                case SELECT -> {
                    // Select not supported. At least for now
                    completed.add(prog);
                }
                case NO -> {
                    if (!fulfilled.isEmpty()) {
                        fulfilled.forEach(p -> {
                            if (!(p.getSecond() instanceof NPCTalkTask.NPCTalkResolved)) {
                                LoaderNetwork.INSTANCE.sendToPlayer(new S2CSimpleToast(prog.name(this.player).withStyle(ChatFormatting.DARK_PURPLE),
                                        p.getSecond().translation(this.player).withStyle(ChatFormatting.GOLD)), this.player);
                            }
                        });
                        completion.add(new ProgressResult<>(prog, fulfilled, state));
                    }
                }
            }
            if (max != -1 && completion.size() >= max) {
                break;
            }
        }
        completed.forEach(this::removeProgress);
        return completion;
    }

    public void tryCompleteQuest(QuestProgress prog, String trigger) {
        if (!this.currentQuests.contains(prog)) {
            return;
        }
        QuestState state = prog.tryComplete(trigger);
        if (state == QuestState.COMPLETE) {
            this.completeQuest(prog);
            this.removeProgress(prog);
        }
    }

    private void completeQuest(QuestProgress prog) {
        prog.getRootQuest().onComplete(this.player);
        prog.getCompletionID().forEach(id -> {
            this.unlockTracker.add(id);
            this.finishedQuestsTracker.compute(id, (key, i) -> i == null ? 1 : ++i);
        });
        this.finishedQuestDay++;
        this.player.level().playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_LEVELUP, this.player.getSoundSource(), 2 * 0.75f, 1.0f);
        if (!prog.getRootDefinition().requiredParentQuests().isEmpty() && prog.getRootDefinition().redoParent()) {
            prog.getRootDefinition().requiredParentQuests().forEach(res -> {
                QuestDefinition quest = QuestsManager.instance().getQuest(res, null);
                if (quest != null)
                    this.unlockTracker.remove(quest.id());
            });
        }
    }

    public void tickTickableQuests() {
        this.tickables.removeIf(prog -> {
            Pair<Boolean, Set<ResolvedQuestTask>> fulfilled = prog.tickProgress();
            if (!fulfilled.getSecond().isEmpty()) {
                fulfilled.getSecond().forEach(p -> LoaderNetwork.INSTANCE.sendToPlayer(new S2CSimpleToast(prog.name(this.player).withStyle(ChatFormatting.DARK_PURPLE),
                        p.translation(this.player).withStyle(ChatFormatting.GOLD)), this.player));
            }
            return fulfilled.getFirst();
        });
    }

    public void tick() {
        --this.interactionCooldown;
        this.tickTickableQuests();

        LocalDateTime now = LocalDateTime.now();
        if (this.questTrackerTime == null || this.questTrackerTime.getDayOfYear() != now.getDayOfYear()) {
            this.dailySeed = this.player.getRandom().nextLong();
            this.questTrackerTime = now;
            this.finishedQuestDay = 0;
        }
    }

    public void reset(ResourceLocation res) {
        if (this.currentQuests.isEmpty()) {
            return;
        }
        QuestProgress prog = null;
        for (QuestProgress p : this.currentQuests) {
            if (p.getRootId().equals(res)) {
                prog = p;
                break;
            }
        }
        if (prog == null) {
            return;
        }
        this.removeProgress(prog);
        this.removeTickableQuestProgress(prog);
        prog.getRootQuest().onReset(this.player);
    }

    private void removeProgress(QuestProgress prog) {
        this.currentQuests.remove(prog);
        this.questStates.remove(prog.getRootId());
    }

    public QuestData clone(QuestData other) {
        this.currentQuests.clear();
        this.currentQuests.addAll(other.currentQuests);
        this.questStates.clear();
        this.tickables.clear();
        this.tickables.addAll(other.tickables);

        this.unlockTracker.clear();
        this.unlockTracker.addAll(other.unlockTracker);

        this.questTrackerTime = other.questTrackerTime;
        this.dailySeed = other.dailySeed;
        this.finishedQuestDay = other.finishedQuestDay;

        this.finishedQuestsTracker.clear();
        this.finishedQuestsTracker.putAll(other.finishedQuestsTracker);
        return this;
    }

    public void setQuestboardQuests(Map<ResourceLocation, QuestHandler.QuestboardQuestHolder> quest) {
        this.questBoardContent = quest;
    }

    public NPCQuestSetupData getSetupDataFor(QuestDefinition definition) {
        if (this.selectedContent == null || this.selectedContent.data().isEmpty() || definition != this.selectedContent.definition()) {
            return null;
        }
        return this.selectedContent.data().get();
    }

    public boolean isActive(ResourceLocation key) {
        return this.currentQuests.stream().anyMatch(p -> p.getRootId().equals(key));
    }

    public void resetAll() {
        this.currentQuests.forEach(p -> p.getRootQuest().onReset(this.player));
        this.currentQuests.clear();
        this.tickables.clear();
        this.questBoardContent = null;
        this.unlockTracker.clear();
        this.finishedQuestsTracker.clear();
        this.finishedQuestDay = 0;
    }

    @Override
    public QuestData read(CompoundTag tag, HolderLookup.Provider provider) {
        ListTag quests = tag.getList("ActiveQuests", Tag.TAG_COMPOUND);
        quests.forEach(q -> {
            try {
                QuestProgress prog = new QuestProgress((CompoundTag) q, this);
                if (prog.valid())
                    this.currentQuests.add(prog);
            } catch (InvalidQuestStateException e) {
                RuneCraftory.LOGGER.error(e);
            }
        });
        ListTag unlocked = tag.getList("UnlockedQuests", Tag.TAG_STRING);
        unlocked.forEach(t -> this.unlockTracker.add(ResourceLocation.parse(t.getAsString())));
        if (tag.contains("TimeTracker"))
            this.questTrackerTime = LocalDateTime.parse(tag.getString("TimeTracker"), TIME);
        this.dailySeed = tag.getLong("DailySeed");
        this.finishedQuestDay = tag.getInt("DailyDone");
        CompoundTag total = tag.getCompound("FinishedQuestTracker");
        total.getAllKeys().forEach(key -> this.finishedQuestsTracker.put(ResourceLocation.parse(key), total.getInt(key)));
        return this;
    }

    @Override
    public CompoundTag write(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        ListTag quests = new ListTag();
        this.currentQuests.forEach(prog -> quests.add(prog.save(provider)));
        tag.put("ActiveQuests", quests);
        ListTag unlocked = new ListTag();
        this.unlockTracker.forEach(res -> unlocked.add(StringTag.valueOf(res.toString())));
        tag.put("UnlockedQuests", unlocked);
        if (this.questTrackerTime != null)
            tag.putString("TimeTracker", this.questTrackerTime.format(TIME));
        tag.putLong("DailySeed", this.dailySeed);
        tag.putInt("DailyDone", this.finishedQuestDay);
        CompoundTag total = new CompoundTag();
        this.finishedQuestsTracker.forEach((res, amount) -> total.putInt(res.toString(), amount));
        tag.put("FinishedQuestTracker", total);
        return tag;
    }

    public enum AcceptType {

        MISSING("runecraftory.quests.accept_type.missing"),
        REQUIREMENTS("runecraftory.quests.accept_type.requirements"),
        ACCEPT("runecraftory.quests.accept_type.accept"),
        LIMIT("runecraftory.quests.accept_type.limit"),
        NONPC("runecraftory.quest.npc.none");

        final String lang;

        AcceptType(String id) {
            this.lang = id;
        }

        public String langKey() {
            return this.lang;
        }
    }
}
