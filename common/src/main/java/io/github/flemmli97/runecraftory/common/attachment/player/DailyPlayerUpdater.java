package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.attachment.Skills;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.Random;

public class DailyPlayerUpdater {

    private int lastUpdateDay = -1;

    private boolean gaveMonsterItem, ateFood;
    private int bathCounter;

    private final Random random = new Random();
    private long dailyRandomSeed;

    private final PlayerData data;

    public DailyPlayerUpdater(PlayerData data) {
        this.data = data;
    }

    public void tick(ServerPlayer player) {
        if (this.dailyRandomSeed == 0)
            this.dailyRandomSeed = player.getRandom().nextLong();
        int day = WorldUtils.day(player.level());
        if (this.lastUpdateDay != day) {
            this.lastUpdateDay = day;
            this.data.getShippingInv().shipItems(player);
            this.data.refreshShop();
            this.gaveMonsterItem = false;
            this.ateFood = false;
            this.bathCounter = 0;
            this.dailyRandomSeed = player.getRandom().nextLong();
        }
    }

    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    public void onGiveMonsterItem() {
        if (!this.gaveMonsterItem) {
            this.gaveMonsterItem = true;
            LevelCalc.levelSkill(this.data, Skills.TAMING, 4);
        }
    }

    public void onFoodEaten() {
        if (!this.ateFood) {
            this.ateFood = true;
            LevelCalc.levelSkill(this.data, Skills.EATING, 50);
        }
    }

    public int getBathCounter() {
        return this.bathCounter;
    }

    public void increaseBathCounter() {
        this.bathCounter++;
    }

    public long getDailyRandomSeed() {
        return this.dailyRandomSeed;
    }

    public Random getDailyRandom() {
        this.random.setSeed(this.getDailyRandomSeed());
        return this.random;
    }

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        compound.putBoolean("GaveMonsterItem", this.gaveMonsterItem);
        compound.putBoolean("AteFood", this.ateFood);
        compound.putInt("BathCounter", this.bathCounter);
        compound.putLong("DailyRandomSeed", this.dailyRandomSeed);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.gaveMonsterItem = compound.getBoolean("GaveMonsterItem");
        this.ateFood = compound.getBoolean("AteFood");
        this.bathCounter = compound.getInt("BathCounter");
        this.dailyRandomSeed = compound.getLong("DailyRandomSeed");
    }
}
