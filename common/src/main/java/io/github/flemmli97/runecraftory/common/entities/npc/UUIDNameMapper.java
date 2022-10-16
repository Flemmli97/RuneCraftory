package io.github.flemmli97.runecraftory.common.entities.npc;

import io.github.flemmli97.tenshilib.common.entity.EntityUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class UUIDNameMapper {

    private UUID uuid;
    private Component name;

    public UUIDNameMapper(UUID uuid, Component name) {
        this.uuid = uuid;
        this.name = name;
    }

    public void recalculateName(Level level) {
        if (this.uuid != null) {
            Entity entity = EntityUtil.findFromUUID(Entity.class, level, this.uuid);
            if (entity != null)
                this.name = entity.getName();
        }
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public Component getName(Level level) {
        if (this.name == null && this.uuid != null) {
            Entity entity = EntityUtil.findFromUUID(Entity.class, level, this.uuid);
            if (entity != null)
                this.name = entity.getName();
        }
        return this.name;
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        if (this.uuid != null)
            tag.putUUID("UUID", this.uuid);
        return tag;
    }

    public void load(CompoundTag tag) {
        if (tag.hasUUID("UUID"))
            this.uuid = tag.getUUID("UUID");
    }
}
