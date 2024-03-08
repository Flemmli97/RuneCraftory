package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.lib.RunecraftoryTags;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

public class TamedEntityTracker {

    private final Object2IntMap<ResourceLocation> tamedMonsters = new Object2IntArrayMap<>();

    private int tamedMonster;
    private int tamedBossMonster;

    public void tameEntity(BaseMonster monster) {
        this.tamedMonster++;
        if (monster.getType().is(RunecraftoryTags.BOSS_MONSTERS))
            this.tamedBossMonster++;
        this.tamedMonsters.computeInt(Registry.ENTITY_TYPE.getKey(monster.getType()),
                (id, o) -> o == null ? 1 : ++o);
    }

    public int getTotalTameCount(boolean boss) {
        return boss ? this.tamedBossMonster : this.tamedMonster;
    }

    public int getTameCount(EntityType<?> type) {
        return this.tamedMonsters.getInt(Registry.ENTITY_TYPE.getKey(type));
    }

    public void reset() {
        this.tamedMonster = 0;
        this.tamedBossMonster = 0;
    }

    public void read(CompoundTag tag) {
        this.tamedMonster = tag.getInt("TamedMonster");
        this.tamedBossMonster = tag.getInt("TamedBossMonster");
        CompoundTag tamed = tag.getCompound("TamedMonstersTrack");
        tamed.getAllKeys().forEach(key -> this.tamedMonsters.put(new ResourceLocation(key), tamed.getInt(key)));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("TamedMonster", this.tamedMonster);
        tag.putInt("TamedBossMonster", this.tamedBossMonster);
        CompoundTag tamed = new CompoundTag();
        this.tamedMonsters.forEach((id, val) -> {
            tamed.putInt(id.toString(), val);
        });
        tag.put("TamedMonstersTrack", tamed);
        return tag;
    }
}
