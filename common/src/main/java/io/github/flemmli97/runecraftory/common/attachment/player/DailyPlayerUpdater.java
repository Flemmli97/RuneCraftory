package io.github.flemmli97.runecraftory.common.attachment.player;

import io.github.flemmli97.runecraftory.api.enums.EnumSkills;
import io.github.flemmli97.runecraftory.common.utils.LevelCalc;
import io.github.flemmli97.runecraftory.common.utils.WorldUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class DailyPlayerUpdater {

    private int lastUpdateDay;

    private boolean gaveMonsterItem, ateFood;

    private final PlayerData data;

    public DailyPlayerUpdater(PlayerData data) {
        this.data = data;
    }

    public void tick(ServerPlayer player) {
        int day = WorldUtils.day(player.level);
        if (Math.abs(this.lastUpdateDay - day) >= 1) {
            this.lastUpdateDay = day;
            this.data.getShippingInv().shipItems(player);
            this.data.refreshShop(player);
            this.gaveMonsterItem = false;
            this.ateFood = false;
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

    public CompoundTag save() {
        CompoundTag compound = new CompoundTag();
        compound.putInt("LastUpdateDay", this.lastUpdateDay);
        compound.putBoolean("GaveMonsterItem", this.gaveMonsterItem);
        compound.putBoolean("AteFood", this.ateFood);
        return compound;
    }

    public void read(CompoundTag compound) {
        this.lastUpdateDay = compound.getInt("LastUpdateDay");
        this.gaveMonsterItem = compound.getBoolean("GaveMonsterItem");
        this.ateFood = compound.getBoolean("AteFood");
    }
}
