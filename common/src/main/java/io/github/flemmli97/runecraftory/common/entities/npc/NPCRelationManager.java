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
            listTag.forEach(t -> set.add(ResourceLocation.parse(t.getAsString())));
            this.completedQuests.put(UUID.fromString(key), set);
        });
    }
}
