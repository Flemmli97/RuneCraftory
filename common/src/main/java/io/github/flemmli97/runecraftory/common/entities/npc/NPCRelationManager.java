package io.github.flemmli97.runecraftory.common.entities.npc;

import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NPCRelationManager {

    private final Map<UUID, NPCFriendPoints> playerHearts = new HashMap<>();
    private Pair<UUID, NPCRelation> relation;
    private UUIDNameMapper fatherUUID;
    private UUIDNameMapper motherUUID;
    private UUIDNameMapper[] childUUIDs;

    public NPCFriendPoints getFriendPointData(UUID uuid) {
        return this.playerHearts.computeIfAbsent(uuid, key -> new NPCFriendPoints());
    }

    public NPCRelation getRelationFor(UUID uuid) {
        if (this.relation != null && this.relation.getFirst().equals(uuid))
            return this.relation.getSecond();
        return NPCRelation.NORMAL;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        CompoundTag heartsTag = new CompoundTag();
        this.playerHearts.forEach((uuid, hearts) -> heartsTag.put(uuid.toString(), hearts.save()));
        compound.put("PlayerHearts", heartsTag);
        return compound;
    }

    public void load(CompoundTag compound) {
        CompoundTag heartsTag = compound.getCompound("PlayerHearts");
        heartsTag.getAllKeys().forEach(key -> {
            NPCFriendPoints points = new NPCFriendPoints();
            points.load(heartsTag.getCompound(key));
            this.playerHearts.put(UUID.fromString(key), points);
        });
    }
}
