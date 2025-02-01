package io.github.flemmli97.runecraftory.common.quests;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.common.network.S2CSimpleToast;
import io.github.flemmli97.runecraftory.common.quests.tasks.NPCTalk;
import io.github.flemmli97.runecraftory.common.world.WorldHandler;
import io.github.flemmli97.runecraftory.platform.Platform;
import io.github.flemmli97.simplequests_api.datapack.QuestsManager;
import io.github.flemmli97.simplequests_api.impls.progression.EntityTracker;
import io.github.flemmli97.simplequests_api.impls.quests.Quest;
import io.github.flemmli97.simplequests_api.player.PlayerQuestData;
import io.github.flemmli97.simplequests_api.player.ProgressionTrackerKey;
import io.github.flemmli97.simplequests_api.player.QuestProgress;
import io.github.flemmli97.simplequests_api.quest.QuestBase;
import io.github.flemmli97.simplequests_api.quest.QuestState;
import io.github.flemmli97.simplequests_api.quest.entry.QuestEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
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
import java.util.Random;
import java.util.Set;

public class QuestData implements PlayerQuestData {

    public static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ServerPlayer player;

    private final List<QuestProgress> currentQuests = new ArrayList<>();
    private final List<QuestProgress> tickables = new ArrayList<>();

    private Map<ResourceLocation, QuestBase> questBoardContent;
    private final Set<ResourceLocation> unlockTracker = new HashSet<>();

    private LocalDateTime questTrackerTime = LocalDateTime.now();
    private long dailySeed;
    private final Random questRandom = new Random();
    private int finishedQuestDay;

    private final Map<ResourceLocation, Integer> finishedQuestsTracker = new HashMap<>();

    private int interactionCooldown;

