package io.github.flemmli97.runecraftory.common.world;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import io.github.flemmli97.runecraftory.RuneCraftory;
import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class BarnData {

    public final GlobalPos pos;
    private int size = 0;
    private boolean hasRoof;

    private final Map<UUID, Integer> monsters = new HashMap<>();
    private final Set<BaseMonster> listeners = new HashSet<>();

    private boolean removed;

    public BarnData(GlobalPos pos) {
        this.pos = pos;
    }

    public static BarnData fromTag(CompoundTag tag) {
        DataResult<GlobalPos> dataResult = GlobalPos.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("Pos")));
        BarnData data = new BarnData(dataResult.getOrThrow(false, RuneCraftory.logger::error));
        data.load(tag);
        return data;
    }

    public void update(int size, boolean hasRoof) {
        this.size = size;
        this.hasRoof = hasRoof;
        this.listeners.removeIf(m -> {
            if (m.behaviourState() == BaseMonster.Behaviour.WANDER_HOME) {
                if (m.level.dimension() == this.pos.dimension())
                    m.restrictTo(this.pos.pos(), this.getSize() + 1);
            }
            return m.isRemoved();
        });
    }

    public void addMonster(BaseMonster monster, int size) {
        this.monsters.put(monster.getUUID(), size);
        this.listeners.add(monster);
    }

    public void removeMonster(BaseMonster monster) {
        this.monsters.remove(monster.getUUID());
        this.listeners.remove(monster);
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
        return (this.getSize() - 3) * 4 + 2;
    }

    public boolean hasRoof() {
        return this.hasRoof;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isInvalid() {
        return this.removed || this.size < 2;
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
        GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.pos).resultOrPartial(RuneCraftory.logger::error)
                .ifPresent(t -> tag.put("Pos", t));
        tag.putInt("Size", this.size);
        tag.putBoolean("HasRoof", this.hasRoof);
        CompoundTag monsters = new CompoundTag();
        this.monsters.forEach((uuid, integer) -> monsters.putInt(uuid.toString(), integer));
        tag.put("Monsters", monsters);
        return tag;
    }

    @Override
    public String toString() {
        return String.format("Barn[%s]; Size: %d, With Roof: %s, Capacity: %d, FreeCapacity: %d", this.pos,
                this.size, this.hasRoof, this.getCapacity(), this.getCapacity() - this.monsters.values().stream().mapToInt(i -> i).sum());
    }
}
