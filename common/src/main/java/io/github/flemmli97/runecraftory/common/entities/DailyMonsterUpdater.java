package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;

public class DailyMonsterUpdater {

    private final BaseMonster monster;

    private int lastUpdateDay;
    private int lastUpdateFood, lastUpdateBrush;

    public DailyMonsterUpdater(BaseMonster monster) {
        this.monster = monster;
    }

    public void tick() {
        int day = WorldUtils.day(this.monster.level);
        if (Math.abs(this.lastUpdateDay - day) >= 1) {
            this.lastUpdateDay = day;
            this.monster.onDailyUpdate();
        }
    }

    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    public void setLastUpdateFood(int lastUpdateFood) {
        this.lastUpdateFood = lastUpdateFood;
    }

    public int getLastUpdateFood() {
        return this.lastUpdateFood;
    }

    public void setLastUpdateBrush(int lastUpdateBrush) {
        this.lastUpdateBrush = lastUpdateBrush;
    }

    public int getLastUpdateBrush() {
        return this.lastUpdateBrush;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        compound.putInt("LastUpdateFood", this.lastUpdateFood);
        compound.putInt("LastUpdateBrush", this.lastUpdateBrush);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.lastUpdateFood = compound.getInt("LastUpdateFood");
        this.lastUpdateBrush = compound.getInt("LastUpdateBrush");
    }
}
