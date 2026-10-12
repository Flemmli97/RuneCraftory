package io.github.flemmli97.runecraftory.common.entities.npc;

import com.mojang.datafixers.util.Either;
import io.github.flemmli97.runecraftory.common.registry.RuneCraftoryMemoryTypes;
import net.minecraft.resources.ResourceLocation;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConversationGroupMemory {

    public static final int MAX_SIZE = 8;

    private final NPCEntity memoryHolder;

    private final Either<NPCEntity, GroupData> data;

    public ConversationGroupMemory(NPCEntity memoryHolder) {
        this(memoryHolder, null);
    }

    public ConversationGroupMemory(NPCEntity memoryHolder, NPCEntity npc) {
        this.memoryHolder = memoryHolder;
        if (npc == null) {
            GroupData set = new GroupData();
            set.participants.add(memoryHolder);
            this.data = Either.right(set);
        } else {
            this.data = Either.left(npc);
        }
    }

    /**
     * @return All OTHER participants in this group
     */
    public Set<NPCEntity> participants() {
        Set<NPCEntity> participants = this.applyToGroup(g -> new HashSet<>(g.participants), HashSet::new);
        participants.remove(this.memoryHolder);
        return participants;
    }

    public List<NPCEntity> sortedParticipants() {
        List<NPCEntity> participants = new ArrayList<>(this.participants());
        participants.sort(Comparator.comparingDouble(this.memoryHolder::distanceToSqr));
        return participants;
    }

    public boolean isValid(double distSqr) {
        return this.data.map(e -> e.isAlive() && e.distanceToSqr(this.memoryHolder) < distSqr, s -> !s.participants.isEmpty());
    }

    public boolean canJoin() {
        return this.applyToGroup(group -> group.participants.size() < MAX_SIZE, () -> false);
    }

    public boolean isOwner() {
        return this.data.left().isPresent();
    }

    public void join(NPCEntity npc) {
        this.data.right().ifPresent(participants -> participants.participants.add(npc));
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

    private <T> T applyToGroup(Function<GroupData, T> function, Supplier<T> otherwise) {
        return this.data.map(npc -> {
            if (!npc.isAlive()) {
                return otherwise == null ? null : otherwise.get();
            }
            ConversationGroupMemory group = BrainUtils.getMemory(npc, RuneCraftoryMemoryTypes.CONVERSATION_GROUP.get());
            if (group == null || group.data.right().isEmpty()) {
                return otherwise == null ? null : otherwise.get();
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
                consumer.accept(group.data.right().get());
            });
        }
    }

    public Map<ResourceLocation, List<NPCEntity>> mapTo() {
        List<NPCEntity> participants = this.sortedParticipants();
        Map<ResourceLocation, List<NPCEntity>> mapped = new HashMap<>();
        participants.forEach(entity -> mapped.computeIfAbsent(entity.getDataID(), k -> new ArrayList<>()).add(entity));
        return mapped;
    }

    static class GroupData {

        private final Set<NPCEntity> participants = new HashSet<>();
        private NPCEntity speaker;
        private int speakTill;

        void setSpeaker(NPCEntity speaker, int duration) {
            this.speaker = speaker;
            this.speakTill = speaker.getServer().getTickCount() + duration;
        }

        public void removeParticipant(NPCEntity npc) {
            this.participants.remove(npc);
            if (npc.equals(this.speaker)) {
                this.speaker = null;
            }
        }
    }
}
