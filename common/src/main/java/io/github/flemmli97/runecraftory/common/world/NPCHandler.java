package io.github.flemmli97.runecraftory.common.world;

import com.mojang.datafixers.util.Pair;
import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
import io.github.flemmli97.runecraftory.integration.simplequest.SimpleQuestIntegration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCHandler {

    private final Set<UUID> npcs = new HashSet<>();
    private final Map<ResourceLocation, Set<UUID>> uniqueNPCS = new HashMap<>();
    private final Map<UUID, Set<Pair<UUID, ResourceLocation>>> resetQuestNPCS = new HashMap<>();

    public boolean doesNPCExist(UUID uuid) {
        return this.npcs.contains(uuid);
    }

    public boolean addNPC(EntityNPCBase npc) {
        return this.npcs.add(npc.getUUID());
    }

    public void removeNPC(EntityNPCBase npc, Entity.RemovalReason reason) {
        if (reason.shouldDestroy()) {
            npc.getServer().getPlayerList().getPlayers().forEach(p -> {
                SimpleQuestIntegration.INST().removeQuestFor(p, npc);
            });
            npc.getFamily().markAsDead();
            this.npcs.remove(npc.getUUID());
        }
    }

    public boolean canAssignNPC(NPCData data) {
        if (data.unique() == 0)
            return true;
        ResourceLocation res = DataPackHandler.INSTANCE.npcDataManager().getId(data);
        Set<UUID> uuids = this.uniqueNPCS.get(res);
        return uuids == null || uuids.size() < data.unique();
    }

    public boolean addUniqueNPC(UUID uuid, NPCData data) {
        if (data.unique() == 0)
            return false;
        ResourceLocation res = DataPackHandler.INSTANCE.npcDataManager().getId(data);
        return this.uniqueNPCS.computeIfAbsent(res, key -> new HashSet<>()).add(uuid);
    }

    public boolean removeUniqueNPC(UUID uuid, NPCData data) {
        if (data.unique() == 0)
            return false;
        ResourceLocation res = DataPackHandler.INSTANCE.npcDataManager().getId(data);
        return this.uniqueNPCS.computeIfAbsent(res, key -> new HashSet<>()).remove(uuid);
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

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag npcs = new ListTag();
        this.npcs.forEach(uuid -> npcs.add(StringTag.valueOf(uuid.toString())));
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

    public void load(CompoundTag tag) {
        tag.getList("NPCs", Tag.TAG_STRING)
                .forEach(t -> this.npcs.add(UUID.fromString(t.getAsString())));
        CompoundTag uniques = tag.getCompound("UniqueNPCs");
        uniques.getAllKeys().forEach(key -> {
            ListTag idTag = uniques.getList(key, Tag.TAG_STRING);
            this.uniqueNPCS.put(new ResourceLocation(key), idTag.stream().map(t -> UUID.fromString(t.getAsString())).collect(Collectors.toSet()));
        });
        CompoundTag resetQuestTracker = tag.getCompound("ResetQuestNPCs");
        resetQuestTracker.getAllKeys().forEach(key -> {
            CompoundTag pairs = resetQuestTracker.getCompound(key);
            this.resetQuestNPCS.put(UUID.fromString(key), pairs.getAllKeys().stream().map(t -> Pair.of(UUID.fromString(t), new ResourceLocation(pairs.getString(t)))).collect(Collectors.toSet()));
        });
    }
}
