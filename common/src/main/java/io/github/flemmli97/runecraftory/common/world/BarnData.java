package io.github.flemmli97.runecraftory.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BarnData {

    public final BlockPos pos;
    private int size = 0;
    private boolean hasRoof;

    private final Map<UUID, Integer> monsters = new HashMap<>();

    private boolean removed;

    public BarnData(BlockPos pos) {
        this.pos = pos;
    }

    public static BarnData fromTag(CompoundTag tag) {
        BarnData data = new BarnData(new BlockPos(tag.getInt("PosX"), tag.getInt("PosY"), tag.getInt("PosZ")));
        data.load(tag);
        return data;
    }

    public void update(int size, boolean hasRoof) {
        this.size = size;
        this.hasRoof = hasRoof;
    }

    public void addMonster(UUID uuid, int size) {
        this.monsters.put(uuid, size);
    }

    public void removeMonster(UUID uuid) {
        this.monsters.remove(uuid);
    }

    public boolean hasCapacityFor(int size, boolean needsRoof) {
        if (needsRoof && !this.hasRoof)
            return false;
        return this.monsters.values().stream().mapToInt(i -> i)
                .sum() + size <= this.getCapacity();
    }

    public int getCapacity() {
        if (this.getSize() < 2)
            return 0;
        if (this.getSize() < 4)
            return (this.getSize() - 1) * 2;
        return (this.getSize() - 3) * 4;
    }

    public boolean hasRoof() {
        return this.hasRoof;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public void remove() {
        this.removed = true;
    }

    public void load(CompoundTag tag) {
        this.size = tag.getInt("Size");
        this.hasRoof = tag.getBoolean("HasRoof");
        CompoundTag monsters = tag.getCompound("Monsters");
        monsters.getAllKeys().forEach(key -> this.monsters.put(UUID.fromString(key), monsters.getInt(key)));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("PosX", this.pos.getX());
        tag.putInt("PosY", this.pos.getY());
        tag.putInt("PosZ", this.pos.getZ());
        tag.putInt("Size", this.size);
        tag.putBoolean("HasRoof", this.hasRoof);
        CompoundTag monsters = new CompoundTag();
        this.monsters.forEach((uuid, integer) -> monsters.putInt(uuid.toString(), integer));
        tag.put("Monsters", monsters);
        return tag;
    }

    @Override
    public String toString() {
        return String.format("Barn[x=%d,y=%d,z=%d]; Size: %d, With Roof: %s, Capacity: %d, FreeCapacity: %d", this.pos.getX(), this.pos.getY(), this.pos.getZ(),
                this.size, this.hasRoof, this.getCapacity(), this.getCapacity() - this.monsters.values().stream().mapToInt(i -> i).sum());
    }
}
