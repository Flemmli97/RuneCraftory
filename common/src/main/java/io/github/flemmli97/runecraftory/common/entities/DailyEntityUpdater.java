package io.github.flemmli97.runecraftory.common.entities;

import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.Random;

public class DailyEntityUpdater<T extends LivingEntity> {

    private int dailyRandomSeed;

    protected final T entity;

    protected int lastUpdateDay;

    protected final Random random = new Random();

    public DailyEntityUpdater(T entity) {
        this.entity = entity;
    }

    public void tick() {
        if (this.dailyRandomSeed == 0)
            this.dailyRandomSeed = this.entity.getRandom().nextInt();
        int day = WorldUtils.day(this.entity.level);
        if (this.lastUpdateDay != day) {
            this.lastUpdateDay = day;
            this.dailyRandomSeed = this.entity.getRandom().nextInt();
            this.onUpdate();
        }
    }

    protected void onUpdate() {
    }

    public int getDailyRandomSeed() {
        return this.dailyRandomSeed;
    }

    public Random getDailyRandom() {
        this.random.setSeed(this.getDailyRandomSeed());
        return this.random;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        compound.putInt("DailyRandomSeed", this.dailyRandomSeed);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.dailyRandomSeed = compound.getInt("DailyRandomSeed");
    }
}