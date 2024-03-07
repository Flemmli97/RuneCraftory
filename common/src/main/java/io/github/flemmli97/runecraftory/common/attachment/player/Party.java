package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.common.config.GeneralConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Party {

    private final Set<UUID> members = new HashSet<>();

    public boolean addPartyMember(Entity entity) {
        if (this.isPartyFull())
            return false;
        return this.members.add(entity.getUUID());
    }

    public boolean removePartyMember(Entity entity) {
        return this.removePartyMember(entity.getUUID());
    }

    public boolean removePartyMember(UUID uuid) {
        return this.members.remove(uuid);
    }

    public boolean isPartyMember(Entity entity) {
        return this.members.contains(entity.getUUID());
    }

    public boolean isPartyFull() {
        return GeneralConfig.MAX_PARTY_SIZE != 0 && this.members.size() >= GeneralConfig.MAX_PARTY_SIZE;
    }

    public void load(CompoundTag tag) {
        ListTag members = tag.getList("Party", Tag.TAG_STRING);
        members.forEach(t -> this.members.add(UUID.fromString(t.getAsString())));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag members = new ListTag();
        this.members.forEach(uuid -> members.add(StringTag.valueOf(uuid.toString())));
        tag.put("Party", members);
        return tag;
    }
}
