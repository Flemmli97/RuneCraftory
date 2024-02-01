package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
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
    private int dailyRandomSeed;

    private final PlayerData data;

    public DailyPlayerUpdater(PlayerData data) {
        this.data = data;
    }

    public void tick(ServerPlayer player) {
        if (this.dailyRandomSeed == 0)
            this.dailyRandomSeed = player.getRandom().nextInt();
        int day = WorldUtils.day(player.level);
        if (this.lastUpdateDay != day) {
            this.lastUpdateDay = day;
            this.data.getShippingInv().shipItems(player);
            this.data.refreshShop(player);
            this.gaveMonsterItem = false;
            this.ateFood = false;
            this.bathCounter = 0;
            this.dailyRandomSeed = player.getRandom().nextInt();
        }
    }

    public void setLastUpdateDay(int lastUpdateDay) {
        this.lastUpdateDay = lastUpdateDay;
    }

    public void onGiveMonsterItem(ServerPlayer player) {
        if (!this.gaveMonsterItem) {
            this.gaveMonsterItem = true;
            LevelCalc.levelSkill(player, this.data, EnumSkills.TAMING, 4);
        }
    }

    public void onFoodEaten(ServerPlayer player) {
        if (!this.ateFood) {
            this.ateFood = true;
            LevelCalc.levelSkill(player, this.data, EnumSkills.EATING, 50);
        }
    }

    public int getBathCounter() {
        return this.bathCounter;
    }

    public void increaseBathCounter() {
        this.bathCounter++;
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
        compound.putBoolean("GaveMonsterItem", this.gaveMonsterItem);
        compound.putBoolean("AteFood", this.ateFood);
        compound.putInt("BathCounter", this.bathCounter);
        compound.putInt("DailyRandomSeed", this.dailyRandomSeed);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.gaveMonsterItem = compound.getBoolean("GaveMonsterItem");
        this.ateFood = compound.getBoolean("AteFood");
        this.bathCounter = compound.getInt("BathCounter");
        this.dailyRandomSeed = compound.getInt("DailyRandomSeed");
    }
}
