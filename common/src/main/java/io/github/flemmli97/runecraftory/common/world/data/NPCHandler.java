package io.github.flemmli97.runecraftory.common.world.data;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import io.github.flemmli97.runecraftory.api.datapack.npc.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.ReloadableHolder;
import io.github.flemmli97.runecraftory.common.entities.npc.NPCEntity;
import io.github.flemmli97.runecraftory.common.quests.QuestHandler;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCHandler {

    private final Map<UUID, Component> npcs = new HashMap<>();
    private final Map<ResourceLocation, Set<UUID>> uniqueNPCS = new HashMap<>();
    private final Map<UUID, Set<Pair<UUID, ResourceLocation>>> resetQuestNPCS = new HashMap<>();

    public boolean doesNPCExist(UUID uuid) {
        return this.npcs.containsKey(uuid);
    }

    public void addNPC(NPCEntity npc) {
        this.npcs.put(npc.getUUID(), npc.getName());
    }

    public Component getName(UUID uuid) {
        return this.npcs.get(uuid);
    }

    public void removeNPC(NPCEntity npc, Entity.RemovalReason reason) {
        if (reason.shouldDestroy()) {
            npc.getServer().getPlayerList().getPlayers().forEach(p -> QuestHandler.removeQuestFor(p, npc));
            npc.getFamily().markAsDead();
            this.npcs.remove(npc.getUUID());
        } else {
            this.npcs.put(npc.getUUID(), npc.getName());
        }
    }

    public boolean canAssignNPC(ReloadableHolder<NPCData> data) {
        if (data.value().unique() == 0)
            return true;
        Set<UUID> uuids = this.uniqueNPCS.get(data.id());
        return uuids == null || uuids.size() < data.value().unique();
    }

    public boolean addUniqueNPC(UUID uuid, ReloadableHolder<NPCData> data) {
        if (data.value().unique() == 0)
            return false;
        return this.uniqueNPCS.computeIfAbsent(data.id(), key -> new HashSet<>()).add(uuid);
    }

    public boolean removeUniqueNPC(UUID uuid, ReloadableHolder<NPCData> data) {
        if (data.value().unique() == 0)
            return false;
        return this.uniqueNPCS.computeIfAbsent(data.id(), key -> new HashSet<>()).remove(uuid);
    }

    /**
     * Add player quest process to reset for the given unloaded npc
     */
    public void scheduleQuestTrackerReset(UUID npc, UUID player, ResourceLocation quest) {
        this.resetQuestNPCS.computeIfAbsent(npc, key -> new HashSet<>()).add(Pair.of(player, quest));
    }

    /**
     * Get the players to reset the quest process for
     */
    public Set<Pair<UUID, ResourceLocation>> playersToReset(UUID npc) {
        return this.resetQuestNPCS.getOrDefault(npc, new HashSet<>());
    }

    public CompoundTag save(HolderLookup.Provider provider) {
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        CompoundTag tag = new CompoundTag();
        CompoundTag npcs = new CompoundTag();
        this.npcs.forEach((uuid, comp) -> npcs.put(uuid.toString(), ComponentSerialization.CODEC.encodeStart(ops, comp).getOrThrow()));
        tag.put("NPCs", npcs);
        CompoundTag uniques = new CompoundTag();
        this.uniqueNPCS.forEach((res, ids) -> {
            ListTag idTag = new ListTag();
            ids.forEach(uuid -> idTag.add(StringTag.valueOf(uuid.toString())));
            uniques.put(res.toString(), idTag);
        });
        tag.put("UniqueNPCs", uniques);
        CompoundTag resetQuestTracker = new CompoundTag();
        this.resetQuestNPCS.forEach((uuid, ids) -> {
            CompoundTag pairs = new CompoundTag();
            ids.forEach(pair -> pairs.putString(pair.getFirst().toString(), pair.getSecond().toString()));
            resetQuestTracker.put(uuid.toString(), pairs);
        });
        tag.put("ResetQuestNPCs", resetQuestTracker);
        return tag;
    }

    public void load(CompoundTag tag, HolderLookup.Provider provider) {
        DynamicOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
        CompoundTag npcs = tag.getCompound("NPCs");
        npcs.getAllKeys()
                .forEach(key -> this.npcs.put(UUID.fromString(key), ComponentSerialization.CODEC.parse(ops, npcs.get(key)).getOrThrow()));
        CompoundTag uniques = tag.getCompound("UniqueNPCs");
        uniques.getAllKeys().forEach(key -> {
            ListTag idTag = uniques.getList(key, Tag.TAG_STRING);
            this.uniqueNPCS.put(ResourceLocation.parse(key), idTag.stream().map(t -> UUID.fromString(t.getAsString())).collect(Collectors.toSet()));
        });
        CompoundTag resetQuestTracker = tag.getCompound("ResetQuestNPCs");
        resetQuestTracker.getAllKeys().forEach(key -> {
            CompoundTag pairs = resetQuestTracker.getCompound(key);
            this.resetQuestNPCS.put(UUID.fromString(key), pairs.getAllKeys().stream().map(t -> Pair.of(UUID.fromString(t), ResourceLocation.parse(pairs.getString(t)))).collect(Collectors.toSet()));
        });
    }
}
