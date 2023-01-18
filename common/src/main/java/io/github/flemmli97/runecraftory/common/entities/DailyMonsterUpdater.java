package io.github.flemmli97.runecraftory.common.entities;

import net.minecraft.nbt.CompoundTag;

public class DailyMonsterUpdater extends DailyEntityUpdater<BaseMonster> {

    private int lastUpdateFood, lastUpdateBrush;

    public DailyMonsterUpdater(BaseMonster monster) {
        super(monster);
    }

    @Override
    protected void onUpdate() {
        this.entity.onDailyUpdate();
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

    @Override
    public CompoundTag save() {
        CompoundTag compound = super.save();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        compound.putInt("LastUpdateFood", this.lastUpdateFood);
        compound.putInt("LastUpdateBrush", this.lastUpdateBrush);
        return compound;
    }

    @Override
    public void read(CompoundTag compound) {
        super.read(compound);
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.lastUpdateFood = compound.getInt("LastUpdateFood");
        this.lastUpdateBrush = compound.getInt("LastUpdateBrush");
    }
}
