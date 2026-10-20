package io.github.flemmli97.runecraftory.common.entities.ai.memory;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.api.datapack.npc.ConversationSet;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.entities.npc.social.TopicHolder;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.LootContext;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConversationGroupMemory {

    private final NPCEntity memoryHolder;

    private final double rangeSqr;

    private Either<NPCEntity, GroupData> data;

    public ConversationGroupMemory(NPCEntity memoryHolder, double range) {
        this(memoryHolder, range, null);
    }

    public ConversationGroupMemory(NPCEntity memoryHolder, double range, NPCEntity npc) {
        this.memoryHolder = memoryHolder;
        this.rangeSqr = range * range;
        if (npc == null) {
            this.data = Either.right(new GroupData(memoryHolder));
        } else {
            this.data = Either.left(npc);
        }
    }

    /**
     * @return All OTHER participants in this group
     */
    public Set<NPCEntity> participants() {
        Set<NPCEntity> participants = this.applyToGroup(g -> new HashSet<>(g.participants.keySet()), HashSet::new);
        participants.remove(this.memoryHolder);
        return participants;
    }

    public Map<String, Component> encodeParticipantsForDialogue() {
        Map<String, Component> participants = new HashMap<>();
        this.runForGroup(d -> d.participantsKeys.forEach((key, value) -> participants.put(key, value.getDisplayName())));
        return participants;
    }

    public boolean isValid() {
        return this.data.map(e -> e.isAlive() && this.memoryHolder.distanceToSqr(e) < this.rangeSqr && BrainUtils.hasMemory(e, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get()),
                GroupData::valid);
    }

    public boolean canJoin(NPCEntity npc) {
        return this.applyToGroup(group -> group.canJoin(npc), () -> false);
    }

    public boolean isOwner() {
        return this.data.left().isPresent();
    }

    public void join(NPCEntity npc) {
        this.data.right().ifPresent(participants -> participants.addParticipant(npc));
    }

    public void leave(NPCEntity npc) {
        this.runForGroup(group -> group.removeParticipant(npc));
    }

    public NPCEntity speaker() {
        int tick = this.memoryHolder.getServer().getTickCount();
        return this.applyToGroup(group -> {
            if (tick <= group.speakTill)
                return group.speaker;
            return null;
        }, null);
    }

    public void setSpeaker(NPCEntity speaker, int duration) {
        this.runForGroup(d -> d.setSpeaker(speaker, duration));
    }

    public TopicHolder topic() {
        return this.applyToGroup(d -> d.topicHolder, null);
    }

    public boolean isGroupComplete() {
        return this.applyToGroup(GroupData::isComplete, () -> false);
    }

    public GlobalPos creatorPosition() {
        return this.data.map(npc -> new GlobalPos(npc.level().dimension(), npc.blockPosition()),
                d -> new GlobalPos(this.memoryHolder.level().dimension(), this.memoryHolder.blockPosition()));
    }

    private <T> T applyToGroup(Function<GroupData, T> function, Supplier<T> otherwise) {
        return this.data.map(npc -> {
            ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
            if (group == null || group.data.right().isEmpty()) {
                return otherwise == null ? null : otherwise.get();
            }
            if (!npc.isAlive()) {
                this.transferOwnershipToThis(group.data.right().get());
            }
            return function.apply(group.data.right().get());
        }, function);
    }

    private void runForGroup(Consumer<GroupData> consumer) {
        if (this.data.right().isPresent()) {
            consumer.accept(this.data.right().get());
        } else {
            this.data.ifLeft(npc -> {
                ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
                if (group == null || group.data.right().isEmpty()) {
                    return;
                }
                if (!npc.isAlive()) {
                    this.transferOwnershipToThis(group.data.right().get());
                }
                consumer.accept(group.data.right().get());
            });
        }
    }

    private void transferOwnershipToThis(GroupData source) {
        this.data = Either.right(source);
        source.removeParticipant(this.memoryHolder);
        source.participants.keySet().forEach(npc -> {
            ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
            if (group != null && group.data.left().isPresent()) {
                group.data = Either.left(this.memoryHolder);
            }
        });
        source.addParticipant(this.memoryHolder, true);
    }

    static class GroupData {

        private final Map<NPCEntity, String> participants = new HashMap<>();
        private final Map<String, NPCEntity> participantsKeys = new HashMap<>();
        private NPCEntity speaker;
        private int speakTill;

        private final TopicHolder topicHolder;
        private boolean isComplete;

        private final Map<NPCEntity, String> cache = new HashMap<>();

        public GroupData(NPCEntity memoryHolder) {
            this.addParticipant(memoryHolder, true);
            this.topicHolder = memoryHolder.conversationTopics();
        }

        void setSpeaker(NPCEntity speaker, int duration) {
            this.speaker = speaker;
            this.speakTill = speaker.getServer().getTickCount() + duration;
        }

        public boolean canJoin(NPCEntity npc) {
            if (this.topicHolder == null || this.participants.containsKey(npc))
                return false;
            return this.getParticipantKey(npc) != null;
        }

        private String getParticipantKey(NPCEntity npc) {
            String participantKey = this.cache.get(npc);
            if (participantKey != null) {
                if (participantKey.isEmpty())
                    return null;
                return participantKey;
            }
            Set<String> speakers = this.topicHolder.topic().participants().get(npc.getDataID());
            if (speakers == null)
                return null;
            LootContext ctx = npc.createConversationContext(null);
            participantKey = speakers.stream().filter(k -> {
                if (this.participantsKeys.containsKey(k)) {
                    return true;
                }
                ConversationSet.Participant participant = this.topicHolder.topic().conversation().participants().get(k);
                return participant != null && participant.test(ctx);
            }).findFirst().orElse(null);
            this.cache.put(npc, participantKey == null ? "" : participantKey);
            return participantKey;
        }

        public boolean valid() {
            List<NPCEntity> invalid = new ArrayList<>();
            for (NPCEntity npc : this.participants.keySet()) {
                if (!npc.isAlive()) {
                    invalid.add(npc);
                }
            }
            invalid.forEach(this::removeParticipant);
            return this.topicHolder != null && !this.participants.isEmpty();
        }

        public boolean isComplete() {
            return this.isComplete;
        }

        public void addParticipant(NPCEntity npc) {
            this.addParticipant(npc, false);
        }

        private void addParticipant(NPCEntity npc, boolean owner) {
            if (owner) {
                this.participants.put(npc, null);
                return;
            }
            String speaker = this.getParticipantKey(npc);
            if (speaker == null) {
                return;
            }
            this.participants.put(npc, speaker);
            this.participantsKeys.put(speaker, npc);
            this.isComplete = this.topicHolder != null && this.participantsKeys.keySet().containsAll(this.topicHolder.topic().conversation().participants().keySet());
        }

        public void removeParticipant(NPCEntity npc) {
            String key = this.participants.remove(npc);
            if (key != null) {
                this.participantsKeys.remove(key);
            }
            if (npc.equals(this.speaker)) {
                this.speaker = null;
            }
        }
    }
}
