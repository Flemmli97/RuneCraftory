package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class DailyMonsterUpdater {

    private final BaseMonster monster;

    private int lastUpdateDay;

    public DailyMonsterUpdater(BaseMonster monster) {
        this.monster = monster;
    }

    public void tick() {
        int day = WorldUtils.day(this.monster.level);
        if (Math.abs(this.lastUpdateDay - day) >= 1) {
            this.lastUpdateDay = day;
            if (this.monster.isTamed()) {
                int i = -1;
                ItemStack drop = ItemStack.EMPTY;
                for (Map.Entry<ItemStack, Integer> e : this.monster.dailyDrops().entrySet()) {
                    if (this.monster.getFriendlyPoints()[0] >= e.getValue() && i > e.getValue()) {
                        drop = e.getKey();
                        i = e.getValue();
                    }
                }
                this.monster.spawnAtLocation(drop.copy());
            }
        }
    }

    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
    }
}
