package io.github.flemmli97.runecraftory.common.entities.npc;

import com.google.common.collect.ImmutableSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class NPCRelationManager {

    private final Map<UUID, NPCFriendPoints> playerHearts = new HashMap<>();
    private final Map<UUID, Set<ResourceLocation>> completedQuests = new HashMap<>();
    private final Map<UUID, Map<ResourceLocation, Integer>> questTracker = new HashMap<>();
    private UUIDNameMapper fatherUUID;
    private UUIDNameMapper motherUUID;
    private UUIDNameMapper[] childUUIDs;

    public boolean talkedTo(UUID uuid) {
        return this.playerHearts.containsKey(uuid);
    }

    public NPCFriendPoints getFriendPointData(UUID uuid) {
        return this.playerHearts.computeIfAbsent(uuid, key -> new NPCFriendPoints());
    }

    public void completeQuest(UUID uuid, ResourceLocation questID) {
        this.completedQuests.computeIfAbsent(uuid, key -> new HashSet<>())
                .add(questID);
    }

    public Set<ResourceLocation> getCompletedQuests(UUID uuid) {
        return ImmutableSet.copyOf(this.completedQuests.computeIfAbsent(uuid, key -> new HashSet<>()));
    }

    public int questStateFor(UUID uuid, ResourceLocation questID) {
        return this.questTracker.computeIfAbsent(uuid, key -> new HashMap<>())
                .getOrDefault(questID, -1);
    }

    public void advanceQuest(UUID uuid, ResourceLocation questID) {
        int newState = this.questStateFor(uuid, questID) + 1;
        Map<ResourceLocation, Integer> map = this.questTracker.computeIfAbsent(uuid, key -> new HashMap<>());
        map.put(questID, newState);
    }

    public void endQuest(UUID uuid, ResourceLocation questID) {
        Map<ResourceLocation, Integer> map = this.questTracker.computeIfAbsent(uuid, key -> new HashMap<>());
        map.put(questID, -2);
    }

    public void resetQuest(UUID uuid, ResourceLocation questID) {
        Map<ResourceLocation, Integer> map = this.questTracker.get(uuid);
        if (map != null) {
            map.remove(questID);
            if (map.isEmpty())
                this.questTracker.remove(uuid);
        }
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        CompoundTag heartsTag = new CompoundTag();
        this.playerHearts.forEach((uuid, hearts) -> heartsTag.put(uuid.toString(), hearts.save()));
        compound.put("PlayerHearts", heartsTag);
        CompoundTag quests = new CompoundTag();
        this.completedQuests.forEach((uuid, hearts) -> {
            ListTag list = new ListTag();
            hearts.forEach(res -> list.add(StringTag.valueOf(res.toString())));
            quests.put(uuid.toString(), list);
        });
        compound.put("CompletedQuests", quests);
        CompoundTag questsTracker = new CompoundTag();
        this.questTracker.forEach((uuid, tracker) -> {
            CompoundTag states = new CompoundTag();
            tracker.forEach((id, state) -> states.putInt(id.toString(), state));
            questsTracker.put(uuid.toString(), states);
        });
        compound.put("QuestStates", questsTracker);
        return compound;
    }

    public void load(CompoundTag compound) {
        CompoundTag heartsTag = compound.getCompound("PlayerHearts");
        heartsTag.getAllKeys().forEach(key -> {
            NPCFriendPoints points = new NPCFriendPoints();
            points.load(heartsTag.getCompound(key));
            this.playerHearts.put(UUID.fromString(key), points);
        });
        CompoundTag quests = compound.getCompound("CompletedQuests");
        quests.getAllKeys().forEach(key -> {
            ListTag listTag = quests.getList(key, Tag.TAG_STRING);
            Set<ResourceLocation> set = new HashSet<>();
            listTag.forEach(t -> set.add(new ResourceLocation(t.getAsString())));
            this.completedQuests.put(UUID.fromString(key), set);
        });
        CompoundTag questsTracker = compound.getCompound("QuestStates");
        questsTracker.getAllKeys().forEach(key -> {
            CompoundTag states = questsTracker.getCompound(key);
            Map<ResourceLocation, Integer> map = new HashMap<>();
            states.getAllKeys().forEach(id -> map.put(new ResourceLocation(id), states.getInt(id)));
            this.questTracker.put(UUID.fromString(key), map);
        });
    }
}
