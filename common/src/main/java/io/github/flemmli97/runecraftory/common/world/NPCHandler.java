package io.github.flemmli97.runecraftory.common.world;

import io.github.flemmli97.runecraftory.api.datapack.NPCData;
import io.github.flemmli97.runecraftory.common.datapack.DataPackHandler;
import io.github.flemmli97.runecraftory.common.entities.npc.EntityNPCBase;
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
import java.util.stream.Collectors;

public class NPCHandler {

    private final Set<UUID> npcs = new HashSet<>();
    private final Map<ResourceLocation, Set<UUID>> uniqueNPCS = new HashMap<>();

    public boolean doesNPCExist(UUID uuid) {
        return this.npcs.contains(uuid);
    }

    public boolean addNPC(EntityNPCBase npc) {
        return this.npcs.add(npc.getUUID());
    }

    public boolean removeNPC(EntityNPCBase npc) {
        return this.npcs.remove(npc.getUUID());
    }

    public boolean canAssignNPC(NPCData data) {
        if (data.unique() == 0)
            return true;
        ResourceLocation res = DataPackHandler.SERVER_PACK.npcDataManager().getId(data);
        Set<UUID> uuids = this.uniqueNPCS.get(res);
        return uuids == null || uuids.size() < data.unique();
    }

    public boolean addUniqueNPC(UUID uuid, NPCData data) {
        if (data.unique() == 0)
            return false;
        ResourceLocation res = DataPackHandler.SERVER_PACK.npcDataManager().getId(data);
        return this.uniqueNPCS.computeIfAbsent(res, key -> new HashSet<>()).add(uuid);
    }

    public boolean removeUniqueNPC(UUID uuid, NPCData data) {
        if (data.unique() == 0)
            return false;
        ResourceLocation res = DataPackHandler.SERVER_PACK.npcDataManager().getId(data);
        return this.uniqueNPCS.computeIfAbsent(res, key -> new HashSet<>()).remove(uuid);
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
    }
}