    public QuestData(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public ServerPlayer getPlayer() {
        return null;
    }

    @Override
    public List<QuestProgress> getCurrentQuest() {
        return this.currentQuests;
    }

    public boolean acceptQuest(ResourceLocation id) {
        QuestBase quest = this.questBoardContent == null ? null : this.questBoardContent.get(id);
        return this.acceptQuest(quest);
    }

    public boolean acceptQuest(QuestBase quest) {
        AcceptType type = this.canAcceptQuest(quest, false);
        if (type != AcceptType.ACCEPT) {
            this.player.sendMessage(new TranslatableComponent(type.langKey()).withStyle(ChatFormatting.DARK_RED), Util.NIL_UUID);
            return false;
        }
        this.currentQuests.add(new QuestProgress(quest, this, 0));
        this.player.connection.send(new ClientboundSoundPacket(SoundEvents.VILLAGER_YES, this.player.getSoundSource(), this.player.getX(), this.player.getY(), this.player.getZ(), 1, 1.2f));
        return true;
    }

    public AcceptType canAcceptQuest(QuestBase quest, boolean ignoreMiss) {
        if (!ignoreMiss && (this.questBoardContent == null || !this.questBoardContent.containsKey(quest.id)))
            return AcceptType.MISSING;
        if (quest instanceof NPCQuest npcQuest && !WorldHandler.get(this.player.getServer()).npcHandler.doesNPCExist(npcQuest.getNpcUuid())) {
            return AcceptType.NONPC;
        }
        if (!quest.isUnlocked(this.player)
                || (!quest.neededParentQuests.isEmpty() && !this.unlockTracker.containsAll(quest.neededParentQuests))) {
            return AcceptType.REQUIREMENTS;
        }
        if (this.finishedQuestDay > 3) {
            return AcceptType.LIMIT;
        }
        return AcceptType.ACCEPT;
    }

    public Map<ResourceLocation, QuestState> submit(@Nullable EntityNPCBase npc) {
        if (this.currentQuests.isEmpty()) {
            return Map.of();
        }
        Map<ResourceLocation, QuestState> completion = new HashMap<>();
        List<QuestProgress> completed = new ArrayList<>();
        for (QuestProgress prog : this.currentQuests) {
            switch (prog.submit(this, npc == null ? QuestHandler.QUEST_BOARD_TRIGGER : npc.getUUID().toString())) {
                case COMPLETE -> {
                    this.completeQuest(prog);
                    completed.add(prog);
                    completion.put(prog.getQuest().id, QuestState.COMPLETE);
                }
                case PARTIAL_COMPLETE -> completion.put(prog.getQuest().id, QuestState.PARTIAL_COMPLETE);
                case PARTIAL ->
                        this.player.level.playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.VILLAGER_YES, this.player.getSoundSource(), 2 * 0.75f, 1.0f);
                case NOTHING ->
                        this.player.level.playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.VILLAGER_NO, this.player.getSoundSource(), 2 * 0.75f, 1.0f);
            }
        }
        this.currentQuests.removeAll(completed);
        return completion;
    }

    @Override
    public Random getRandom(@Nullable ResourceLocation quest) {
        this.questRandom.setSeed(this.dailySeed);
        return this.questRandom;
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

    public <V, T extends QuestEntry> Map<ResourceLocation, QuestState> trigger(ProgressionTrackerKey<V, T> key, V with) {
        return this.trigger(key, with, "");
    }

    @Override
    public <V, T extends QuestEntry> Map<ResourceLocation, QuestState> trigger(ProgressionTrackerKey<V, T> key, V with, @NotNull String trigger) {
        if (key.equals(EntityTracker.KEY)) {
            if (this.interactionCooldown > 0)
                return Map.of();
            this.interactionCooldown = 2;
        }
        List<QuestProgress> completed = new ArrayList<>();
        Map<ResourceLocation, QuestState> completion = new HashMap<>();
        this.currentQuests.forEach(prog -> {
            Set<Pair<String, T>> fulfilled = prog.tryFullFill(this.player, key, with);
            if (!fulfilled.isEmpty()) {
                fulfilled.forEach(p -> {
                    if (!(p.getSecond() instanceof NPCTalk)) {
                        Platform.INSTANCE.sendToClient(new S2CSimpleToast(prog.getTask(this.player).withStyle(ChatFormatting.DARK_PURPLE),
                                p.getSecond().translation(this.player).withStyle(ChatFormatting.GOLD)), this.player);
                    }
                });
            }
            QuestState state = prog.tryComplete(this, "");
            if (state == QuestState.COMPLETE) {
                this.completeQuest(prog);
                completed.add(prog);
                completion.put(prog.getQuest().id, QuestState.COMPLETE);
            } else if (state == QuestState.PARTIAL_COMPLETE) {
                completion.put(prog.getQuest().id, QuestState.PARTIAL_COMPLETE);
            }
        });
        this.currentQuests.removeAll(completed);
        return completion;
    }

    private void completeQuest(QuestProgress prog) {
        prog.getQuest().onComplete(this.player);
        prog.getCompletionID().forEach(id -> {
            this.unlockTracker.add(id);
            this.finishedQuestsTracker.compute(id, (key, i) -> i == null ? 1 : ++i);
        });
        this.finishedQuestDay++;
        this.player.level.playSound(null, this.player.getX(), this.player.getY(), this.player.getZ(), SoundEvents.PLAYER_LEVELUP, this.player.getSoundSource(), 2 * 0.75f, 1.0f);
        if (!prog.getQuest().neededParentQuests.isEmpty() && prog.getQuest().redoParent) {
            prog.getQuest().neededParentQuests.forEach(res -> {
                Quest quest = QuestsManager.instance().getActualQuest(res, null);
                if (quest != null)
                    this.unlockTracker.remove(quest.id);
            });
        }
    }

    public void tickTickableQuests() {
        this.tickables.removeIf(prog -> {
            Pair<Boolean, Set<QuestEntry>> fulfilled = prog.tickProgress(this);
            if (!fulfilled.getSecond().isEmpty()) {
                fulfilled.getSecond().forEach(p -> Platform.INSTANCE.sendToClient(new S2CSimpleToast(prog.getTask(this.player).withStyle(ChatFormatting.DARK_PURPLE),
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
            if (p.getQuest().id.equals(res)) {
                prog = p;
                break;
            }
        }
        if (prog == null) {
            return;
        }
        this.currentQuests.remove(prog);
        this.removeTickableQuestProgress(prog);
        prog.getQuest().onReset(this.player);
    }

    public void clone(QuestData other) {
        this.currentQuests.clear();
        this.currentQuests.addAll(other.currentQuests);
        this.tickables.clear();
        this.tickables.addAll(other.tickables);

        this.unlockTracker.clear();
        this.unlockTracker.addAll(other.unlockTracker);

        this.questTrackerTime = other.questTrackerTime;
        this.dailySeed = other.dailySeed;
        this.finishedQuestDay = other.finishedQuestDay;

        this.finishedQuestsTracker.clear();
        this.finishedQuestsTracker.putAll(other.finishedQuestsTracker);
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag quests = new ListTag();
        this.currentQuests.forEach(prog -> quests.add(prog.save()));
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

    public void load(CompoundTag tag) {
        if (tag.contains("ActiveQuests")) {
            ListTag quests = tag.getList("ActiveQuests", Tag.TAG_COMPOUND);
            quests.forEach(q -> {
                try {
                    QuestProgress prog = new QuestProgress((CompoundTag) q, this);
                    if (prog.getQuest() != null)
                        this.currentQuests.add(prog);
                } catch (IllegalStateException ignored) {
                }
            });
        }
        ListTag unlocked = tag.getList("UnlockedQuests", Tag.TAG_STRING);
        unlocked.forEach(t -> this.unlockTracker.add(new ResourceLocation(t.getAsString())));
        if (tag.contains("TimeTracker"))
            this.questTrackerTime = LocalDateTime.parse(tag.getString("TimeTracker"), TIME);
        this.dailySeed = tag.getLong("DailySeed");
        this.finishedQuestDay = tag.getInt("DailyDone");
        CompoundTag total = tag.getCompound("FinishedQuestTracker");
        total.getAllKeys().forEach(key -> this.finishedQuestsTracker.put(new ResourceLocation(key), total.getInt(key)));
    }

    public void setQuestboardQuests(Map<ResourceLocation, QuestBase> quest) {
        this.questBoardContent = quest;
    }

    public boolean isActive(ResourceLocation key) {
        return this.currentQuests.stream().anyMatch(p -> p.getQuest().id.equals(key));
    }

    public void resetAll() {
        this.currentQuests.forEach(p -> p.getQuest().onReset(this.player));
        this.currentQuests.clear();
        this.tickables.clear();
        this.questBoardContent = null;
        this.unlockTracker.clear();
        this.finishedQuestsTracker.clear();
        this.finishedQuestDay = 0;
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
