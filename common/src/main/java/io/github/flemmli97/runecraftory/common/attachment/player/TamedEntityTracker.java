package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.common.entities.BaseMonster;
import io.github.flemmli97.runecraftory.common.registry.ModTags;
import net.minecraft.nbt.CompoundTag;

public class TamedEntityTracker {

    private int tamedMonster;
    private int tamedBossMonster;

    public void tameEntity(BaseMonster monster) {
        this.tamedMonster++;
        if (monster.getType().is(ModTags.BOSS_MONSTERS))
            this.tamedBossMonster++;
    }

    public int getTameCount(boolean boss) {
        return boss ? this.tamedBossMonster : this.tamedMonster;
    }

    public void reset() {
        this.tamedMonster = 0;
        this.tamedBossMonster = 0;
    }

    public void read(CompoundTag tag) {
        this.tamedMonster = tag.getInt("TamedMonster");
        this.tamedBossMonster = tag.getInt("TamedBossMonster");
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("TamedMonster", this.tamedMonster);
        tag.putInt("TamedBossMonster", this.tamedBossMonster);
        return tag;
    }
}
