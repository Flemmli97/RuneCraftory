package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

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
            if (this.monster.isTamed()) {
                int i = -1;
                ItemStack drop = ItemStack.EMPTY;
                for (Map.Entry<ItemStack, Integer> e : this.monster.dailyDrops().entrySet()) {
                    if (this.monster.getFriendlyPoints().getLevel() >= e.getValue() && i < e.getValue()) {
                        drop = e.getKey();
                        i = e.getValue();
                    }
                }
                this.monster.spawnAtLocation(drop.copy());
            }
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
